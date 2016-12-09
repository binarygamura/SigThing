package de.fomad.sigthing.model;

import de.fomad.siglib.entities.Reference;

/**
 *
 * @author binary
 */
public class WaypointRequest extends Reference {
    
    private boolean clearOtherWaypoints;
    
    private boolean first;
    
    private SolarSystem solarSystem;

    public boolean isClearOtherWaypoints() {
        return clearOtherWaypoints;
    }

    public void setClearOtherWaypoints(boolean clearOtherWaypoints) {
        this.clearOtherWaypoints = clearOtherWaypoints;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public SolarSystem getSolarSystem() {
        return solarSystem;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }
    
    public static class SolarSystem extends Reference {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        
        
    }
}
