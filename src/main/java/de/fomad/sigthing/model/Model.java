package de.fomad.sigthing.model;

/**
 *
 * @author binary
 */
public class Model {

    private CharacterInfo characterInfo;

    private Character character;

    public Model() {

    }

    public Character getCharacter() {
	return character;
    }

    public void setCharacter(Character character) {
	this.character = character;
    }

    public CharacterInfo getCharacterInfo() {
	return characterInfo;
    }

    public void setCharacterInfo(CharacterInfo characterInfo) {
	this.characterInfo = characterInfo;
    }
}
