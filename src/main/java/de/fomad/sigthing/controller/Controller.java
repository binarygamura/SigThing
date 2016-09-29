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
import de.fomad.sigthing.model.KeyLoggerEvent;
import de.fomad.sigthing.model.LocationPollerEvent;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import de.fomad.sigthing.model.SolarSystemInformation;
import de.fomad.sigthing.model.WaypointRequest;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

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

    public DatabaseController getDatabaseController() {
        return databaseController;
    }

    public Model getModel() {
        return model;
    }

    public void startLoginProcess() throws IOException {
        if (webserver.startListening()) {
            LOGGER.info("start listening for redirect from local browser.");
        } else {
            LOGGER.info("webserver is already listening.");
        }
    }

    public List<Signature> getSignaturesForCurrentSystem() throws SQLException {
        return databaseController.querySignaturesFor(model.getCurrentSystem().getId());
    }

    private List<Signature> parseSignatures(String data, int solarSystemId) {
        Signature signature;
        String strengthString;
        String[] columns;
        List<Signature> signatures = null;
        System.out.println(data);
        String[] lines = data.split("\\n");
        if (lines.length > 0) {
            for (String line : lines) {
                columns = line.split("\\t");
                if (columns.length == 6 && columns[1].equalsIgnoreCase("Cosmic Signature")) {
                    if (signatures == null) {
                        signatures = new ArrayList<>(lines.length);
                    }
                    strengthString = columns[4].substring(0, columns[4].length() - 1).replace(',', '.');

                    signature = new Signature();
                    signature.setSignature(columns[0].trim());
                    signature.setScanGroup(columns[2].trim());
                    signature.setName(columns[3].trim());
                    signature.setSignalStrength(Float.valueOf(strengthString));
                    signature.setSolarSystemId(solarSystemId);
                    signatures.add(signature);
                }
            }
        }
        return signatures;
    }

    private void querySolarSystemInformation(SolarSystem solarSystem) throws URISyntaxException, IOException {
        if(solarSystem != null){
            URI dataUri = new URI(apiUrl + "/solarsystems/" + solarSystem.getId() + "/");
            SolarSystemInformation solarSystemInfo = httpController.makeApiGetRequest(dataUri, SolarSystemInformation.class, false);
            solarSystem.setInformation(solarSystemInfo);
        }
    }

    private void queryCharacterSheet() throws URISyntaxException, IOException {
        URI dataUri = new URI(apiUrl + "/characters/" + model.getCharacterInfo().getId() + "/");
        Character characterData = httpController.makeApiGetRequest(dataUri, Character.class, true);
        model.setCharacter(characterData);
        fireControllerEvent(characterData, ControllerEvent.EventType.GOT_CHARACTER);
    }

    private void fireControllerEvent(Object payload, ControllerEvent.EventType type) {
        setChanged();
        notifyObservers(new ControllerEvent(payload, type));
    }

    private void queryCharacterInformation() throws URISyntaxException, IOException {
        URI verifyUri = new URI(authUrl + "/verify");
        CharacterInfo info = httpController.makeApiGetRequest(verifyUri, CharacterInfo.class, true);
        model.setCharacterInfo(info);
        fireControllerEvent(info, ControllerEvent.EventType.GOT_CHARACTER_INFO);
    }

    public void addWaypointTo(SolarSystem solarSystem) throws URISyntaxException, IOException {
        URI waypointUri = new URI(apiUrl + "/characters/" + model.getCharacterInfo().getId() + "/ui/autopilot/waypoints/");
        URI destinationUri = new URI(apiUrl + "/solarsystems/" + solarSystem.getInformation().getId() + "/");
        WaypointRequest request = new WaypointRequest();
        WaypointRequest.SolarSystem internalSystem = new WaypointRequest.SolarSystem();
        internalSystem.setId(solarSystem.getId());
        internalSystem.setHref(destinationUri);
        request.setClearOtherWaypoints(true);
        request.setFirst(false);
        request.setSolarSystem(internalSystem);
        httpController.makeApiPostRequest(waypointUri, Object.class, true, request);
    }

    public void addSignaturesRaw(String input, SolarSystem currentSystem) throws SQLException {
        input = input.trim();
        if(!input.isEmpty() && currentSystem != null){
            List<Signature> parsedSignatures = parseSignatures(input, currentSystem.getId());
            if (parsedSignatures != null) {
                databaseController.mergeSignatures(parsedSignatures, model.getCharacter(), currentSystem.getId());
                ControllerEvent locationEvent = new ControllerEvent(currentSystem, ControllerEvent.EventType.NEW_SIGNATURES);
                setChanged();
                notifyObservers(locationEvent);
            }
        }
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
            } else if (o == keyLogger) {
                KeyLoggerEvent event = (KeyLoggerEvent) arg;
                SolarSystem currentSystem = model.getCurrentSystem();
//                if (currentSystem != null) {
                    addSignaturesRaw(event.getInput(), currentSystem);
//                    List<Signature> parsedSignatures = parseSignatures(event.getInput(), currentSystem.getId());
//                    if (parsedSignatures != null) {
//                        databaseController.mergeSignatures(parsedSignatures, model.getCharacter(), currentSystem.getId());
//                        ControllerEvent locationEvent = new ControllerEvent(currentSystem, ControllerEvent.EventType.NEW_SIGNATURES);
//                        setChanged();
//                        notifyObservers(locationEvent);
//                    }
//                }
            } else if (o == locationPoller) {
                LocationPollerEvent event = (LocationPollerEvent) arg;
                switch (event.getType()) {
                    case LOCATION:
                        SolarSystem newLocation = event.getNewLocation();
                        querySolarSystemInformation(newLocation);
                        databaseController.save(newLocation, model.getCharacter());
                        model.addSolarSystemToTravelRoute(newLocation);
                        setChanged();
                        notifyObservers(new ControllerEvent<>(newLocation, ControllerEvent.EventType.SOLAR_SYSTEM_CHANGE));
                        break;
                    case OFFLINE:
                        setChanged();
                        notifyObservers(new ControllerEvent<>(null, ControllerEvent.EventType.OFFLINE));
                        break;
                    case SERVICE_UNAVAILABLE:
                        setChanged();
                        notifyObservers(new ControllerEvent<>(null, ControllerEvent.EventType.DOWNTIME));
                        break;
                }

            }
        } catch (SQLException | URISyntaxException | IOException ex) {
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

        try {
            GlobalScreen.registerNativeHook();

            keyLogger = new KeyLogger();
            keyLogger.addObserver(this);

            GlobalScreen.addNativeKeyListener(keyLogger);

            LOGGER.info("initialized key logger");
        } catch (UnsatisfiedLinkError ex) {
            LOGGER.warn("unable to initalize key logger.");
        }

        databaseController = new DatabaseController();
        databaseController.initDatabase();

        LOGGER.info("created database controller.");
    }

    public void shutDown() {
        try {
            GlobalScreen.unregisterNativeHook();
            LOGGER.info("unregisterted key logger.");
        } catch (UnsatisfiedLinkError | Exception ex) {
            LOGGER.warn("error while shutting down key logger.", ex);
        }
        webserver.stopListening();
        locationPoller.stop();
        httpController.shutDown();
    }
}
