package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.AuthData;
import de.fomad.sigthing.model.DummyWebServerEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class DummyWebServer extends Observable implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(DummyWebServer.class);

    private final int port;

    private ServerSocket socket;

    private Thread thread;

    public DummyWebServer(int port) {
	this.port = port;
    }

    public boolean startListening() throws IOException {
	boolean wasStarted = false;
	if (thread == null) {
	    LOGGER.info("starting webserver thread on port " + port);
	    socket = new ServerSocket(port);
	    thread = new Thread(this);
	    thread.start();
	    wasStarted = true;
	}
	return wasStarted;
    }

    public boolean stopListening() {
	boolean wasStopped = false;
	if (thread != null) {

	    try {
		thread.interrupt();
		if (socket != null) {
		    socket.close();
		}
	    }
	    catch (IOException ex) {
		LOGGER.warn("error while stopping the dummy webserver.", ex);
	    }
	    finally {
		thread = null;
	    }
	}
	LOGGER.info("stopped webserver thread.");
	return wasStopped;
    }

    @Override
    public void run() {
	try {
	    LOGGER.info("ready to accept incoming connections.");
	    try (
		    Socket accepted = socket.accept();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(accepted.getInputStream(), StandardCharsets.UTF_8));
		    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(accepted.getOutputStream(), StandardCharsets.UTF_8))) {
		LOGGER.info("accepted connection from " + accepted.getInetAddress());
		String requestLine = reader.readLine();
		if(requestLine == null){
		    throw new IOException("unable to read request line from http response.");
		}
		LOGGER.debug("request line: " + requestLine);
		String line;
		while ((line = reader.readLine()) != null) {
		    LOGGER.debug("line: " + line);
		    if (line.trim().isEmpty()) {
			break;
		    }
		}

		String htmlResponse = "<html><head><title>Login complete</title></head><body><h1>Login complete</h1><p>You can close now this tab.</p></body></html>";
		byte[] responseBodyBytes = htmlResponse.getBytes("UTF-8");
		writer.write("HTTP/1.1 200 OK\n");
		writer.write("Server: DummyWebServer\n");
		writer.write("Content-Length: " + responseBodyBytes.length + "\n");
		writer.write("Content-Type: text/html;charset=UTF-8\n");
		writer.write("\n");
		writer.write(htmlResponse);
		writer.flush();
		LOGGER.info("sent response to the browser.");
		
		URI completeCallbackUrl = new URI(requestLine.split("\\s")[1]);
		List<NameValuePair> params = URLEncodedUtils.parse(completeCallbackUrl, "UTF-8");
		List<NameValuePair> matches = params.stream().filter((e) -> e.getName().equalsIgnoreCase("code")).limit(1).collect(Collectors.toList());
		if (matches.isEmpty()) {
		    throw new Exception("unable to extract authorization code from response!");
		}
		LOGGER.info("got access token \"" + matches.get(0).getValue() + "\".");
		AuthData authData = new AuthData();
		authData.setAccessCode(matches.get(0).getValue());
		DummyWebServerEvent<AuthData> event = new DummyWebServerEvent<>(authData, DummyWebServerEvent.EventType.GOT_RESPONSE);
		setChanged();
		notifyObservers(event);
	    }
	}
	catch (IOException ex) {
	    LOGGER.error("IO-Exception while reading the response from local browser.", ex);
	    DummyWebServerEvent<Exception> event = new DummyWebServerEvent<>(ex, DummyWebServerEvent.EventType.ERROR);
	    setChanged();
	    notifyObservers(event);
	}
	catch (Exception ex) {
	    LOGGER.error("critical error!", ex);
	    DummyWebServerEvent<Exception> event = new DummyWebServerEvent<>(ex, DummyWebServerEvent.EventType.ERROR);
	    setChanged();
	    notifyObservers(event);
	}
	finally {
	    LOGGER.info("internal webserver terminated.");
	}
    }
}
