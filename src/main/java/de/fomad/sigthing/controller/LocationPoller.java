package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.Location;
import de.fomad.sigthing.model.LocationPollerEvent;
import de.fomad.sigthing.model.Model;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Observable;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class LocationPoller extends Observable implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(LocationPoller.class);

    private Thread pollingThread;

    private static final long POLL_INTERVAL = 6000l;

    private static final String THREAD_NAME = "location polling thread";

    private final Model model;

    private final HttpController httpController;

    private final String apiUrl;

    private Location lastLocation;
    
    private boolean wasOffline;

    public LocationPoller(Model model, String apiUrl, HttpController httpController) {
	this.model = model;
	this.apiUrl = apiUrl;
	this.httpController = httpController;
    }

    public void start() {
	if (pollingThread == null) {

	    pollingThread = new Thread(this);
	    pollingThread.setName(THREAD_NAME);
	    pollingThread.start();
	}
    }

    public void stop() {
	if (pollingThread != null) {
	    pollingThread.interrupt();
	    pollingThread = null;
	}
    }

    @Override
    public void run() {
	try {
	    Location location;
	    URI locationUri = new URI(apiUrl + "/characters/" + model.getCharacter().getId() + "/location/");
	    while (!Thread.currentThread().isInterrupted()) {
		location = httpController.makeApiGetRequest(locationUri, Location.class, true);
                
                //{"message": "Authentication needed, bad token", "key": "authNeeded", "exceptionType": "UnauthorizedError"}
                if(location.getSolarSystem() == null){
                    if(!wasOffline){
                        setChanged();
                        notifyObservers(new LocationPollerEvent(null, LocationPollerEvent.Type.OFFLINE));
                        wasOffline = true;
                    }
                }
                else if (!location.equals(lastLocation) || wasOffline) {
		    lastLocation = location;
                    wasOffline = false;
                    LOGGER.info("got location from solar system \"" + location.getSolarSystem().getName() + "\".");
		    setChanged();
		    notifyObservers(new LocationPollerEvent(location.getSolarSystem(), LocationPollerEvent.Type.LOCATION));
		}

		Thread.sleep(POLL_INTERVAL);
	    }
	}
	catch (URISyntaxException | IOException | InterruptedException ex) {
	    LOGGER.error("error while polling location!", ex);
	}
    }
}
