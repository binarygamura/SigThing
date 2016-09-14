package de.fomad.sigthing.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 *
 * @author binary
 */
public class Character extends Reference {

    private String name;

    private int id;

    private int gender;

    private Race race;

    private Reference loyaltyPoints;

    private Reference location;

    private Reference fittings;

    private Reference contacts;

    private Reference opportunities;

    private Portraits portrait;

    private String description;

    private Corporation corporation;

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Reference getFittings() {
        return fittings;
    }

    public void setFittings(Reference fittings) {
        this.fittings = fittings;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    public Portraits getPortrait() {
        return portrait;
    }

    public void setPortrait(Portraits portrait) {
        this.portrait = portrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Reference getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Reference loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Reference getLocation() {
        return location;
    }

    public void setLocation(Reference location) {
        this.location = location;
    }

    public Reference getContacts() {
        return contacts;
    }

    public void setContacts(Reference contacts) {
        this.contacts = contacts;
    }

    public Reference getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(Reference opportunities) {
        this.opportunities = opportunities;
    }

    public static class Race extends Reference {

        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Corporation extends Reference {

        private String name;

        private boolean isNpc;

        private Portraits logo;

        private int id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIsNpc() {
            return isNpc;
        }

        public void setIsNpc(boolean isNpc) {
            this.isNpc = isNpc;
        }

        public Portraits getLogo() {
            return logo;
        }

        public void setLogo(Portraits logo) {
            this.logo = logo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.name);
            hash = 29 * hash + (this.isNpc ? 1 : 0);
            hash = 29 * hash + Objects.hashCode(this.logo);
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
            final Corporation other = (Corporation) obj;
            if (this.isNpc != other.isNpc) {
                return false;
            }
            if (this.id != other.id) {
                return false;
            }
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.logo, other.logo)) {
                return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.name);
        hash = 59 * hash + this.id;
        hash = 59 * hash + this.gender;
        hash = 59 * hash + Objects.hashCode(this.race);
        hash = 59 * hash + Objects.hashCode(this.loyaltyPoints);
        hash = 59 * hash + Objects.hashCode(this.location);
        hash = 59 * hash + Objects.hashCode(this.fittings);
        hash = 59 * hash + Objects.hashCode(this.contacts);
        hash = 59 * hash + Objects.hashCode(this.opportunities);
        hash = 59 * hash + Objects.hashCode(this.portrait);
        hash = 59 * hash + Objects.hashCode(this.description);
        hash = 59 * hash + Objects.hashCode(this.corporation);
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
        final Character other = (Character) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.gender != other.gender) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.race, other.race)) {
            return false;
        }
        if (!Objects.equals(this.loyaltyPoints, other.loyaltyPoints)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.fittings, other.fittings)) {
            return false;
        }
        if (!Objects.equals(this.contacts, other.contacts)) {
            return false;
        }
        if (!Objects.equals(this.opportunities, other.opportunities)) {
            return false;
        }
        if (!Objects.equals(this.portrait, other.portrait)) {
            return false;
        }
        if (!Objects.equals(this.corporation, other.corporation)) {
            return false;
        }
        return true;
    }

    public static class Portraits {

        @SerializedName("32x32")
        private Reference small;

        @SerializedName("64x64")
        private Reference medium;

        @SerializedName("128x128")
        private Reference large;

        @SerializedName("256x256")
        private Reference huge;

        public Reference getSmall() {
            return small;
        }

        public void setSmall(Reference small) {
            this.small = small;
        }

        public Reference getMedium() {
            return medium;
        }

        public void setMedium(Reference medium) {
            this.medium = medium;
        }

        public Reference getLarge() {
            return large;
        }

        public void setLarge(Reference large) {
            this.large = large;
        }

        public Reference getHuge() {
            return huge;
        }

        public void setHuge(Reference huge) {
            this.huge = huge;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.small);
            hash = 79 * hash + Objects.hashCode(this.medium);
            hash = 79 * hash + Objects.hashCode(this.large);
            hash = 79 * hash + Objects.hashCode(this.huge);
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
            final Portraits other = (Portraits) obj;
            if (!Objects.equals(this.small, other.small)) {
                return false;
            }
            if (!Objects.equals(this.medium, other.medium)) {
                return false;
            }
            if (!Objects.equals(this.large, other.large)) {
                return false;
            }
            if (!Objects.equals(this.huge, other.huge)) {
                return false;
            }
            return true;
        }
        
        
    }
}
