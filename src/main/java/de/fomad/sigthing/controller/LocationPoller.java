package de.fomad.sigthing.controller;

import org.apache.http.impl.client.CloseableHttpClient;

/**
 *
 * @author binary gamura
 */
public class LocationPoller {
    
    private CloseableHttpClient httpClient;
    
    private Thread pollingThread;
    
    public LocationPoller(){
	
    }

    public void start(){
	
    }
    
    public void stop(){
	
    }
    
    static class LocationPollerThread implements Runnable
    {

	public static long POLL_INTERVAL = 6000l;
	
	private String accessToken;
	
	public LocationPollerThread(String accessToken)
	{
	    this.accessToken = accessToken;
	}
	
	@Override
	public void run()
	{
	    while(!Thread.currentThread().isInterrupted()){
		
	    }
	}	
    }
}
