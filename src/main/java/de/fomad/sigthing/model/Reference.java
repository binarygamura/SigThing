package de.fomad.sigthing.model;

import java.net.URI;
import java.util.Objects;

/**
 *
 * @author binary
 */
public class Reference {

    private URI href;

    public URI getHref() {
	return href;
    }

    public void setHref(URI href) {
	this.href = href;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.href);
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
        final Reference other = (Reference) obj;
        if (!Objects.equals(this.href, other.href)) {
            return false;
        }
        return true;
    }
    
    

}
