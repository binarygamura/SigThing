package de.fomad.sigthing.model;

import java.util.Objects;

/**
 *
 * @author binary gamura
 */
public class Signature {

    private String signature;

    private String scanGroup;
    
    private String name;

    private int id;

    private float signalStrength;

    private int solarSystemId;

    private String addedBy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
	return signature;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
    
    public void setSignature(String signature) {
	this.signature = signature;
    }

    public String getScanGroup() {
	return scanGroup;
    }

    public void setScanGroup(String scanGroup) {
	this.scanGroup = scanGroup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public float getSignalStrength() {
	return signalStrength;
    }

    public void setSignalStrength(float signalStrength) {
	this.signalStrength = signalStrength;
    }

    public int getSolarSystemId() {
	return solarSystemId;
    }

    public void setSolarSystemId(int solarSystemId) {
	this.solarSystemId = solarSystemId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Signature other = (Signature) obj;
        if (this.solarSystemId != other.solarSystemId) {
            return false;
        }
        if (!Objects.equals(this.signature, other.signature)) {
            return false;
        }
        return true;
    }
    
    
    
    

}
