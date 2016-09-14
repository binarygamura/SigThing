package de.fomad.sigthing.model;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author binary
 */
public class SolarSystem extends Reference {

    private int id;

    private String name;
    
    private SolarSystemInformation information;

    private final Date added;
    
    public SolarSystem(){
        added = new Date();
    }

    public Date getAdded() {
        return new Date(added.getTime());
    }
    
    public SolarSystemInformation getInformation() {
        return information;
    }

    public void setInformation(SolarSystemInformation information) {
        this.information = information;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
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
	final SolarSystem other = (SolarSystem) obj;
	if (this.id != other.id) {
	    return false;
	}
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	return true;
    }
}
