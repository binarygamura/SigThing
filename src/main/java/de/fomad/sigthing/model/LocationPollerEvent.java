package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class LocationPollerEvent {
    
    private final Type type;
    
    public enum Type {
        LOCATION,
        OFFLINE,
        SERVICE_UNAVAILABLE
    }
    private SolarSystem newLocation;

    public LocationPollerEvent(SolarSystem newLocation, Type type) {
        this.type = type;
	this.newLocation = newLocation;
    }

    public Type getType() {
        return type;
    }
    
    
    
    public SolarSystem getNewLocation() {
	return newLocation;
    }

    public void setNewLocation(SolarSystem newLocation) {
	this.newLocation = newLocation;
    }
}
