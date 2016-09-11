package de.fomad.sigthing.model;

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
}
