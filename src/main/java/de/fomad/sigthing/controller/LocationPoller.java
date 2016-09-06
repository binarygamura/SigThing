package de.fomad.sigthing.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.fomad.sigthing.model.Location;
import de.fomad.sigthing.model.Model;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class LocationPoller {    
    
    private static final Logger LOGGER = LogManager.getLogger(LocationPoller.class);
    
    private Thread pollingThread;
    
    private final Model model;
    
    private LocationPollerThread poller;
    
    private final Properties properties;
    
    public LocationPoller(Model model, Properties properties){
	this.model = model;
        this.properties = properties;
    }

    public void start(CloseableHttpClient httpClient){
	if(pollingThread == null){
            poller = new LocationPollerThread(model, properties, httpClient);
            pollingThread = new Thread(poller);
            pollingThread.start();
        }
    }
    
    public void stop(){
	if(pollingThread != null){
            pollingThread.interrupt();
            pollingThread = null;
        }
    }
    
    static class LocationPollerThread implements Runnable
    {
        private Gson gson;
        
	public static long POLL_INTERVAL = 6000l;
	
	private final Model model;
        
        private final Properties properties;
        
        private final CloseableHttpClient client;
        
	public LocationPollerThread(Model model, Properties properties, CloseableHttpClient client)
	{
	    this.model = model;
            this.client = client;
            this.properties = properties;
	}
	
	@Override
	public void run()
	{
            try
            {
                Location location;
                URI locationUri = new URI(properties.getProperty("api_url")+"/characters/"+model.getCharacter().getId()+"/location/");
                gson = new GsonBuilder().create();
                while(!Thread.currentThread().isInterrupted()){
                     HttpUriRequest request = RequestBuilder
                    .get()
                    .addHeader(new BasicHeader("Authorization", "Bearer "+model.getAuthData().getAccessToken()))
                    .setUri(locationUri)
                    .build();
                    try (CloseableHttpResponse response = client.execute(request))
                    {
                        if (response.getStatusLine().getStatusCode() != 200)
                        {
                            String responseMessage = EntityUtils.toString(response.getEntity());
                            throw new IOException("unable to query character data! ("+response.getStatusLine()+") " + responseMessage);
                        }
                        String responseBody = EntityUtils.toString(response.getEntity());
                        location = gson.fromJson(responseBody, Location.class);
                        LOGGER.info("got location from solar system \""+location.getSolarSystem().getName()+"\".");
                        //{"message": "Authentication needed, bad token", "key": "authNeeded", "exceptionType": "UnauthorizedError"}
                    }
                    Thread.sleep(POLL_INTERVAL);
                }
            }
            catch(Exception ex){
                LOGGER.error("error while polling location!", ex);
            }
	}	
    }
}
