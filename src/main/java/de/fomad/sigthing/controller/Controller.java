package de.fomad.sigthing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.fomad.sigthing.model.AuthData;
import de.fomad.sigthing.model.CharacterInfo;
import de.fomad.sigthing.model.ControllerEvent;
import de.fomad.sigthing.model.DummyWebServerEvent;
import de.fomad.sigthing.model.Model;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import de.fomad.sigthing.model.Character;

/**
 *
 * @author binary gamura
 */
public class Controller extends Observable implements Observer
{
    private final DummyWebServer webserver;

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    private final CloseableHttpClient httpClient;

    private final HttpHost loginHost;
    
    private final HttpClientContext loginContext;
    
    private DatabaseController databaseController;
    
    private final LocationPoller locationPoller;
    
    private final Model model;

    private final Properties properties;
    
    private final Gson gson;
    
    public Controller(Properties properties) throws URISyntaxException
    {
        this.properties = properties;   
        model = new Model();
	locationPoller = new LocationPoller(model, properties);
	webserver = new DummyWebServer(new URI(properties.getProperty("callback_url")));
	webserver.addObserver(Controller.this);
        URI verificationUrl = new URI(properties.getProperty("auth_url")+"/token");
	int port = verificationUrl.getScheme().equalsIgnoreCase("https") ? 443 : 80;
	loginHost = new HttpHost(verificationUrl.getHost(), port, verificationUrl.getScheme());
	CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	credentialsProvider.setCredentials(new AuthScope(loginHost.getHostName(), port), new UsernamePasswordCredentials(properties.getProperty("client_id"), properties.getProperty("client_secret")));
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();

	AuthCache authCache = new BasicAuthCache();
	BasicScheme basicAuth = new BasicScheme();
	authCache.put(loginHost, basicAuth);
	
	loginContext = HttpClientContext.create();
	loginContext.setAuthCache(authCache);
	
	httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
    }

    public Model getModel()
    {
        return model;
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
    
    private void queryCharacterSheet() throws URISyntaxException, IOException{
        URI dataUri = new URI(properties.get("api_url")+"/characters/"+model.getCharacterInfo().getId()+"/");
        HttpUriRequest request = RequestBuilder
                .get()
                .addHeader(new BasicHeader("Authorization", "Bearer "+model.getAuthData().getAccessToken()))
                .setUri(dataUri)
                .build();
        try (CloseableHttpResponse response = httpClient.execute(request))
        {
            if (response.getStatusLine().getStatusCode() != 200)
            {
                String responseMessage = EntityUtils.toString(response.getEntity());
                throw new IOException("unable to query character data! " + responseMessage);
            }
            Character character = gson.fromJson(EntityUtils.toString(response.getEntity()), Character.class);
            model.setCharacter(character);
            
            ControllerEvent successEvent = new ControllerEvent(character, ControllerEvent.EventType.GOT_CHARACTER);
            setChanged();
            notifyObservers(successEvent);
        }
    }

    private void queryCharacterInformation() throws URISyntaxException, IOException{
        URI verifyUri = new URI(properties.get("auth_url")+"/verify");
        HttpUriRequest request = RequestBuilder
                .get()
                .addHeader(new BasicHeader("Authorization", "Bearer "+model.getAuthData().getAccessToken()))
                .setUri(verifyUri)
                .build();
        
        try (CloseableHttpResponse response = httpClient.execute(request))
        {
            if (response.getStatusLine().getStatusCode() != 200)
            {
                String responseMessage = EntityUtils.toString(response.getEntity());
                throw new IOException("unable to verify access token! " + responseMessage);
            }
            CharacterInfo info = gson.fromJson(EntityUtils.toString(response.getEntity()), CharacterInfo.class);
            model.setCharacterInfo(info);
            
            ControllerEvent successEvent = new ControllerEvent(info, ControllerEvent.EventType.GOT_CHARACTER_INFO);
            setChanged();
            notifyObservers(successEvent);
        }
    }
    
    
    private void getAccessToken(AuthData authData, boolean useRefreshtoken) throws UnsupportedEncodingException, IOException, URISyntaxException
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

        URI verificationUrl = new URI(properties.getProperty("auth_url")+"/token");
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
    
    private void startPollingLocation(){
        locationPoller.start(httpClient);
        LOGGER.info("started location polling.");
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
                        model.setAuthData(authData);
			getAccessToken(authData, false);
                        queryCharacterInformation();
                        queryCharacterSheet();
                        startPollingLocation();
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
