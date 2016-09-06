package de.fomad.sigthing.model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 */
public class CharacterInfoTest extends BasicJsonParseTest
{
    @Test
    public void testPositiv1(){
        String input ="{\n" +
            "    \"CharacterID\": 273042051,\n" +
            "    \"CharacterName\": \"CCP illurkall\",\n" +
            "    \"ExpiresOn\": \"2014-05-23T15:01:15.182864\",\n" +
            "    \"Scopes\": \" \",\n" +
            "    \"TokenType\": \"Character\",\n" +
            "    \"CharacterOwnerHash\": \"XM4DDSDASDSAFoY=\"\n" +
            "}";
        
        CharacterInfo characterInfo = gson.fromJson(input, CharacterInfo.class);
        
        assertEquals("error while parsing character id.", characterInfo.getId(), 273042051);
        assertEquals("error while parsing character name.", characterInfo.getName(), "CCP illurkall");
        assertEquals("error while parsing token type.", characterInfo.getTokenType(), "Character");
        assertEquals("error while parsing owner hash.", characterInfo.getOwnerHash(), "XM4DDSDASDSAFoY=");
        assertEquals("error while parsing date", dateFormat.format(characterInfo.getExpires()), "2014-05-23 15:01:15");
    }
}
