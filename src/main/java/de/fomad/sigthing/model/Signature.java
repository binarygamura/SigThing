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

    private String solarSystemName;

    private int solarSystemId;

    public String getSignature() {
	return signature;
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

    public String getSolarSystemName() {
	return solarSystemName;
    }

    public void setSolarSystemName(String solarSystemName) {
	this.solarSystemName = solarSystemName;
    }

    public int getSolarSystemId() {
	return solarSystemId;
    }

    public void setSolarSystemId(int solarSystemId) {
	this.solarSystemId = solarSystemId;
    }

}
