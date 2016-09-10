package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class LocationPollerEvent {
    private SolarSystem newLocation;

    public LocationPollerEvent(SolarSystem newLocation) {
	this.newLocation = newLocation;
    }
    
    public SolarSystem getNewLocation() {
	return newLocation;
    }

    public void setNewLocation(SolarSystem newLocation) {
	this.newLocation = newLocation;
    }
}
