package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.Model;
import java.util.Properties;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 *
 * @author binary gamura
 */
public class LocationPoller {
    
    private CloseableHttpClient httpClient;
    
    private Thread pollingThread;
    
    private Model model;
    
    public LocationPoller(Model model){
	this.model = model;
    }

    public void start(CloseableHttpClient httpClient){
	
    }
    
    public void stop(){
	
    }
    
    static class LocationPollerThread implements Runnable
    {

	public static long POLL_INTERVAL = 6000l;
	
	private final Model model;
        
        private final Properties properties;
        
        private final HttpClient client;
        
	public LocationPollerThread(Model model, Properties properties, HttpClient client)
	{
	    this.model = model;
            this.client = client;
            this.properties = properties;
	}
	
	@Override
	public void run()
	{
	    while(!Thread.currentThread().isInterrupted()){
		String uriString = properties.getProperty("api_url")+"/";
	    }
	}	
    }
}
