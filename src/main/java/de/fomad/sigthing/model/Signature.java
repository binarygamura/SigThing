package de.fomad.sigthing.model;

/**
 *
 * @author binary gamura
 */
public class Signature {

    private String signature;

    private String scanGroup;

    private String id;

    private float signalStrength;

    private int solarSystemId;

    private String addedBy;

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

    public String getId() {
	return id;
    }

    public void setId(String id) {
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

}
