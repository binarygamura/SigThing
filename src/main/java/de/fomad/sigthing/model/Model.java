package de.fomad.sigthing.model;

/**
 *
 * @author binary
 */
public class Model
{
    private CharacterInfo characterInfo;

    private AuthData authData;
    
    private Character character;

    public Character getCharacter()
    {
        return character;
    }

    public void setCharacter(Character character)
    {
        this.character = character;
    }
    
    public CharacterInfo getCharacterInfo()
    {
        return characterInfo;
    }

    public void setCharacterInfo(CharacterInfo characterInfo)
    {
        this.characterInfo = characterInfo;
    }

    public AuthData getAuthData()
    {
        return authData;
    }

    public void setAuthData(AuthData authData)
    {
        this.authData = authData;
    }
}
