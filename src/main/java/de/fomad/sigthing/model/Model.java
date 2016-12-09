package de.fomad.sigthing.model;

import de.fomad.siglib.entities.CharacterInfo;
import de.fomad.siglib.entities.Pilot;
import de.fomad.siglib.entities.SolarSystem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author binary
 */
public class Model {

    private static final int MAX_TRAVEL_ROUTE_SIZE = 100;
    
    private CharacterInfo characterInfo;

    private Pilot character;
    
    private final List<SolarSystem> travelRoute;
    
    private SolarSystem currentSystem;

    public Model() {
	travelRoute = new ArrayList<>(MAX_TRAVEL_ROUTE_SIZE);	
    }

    public SolarSystem getCurrentSystem() {
        return currentSystem;
    }
    
    public void addSolarSystemToTravelRoute(SolarSystem solarSystem){
        currentSystem = solarSystem;
	travelRoute.add(0, solarSystem);
	if(travelRoute.size() > MAX_TRAVEL_ROUTE_SIZE){
	    travelRoute.remove(MAX_TRAVEL_ROUTE_SIZE);
	}
    }

    public List<SolarSystem> getTravelRoute() {
	return travelRoute;
    }

    public Pilot getCharacter() {
	return character;
    }

    public void setCharacter(Pilot character) {
	this.character = character;
    }

    public CharacterInfo getCharacterInfo() {
	return characterInfo;
    }

    public void setCharacterInfo(CharacterInfo characterInfo) {
	this.characterInfo = characterInfo;
    }
}
