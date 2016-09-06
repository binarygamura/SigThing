package de.fomad.sigthing.model;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author binary
 */
public class LocationTest extends BasicJsonParseTest
{
    
    @Test
    public void testPositiv1() throws URISyntaxException{
        String source = "{\n" +
        "  \"solarSystem\": {\n" +
        "    \"id_str\": \"30002782\",\n" +
        "    \"href\": \"https://crest-tq.eveonline.com/solarsystems/30002782/\",\n" +
        "    \"id\": 30002782,\n" +
        "    \"name\": \"Kamio\"\n" +
        "  }\n" +
        "}";
        
        Location location = gson.fromJson(source, Location.class);
        assertNotNull("solar system is null.", location.getSolarSystem());
        assertEquals("id is wrong.", 30002782, location.getSolarSystem().getId());
        assertEquals("href is wrong.", new URI("https://crest-tq.eveonline.com/solarsystems/30002782/"), location.getSolarSystem().getHref());
        assertEquals("solar system nameis wrong.", "Kamio", location.getSolarSystem().getName());
    }
}
