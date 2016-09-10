package de.fomad.sigthing.model;

import java.util.Objects;

/**
 *
 * @author binary
 */
public class Location {

    private SolarSystem solarSystem;

    public SolarSystem getSolarSystem() {
	return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
	this.solarSystem = solarSystem;
    }

    @Override
    public int hashCode() {
	int hash = 7;
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Location other = (Location) obj;
	if (!Objects.equals(this.solarSystem, other.solarSystem)) {
	    return false;
	}
	return true;
    }
    
    
}
