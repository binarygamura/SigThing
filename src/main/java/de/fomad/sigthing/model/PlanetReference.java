package de.fomad.sigthing.model;

import java.util.Arrays;

/**
 *
 * @author boreas
 */
public class PlanetReference extends Reference {
    private Reference[] moons;

    public Reference[] getMoons() {
        return moons;
    }

    public void setMoons(Reference[] moons) {
        this.moons = moons;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Arrays.deepHashCode(this.moons);
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
        final PlanetReference other = (PlanetReference) obj;
        if (!Arrays.deepEquals(this.moons, other.moons)) {
            return false;
        }
        return true;
    }
    
    
}
