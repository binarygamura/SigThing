package de.fomad.sigthing.model;


import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author boreas
 */
public class SolarSystemTest extends BasicJsonParseTest {
    
    @Test
    public void testPositiv1(){
        String input = "{\n" +
        "  \"stats\": {\n" +
        "    \"href\": \"https://crest-tq.eveonline.com/solarsystems/30002290/stats/\"\n" +
        "  },\n" +
        "  \"planets\": [\n" +
        "    {\n" +
        "      \"href\": \"https://crest-tq.eveonline.com/planets/40145807/\",\n" +
        "      \"moons\": [\n" +
        "        {\n" +
        "          \"href\": \"https://crest-tq.eveonline.com/moons/40145808/\"\n" +
        "        }\n" +
        "      ]\n" +
        "    },\n" +
        "    {\n" +
        "      \"href\": \"https://crest-tq.eveonline.com/planets/40145809/\",\n" +
        "      \"moons\": [\n" +
        "        {\n" +
        "          \"href\": \"https://crest-tq.eveonline.com/moons/40145810/\"\n" +
        "        },\n" +
        "        {\n" +
        "          \"href\": \"https://crest-tq.eveonline.com/moons/40145808/\"\n" +
        "        }\n" +
        "      ]\n" +
        "    }\n" +
        "  ],\n" +
        "  \"stargates\": [\n" +
        "    {\n" +
        "      \"href\": \"https://crest-tq.eveonline.com/stargates/50000569/\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"href\": \"https://crest-tq.eveonline.com/stargates/50000570/\"\n" +
        "    }\n" +
        "  ],\n" +
        "  \"securityClass\": \"K4\",\n" +
        "  \"href\": \"https://crest-tq.eveonline.com/solarsystems/30002290/\",\n" +
        "  \"id_str\": \"30002290\",\n" +
        "  \"securityStatus\": -0.18677988648414612,\n" +
        "  \"position\": {\n" +
        "    \"y\": 52867918353394540,\n" +
        "    \"x\": 149868501775364700,\n" +
        "    \"z\": 87927918423435500\n" +
        "  },\n" +
        "  \"constellation\": {\n" +
        "    \"href\": \"https://crest-tq.eveonline.com/constellations/20000336/\",\n" +
        "    \"id\": 20000336,\n" +
        "    \"id_str\": \"20000336\"\n" +
        "  },\n" +
        "  \"id\": 30002290,\n" +
        "  \"name\": \"PXE-RG\"\n" +
        "}";
        
        SolarSystemInformation information = gson.fromJson(input, SolarSystemInformation.class);
        
        assertNotNull(information);
        
        assertEquals("solar system name is wrong.", "PXE-RG", information.getName());        
        assertEquals("security class is wrong.", "K4", information.getSecurityClass());        
        assertEquals("solar system id is wrong", 30002290, information.getId());
        
        assertNotNull(information.getPosition());
        assertEquals("x pos is wrong", 149868501775364700L, information.getPosition().getX());
        assertEquals("y pos is wrong", 52867918353394540L, information.getPosition().getY());
        assertEquals("z pos is wrong", 87927918423435500L, information.getPosition().getZ());
        
        assertNotNull(information.getStargates());
        assertEquals("number of stargates is wrong.", 2, information.getStargates().length);
        
        assertNotNull(information.getPlanets());
        assertEquals("number of planets is wrong.", 2, information.getPlanets().length);
    }
}
