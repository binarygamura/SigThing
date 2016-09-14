package de.fomad.sigthing.model;

/**
 *
 * @author boreas
 */
public class Constellation extends Reference {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
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
        final Constellation other = (Constellation) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
