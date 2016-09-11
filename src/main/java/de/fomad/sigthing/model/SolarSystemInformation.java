package de.fomad.sigthing.model;

/**
 *
 * @author boreas
 */
public class SolarSystemInformation extends Reference {
    private Reference stats;
    
    private Reference[] stargates;
    
    private PlanetReference[] planets;
    
    private String securityClass;
    
    private float securityStatus;
    
    private Position position;
    
    private int id;
    
    private String name;
    
    private Constellation constellation;

    public Reference getStats() {
        return stats;
    }

    public void setStats(Reference stats) {
        this.stats = stats;
    }

    public Reference[] getStargates() {
        return stargates;
    }

    public void setStargates(Reference[] stargates) {
        this.stargates = stargates;
    }

    public PlanetReference[] getPlanets() {
        return planets;
    }

    public void setPlanets(PlanetReference[] planets) {
        this.planets = planets;
    }

    public String getSecurityClass() {
        return securityClass;
    }

    public void setSecurityClass(String securityClass) {
        this.securityClass = securityClass;
    }

    public float getSecurityStatus() {
        return securityStatus;
    }

    public void setSecurityStatus(float securityStatus) {
        this.securityStatus = securityStatus;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public Constellation getConstellation() {
        return constellation;
    }

    public void setConstellation(Constellation constellation) {
        this.constellation = constellation;
    }
    
    
    
    
}
