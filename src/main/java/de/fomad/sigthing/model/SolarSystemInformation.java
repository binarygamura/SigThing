package de.fomad.sigthing.model;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.stats);
        hash = 83 * hash + Arrays.deepHashCode(this.stargates);
        hash = 83 * hash + Arrays.deepHashCode(this.planets);
        hash = 83 * hash + Objects.hashCode(this.securityClass);
        hash = 83 * hash + Float.floatToIntBits(this.securityStatus);
        hash = 83 * hash + Objects.hashCode(this.position);
        hash = 83 * hash + this.id;
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.constellation);
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
        final SolarSystemInformation other = (SolarSystemInformation) obj;
        if (Float.floatToIntBits(this.securityStatus) != Float.floatToIntBits(other.securityStatus)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.securityClass, other.securityClass)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.stats, other.stats)) {
            return false;
        }
        if (!Arrays.deepEquals(this.stargates, other.stargates)) {
            return false;
        }
        if (!Arrays.deepEquals(this.planets, other.planets)) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (!Objects.equals(this.constellation, other.constellation)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
