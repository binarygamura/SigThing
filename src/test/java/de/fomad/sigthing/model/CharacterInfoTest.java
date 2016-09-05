package de.fomad.sigthing.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 
 */
public class CharacterInfoTest
{
    private static Gson gson;
    
    private static DateFormat dateFormat;
    
    @BeforeClass
    public static void init(){
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
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
