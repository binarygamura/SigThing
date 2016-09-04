package de.fomad.sigthing.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.fomad.sigthing.model.AuthData;
import de.fomad.sigthing.model.ControllerEvent;
import de.fomad.sigthing.model.DummyWebServerEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

/**
 *
 * @author binary gamura
 */
public class Controller extends Observable implements Observer
{

    private final DummyWebServer webserver;

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    private final CloseableHttpClient httpClient;

    private final URI verificationUrl;

    private final String clientId;

    private final String clientSecret;
    
    private final HttpHost loginHost;
    
    private final HttpClientContext loginContext;
    
    private DatabaseController databaseController;
    
    private LocationPoller locationPoller;
    
    

    public Controller(URI callbackUri, URI verificationUrl, String clientId, String clientSecret)
    {
	this.verificationUrl = verificationUrl;
	this.clientId = clientId;
	this.clientSecret = clientSecret;
	locationPoller = new LocationPoller();
	webserver = new DummyWebServer(callbackUri);
	webserver.addObserver(Controller.this);
	int port = verificationUrl.getScheme().equalsIgnoreCase("https") ? 443 : 80;
	loginHost = new HttpHost(verificationUrl.getHost(), port, verificationUrl.getScheme());
	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	credentialsProvider.setCredentials(new AuthScope(loginHost.getHostName(), port), new UsernamePasswordCredentials(clientId, clientSecret));

	AuthCache authCache = new BasicAuthCache();
	BasicScheme basicAuth = new BasicScheme();
	authCache.put(loginHost, basicAuth);
	
	loginContext = HttpClientContext.create();
	loginContext.setAuthCache(authCache);
	
	httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    public void startLoginProcess() throws IOException
    {
	if (webserver.startListening())
	{
	    LOGGER.info("start listening for redirect from local browser.");
	}
	else
	{
	    LOGGER.info("webserver is already listening.");
	}
    }

    
    
    private void getAccessToken(AuthData authData, boolean useRefreshtoken) throws UnsupportedEncodingException, IOException
    {
	UrlEncodedFormEntity form;
	authData.setLastTokenRefresh(System.currentTimeMillis());
	if(!useRefreshtoken){
		form = new UrlEncodedFormEntity(Arrays.asList(
			new BasicNameValuePair("code", authData.getAccessCode()),
			new BasicNameValuePair("grant_type", "authorization_code")));
	}
	else{
	    form = new UrlEncodedFormEntity(Arrays.asList(
			new BasicNameValuePair("refresh_token", authData.getRefreshToken()),
			new BasicNameValuePair("grant_type", "refresh_token")));
	}

	    HttpUriRequest request = RequestBuilder
		    .post()
		    .setEntity(form)
		    .setUri(verificationUrl)
		    .build();
	    try (CloseableHttpResponse response = httpClient.execute(loginHost, request, loginContext))
	    {
		if (response.getStatusLine().getStatusCode() != 200)
		{
		    String responseMessage = EntityUtils.toString(response.getEntity());
		    throw new IOException("unable to trade the access code to a token! " + responseMessage);
		}
		String responseBody = EntityUtils.toString(response.getEntity());
		JsonParser parser = new JsonParser();
		JsonObject responseObject = parser.parse(responseBody).getAsJsonObject();
		String refreshToken = responseObject.get("refresh_token").getAsString();
		long expiresIn = responseObject.get("expires_in").getAsLong();
		String accessToken = responseObject.get("access_token").getAsString();

		authData.setAccessToken(accessToken);
		authData.setRefreshToken(refreshToken);
		authData.setExpiresIn(expiresIn);

		LOGGER.info("got token from server " + accessToken + " which expires in " + expiresIn + "s.");

		ControllerEvent successEvent = new ControllerEvent(null, ControllerEvent.EventType.GOT_TOKEN);
		setChanged();
		notifyObservers(successEvent);

	    }
    }

    @Override
    public void update(Observable o, Object arg)
    {
	try
	{
	    if (o == webserver)
	    {
		DummyWebServerEvent event = (DummyWebServerEvent) arg;
		switch (event.getType())
		{
		    case ERROR:
			ControllerEvent<Exception> errorEvent = new ControllerEvent<>((Exception) event.getPayload(), ControllerEvent.EventType.ERROR);
			setChanged();
			notifyObservers(errorEvent);
			break;
		    case GOT_RESPONSE:
			AuthData authData = (AuthData) event.getPayload();
			getAccessToken(authData, false);
			break;
		}
	    }
	}
	catch (Exception ex)
	{
	    LOGGER.fatal("error while handling events.", ex);
	    ControllerEvent<Exception> event = new ControllerEvent<>(ex, ControllerEvent.EventType.ERROR);
	    setChanged();
	    notifyObservers(event);
	}
    }
    
    public void init() throws NativeHookException, ClassNotFoundException, SQLException{
        //turn off debug logging from JNativeHook. great lib btw!
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        GlobalScreen.registerNativeHook();
        
        KeyLogger keyLogger = new KeyLogger();
        
        GlobalScreen.addNativeKeyListener(keyLogger);
        LOGGER.info("initialized key logger");
	
	databaseController = new DatabaseController();
	databaseController.initDatabase();
	
	LOGGER.info("created database controller.");
    }

    public void shutDown()
    {
        try
        {
            GlobalScreen.unregisterNativeHook();
        }
        catch(Exception ex)
        {
            LOGGER.warn("error while shutting down key logger.", ex);
        }
	if (httpClient != null)
	{
	    try
	    {
		httpClient.close();
	    }
	    catch (Exception ex)
	    {
		LOGGER.warn("error while shutting down the http client.", ex);
	    }
	}
	webserver.stopListening();
    }
}
