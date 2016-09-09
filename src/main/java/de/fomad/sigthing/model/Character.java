package de.fomad.sigthing.model;

import com.google.gson.annotations.SerializedName;
import java.net.URI;

/**
 * {
 * "race": { "href": "https://crest-tq.eveonline.com/races/4/", "id": 4,
 * "id_str": "4" }, "fittings": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/fittings/" },
 * "description": "Demo description. HI MOM!", "bloodLine": { "href":
 * "https://crest-tq.eveonline.com/bloodlines/13/", "id": 13, "id_str": "13" },
 * "gender_str": "1", "corporation": { "name": "Resilience.", "isNPC": false,
 * "href": "https://crest-tq.eveonline.com/corporations/98019139/", "id_str":
 * "98019139", "logo": { "32x32": { "href":
 * "http://imageserver.eveonline.com/Corporation/98019139_32.png" }, "64x64": {
 * "href": "http://imageserver.eveonline.com/Corporation/98019139_64.png" },
 * "128x128": { "href":
 * "http://imageserver.eveonline.com/Corporation/98019139_128.png" }, "256x256":
 * { "href": "http://imageserver.eveonline.com/Corporation/98019139_256.png" }
 * }, "id": 98019139 }, "contacts": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/contacts/" },
 * "opportunities": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/opportunities/" },
 * "id_str": "2047918291", "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/", "ui": {
 * "setWaypoints": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/ui/autopilot/waypoints/"
 * }, "showContract": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/ui/openwindow/contract/"
 * }, "showMarketDetails": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/ui/openwindow/marketdetails/"
 * } }, "location": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/location/" }, "gender":
 * 1, "portrait": { "32x32": { "href":
 * "http://imageserver.eveonline.com/Character/2047918291_32.jpg" }, "64x64": {
 * "href": "http://imageserver.eveonline.com/Character/2047918291_64.jpg" },
 * "128x128": { "href":
 * "http://imageserver.eveonline.com/Character/2047918291_128.jpg" }, "256x256":
 * { "href": "http://imageserver.eveonline.com/Character/2047918291_256.jpg" }
 * }, "id": 2047918291, "loyaltyPoints": { "href":
 * "https://crest-tq.eveonline.com/characters/2047918291/loyaltypoints/" },
 * "name": "Blacksmoke16" }
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
    }
}
