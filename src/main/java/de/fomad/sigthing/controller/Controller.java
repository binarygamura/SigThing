package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.AuthData;
import de.fomad.sigthing.model.CharacterInfo;
import de.fomad.sigthing.model.ControllerEvent;
import de.fomad.sigthing.model.DummyWebServerEvent;
import de.fomad.sigthing.model.Model;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import de.fomad.sigthing.model.Character;
import de.fomad.sigthing.model.LocationPollerEvent;
import de.fomad.sigthing.model.SolarSystem;
import de.fomad.sigthing.model.SolarSystemInformation;
import java.beans.PropertyVetoException;

/**
 *
 * @author binary gamura
 */
public class Controller extends Observable implements Observer {

    private final DummyWebServer webserver;

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    private DatabaseController databaseController;

    private final LocationPoller locationPoller;

    private final Model model;

    private final HttpController httpController;

    private final String authUrl;

    private final String apiUrl;
    
    private KeyLogger keyLogger;

    public Controller(int callbackPort, String authUrl, String apiUrl, String clientId, String clientSecret) throws URISyntaxException {

	this.apiUrl = apiUrl;
	this.authUrl = authUrl;

	model = new Model();

	webserver = new DummyWebServer(callbackPort);
	webserver.addObserver(Controller.this);
	httpController = new HttpController(new URI(authUrl + "/token"), clientId, clientSecret);
	locationPoller = new LocationPoller(model, apiUrl, httpController);
	locationPoller.addObserver(Controller.this);

    }

    public Model getModel() {
	return model;
    }

    public void startLoginProcess() throws IOException {
	if (webserver.startListening()) {
	    LOGGER.info("start listening for redirect from local browser.");
	}
	else {
	    LOGGER.info("webserver is already listening.");
	}
    }

    
    private void querySolarSystemInformation(SolarSystem solarSystem) throws URISyntaxException, IOException{
        URI dataUri = new URI(apiUrl+"/solarsystems/"+solarSystem.getId()+"/");
        SolarSystemInformation solarSystemInfo = httpController.makeApiRequest(dataUri, SolarSystemInformation.class, false);
        solarSystem.setInformation(solarSystemInfo);
    }
    
    private void queryCharacterSheet() throws URISyntaxException, IOException {
	URI dataUri = new URI(apiUrl + "/characters/" + model.getCharacterInfo().getId() + "/");
	Character characterData = httpController.makeApiRequest(dataUri, Character.class, true);
	model.setCharacter(characterData);
	fireControllerEvent(characterData, ControllerEvent.EventType.GOT_CHARACTER);
    }

//    private 
    
    private void fireControllerEvent(Object payload, ControllerEvent.EventType type) {
	setChanged();
	notifyObservers(new ControllerEvent(payload, type));
    }

    private void queryCharacterInformation() throws URISyntaxException, IOException {
	URI verifyUri = new URI(authUrl + "/verify");
	CharacterInfo info = httpController.makeApiRequest(verifyUri, CharacterInfo.class, true);
	model.setCharacterInfo(info);
	fireControllerEvent(info, ControllerEvent.EventType.GOT_CHARACTER_INFO);
    }

    @Override
    public void update(Observable o, Object arg) {
	try {
	    if (o == webserver) {
		DummyWebServerEvent event = (DummyWebServerEvent) arg;
		switch (event.getType()) {
		    case ERROR:
			ControllerEvent<Exception> errorEvent = new ControllerEvent<>((Exception) event.getPayload(), ControllerEvent.EventType.ERROR);
			setChanged();
			notifyObservers(errorEvent);
			break;
		    case GOT_RESPONSE:
			ControllerEvent successEvent = new ControllerEvent(null, ControllerEvent.EventType.GOT_TOKEN);
			setChanged();
			notifyObservers(successEvent);
			AuthData authData = (AuthData) event.getPayload();
			httpController.setAccessCode(authData.getAccessCode());
			queryCharacterInformation();
			queryCharacterSheet();
			locationPoller.start();
			break;
		}
	    }
            else if(o == keyLogger){
                
            }
	    else if(o == locationPoller){
		LocationPollerEvent event = (LocationPollerEvent) arg;
                SolarSystem newLocation = event.getNewLocation();
                querySolarSystemInformation(newLocation);
		databaseController.save(newLocation);
                
		model.addSolarSystemToTravelRoute(newLocation);
		setChanged();
		notifyObservers(new ControllerEvent<>(newLocation, ControllerEvent.EventType.SOLAR_SYSTEM_CHANGE));
	    }
	}
	catch (SQLException | URISyntaxException | IOException ex) {
	    LOGGER.fatal("error while handling events.", ex);
	    ControllerEvent<Exception> event = new ControllerEvent<>(ex, ControllerEvent.EventType.ERROR);
	    setChanged();
	    notifyObservers(event);
	}
    }

    public void init() throws NativeHookException, ClassNotFoundException, SQLException, PropertyVetoException {
	//turn off debug logging from JNativeHook. great lib btw!
	java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
	logger.setLevel(Level.WARNING);
	GlobalScreen.registerNativeHook();

	keyLogger = new KeyLogger();

	GlobalScreen.addNativeKeyListener(keyLogger);
	LOGGER.info("initialized key logger");

	databaseController = new DatabaseController();
	databaseController.initDatabase();

	LOGGER.info("created database controller.");
    }

    public void shutDown() {
	try {
	    GlobalScreen.unregisterNativeHook();
	}
	catch (Exception ex) {
	    LOGGER.warn("error while shutting down key logger.", ex);
	}
	webserver.stopListening();
	locationPoller.stop();
	httpController.shutDown();
    }
}
