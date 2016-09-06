package de.fomad.sigthing.model;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author binary
 */
public class CharacterTest extends BasicJsonParseTest
{
 
    @Test
    public void testPositiv1() throws URISyntaxException{
        String source = "{\n" +
                "    \"race\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/races/4/\",\n" +
                "    \"id\": 4,\n" +
                "    \"id_str\": \"4\"\n" +
                "  },\n" +
                "  \"fittings\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/fittings/\"\n" +
                "  },\n" +
                "  \"description\": \"Demo description. HI MOM!\",\n" +
                "  \"bloodLine\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/bloodlines/13/\",\n" +
                "    \"id\": 13,\n" +
                "    \"id_str\": \"13\"\n" +
                "  },\n" +
                "  \"gender_str\": \"1\",\n" +
                "  \"corporation\": {\n" +
                "    \"name\": \"Resilience.\",\n" +
                "    \"isNPC\": false,\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/corporations/98019139/\",\n" +
                "    \"id_str\": \"98019139\",\n" +
                "    \"logo\": {\n" +
                "      \"32x32\": {\n" +
                "        \"href\": \"http://imageserver.eveonline.com/Corporation/98019139_32.png\"\n" +
                "      },\n" +
                "      \"64x64\": {\n" +
                "        \"href\": \"http://imageserver.eveonline.com/Corporation/98019139_64.png\"\n" +
                "      },\n" +
                "      \"128x128\": {\n" +
                "        \"href\": \"http://imageserver.eveonline.com/Corporation/98019139_128.png\"\n" +
                "      },\n" +
                "      \"256x256\": {\n" +
                "        \"href\": \"http://imageserver.eveonline.com/Corporation/98019139_256.png\"\n" +
                "      }\n" +
                "    },\n" +
                "    \"id\": 98019139\n" +
                "  },\n" +
                "  \"contacts\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/contacts/\"\n" +
                "  },\n" +
                "  \"opportunities\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/opportunities/\"\n" +
                "  },\n" +
                "  \"id_str\": \"2047918291\",\n" +
                "  \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/\",\n" +
                "  \"ui\": {\n" +
                "    \"setWaypoints\": {\n" +
                "      \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/ui/autopilot/waypoints/\"\n" +
                "    },\n" +
                "    \"showContract\": {\n" +
                "      \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/ui/openwindow/contract/\"\n" +
                "    },\n" +
                "    \"showMarketDetails\": {\n" +
                "      \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/ui/openwindow/marketdetails/\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"location\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/location/\"\n" +
                "  },\n" +
                "  \"gender\": 1,\n" +
                "  \"portrait\": {\n" +
                "    \"32x32\": {\n" +
                "      \"href\": \"http://imageserver.eveonline.com/Character/2047918291_32.jpg\"\n" +
                "    },\n" +
                "    \"64x64\": {\n" +
                "      \"href\": \"http://imageserver.eveonline.com/Character/2047918291_64.jpg\"\n" +
                "    },\n" +
                "    \"128x128\": {\n" +
                "      \"href\": \"http://imageserver.eveonline.com/Character/2047918291_128.jpg\"\n" +
                "    },\n" +
                "    \"256x256\": {\n" +
                "      \"href\": \"http://imageserver.eveonline.com/Character/2047918291_256.jpg\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"id\": 2047918291,\n" +
                "  \"loyaltyPoints\": {\n" +
                "    \"href\": \"https://crest-tq.eveonline.com/characters/2047918291/loyaltypoints/\"\n" +
                "  },\n" +
                "  \"name\": \"Blacksmoke16\"\n" +
                "}";
        Character character = gson.fromJson(source, Character.class);
        
        assertNotNull(character);
        assertEquals("name is wrong.", "Blacksmoke16", character.getName());
        assertNotNull("lp location is null.", character.getLoyaltyPoints());
        assertEquals("loyaltyPoints location href is wrong.", new URI("https://crest-tq.eveonline.com/characters/2047918291/loyaltypoints/"), character.getLoyaltyPoints().getHref());
        assertNotNull("location is null.", character.getLocation());
        assertEquals("location href is wrong.", new URI("https://crest-tq.eveonline.com/characters/2047918291/location/"), character.getLocation().getHref());
        assertEquals("id is wront.", 2047918291, character.getId());
        assertEquals("gender is wrong.", 1, character.getGender());
        
        assertNotNull("portraits are null.", character.getPortrait());
        assertEquals("small portrait is wrong.", new URI("http://imageserver.eveonline.com/Character/2047918291_32.jpg"), character.getPortrait().getSmall().getHref());
        assertEquals("medium portrait is wrong.", new URI("http://imageserver.eveonline.com/Character/2047918291_64.jpg"), character.getPortrait().getMedium().getHref());
        assertEquals("large portrait is wrong.", new URI("http://imageserver.eveonline.com/Character/2047918291_128.jpg"), character.getPortrait().getLarge().getHref());
        assertEquals("huge portrait is wrong.", new URI("http://imageserver.eveonline.com/Character/2047918291_256.jpg"), character.getPortrait().getHuge().getHref());
        
        assertNotNull("race is null.", character.getRace());
        assertEquals("race href is wrong.", new URI("https://crest-tq.eveonline.com/races/4/"), character.getRace().getHref());
        assertEquals("race id is wrong.", 4, character.getRace().getId());
        
        assertNotNull("corporation is null.", character.getCorporation());
        assertEquals("corp name is wrong.", "Resilience.", character.getCorporation().getName());
        assertEquals("corp npc state is wrong.", false, character.getCorporation().isIsNpc());
        
        assertNotNull("corp logos are null.", character.getCorporation().getLogo());
        assertEquals("small corp logo is wrong", new URI("http://imageserver.eveonline.com/Corporation/98019139_32.png"), character.getCorporation().getLogo().getSmall().getHref());
        assertEquals("medium corp logo is wrong", new URI("http://imageserver.eveonline.com/Corporation/98019139_64.png"), character.getCorporation().getLogo().getMedium().getHref());
        assertEquals("large corp logo is wrong", new URI("http://imageserver.eveonline.com/Corporation/98019139_128.png"), character.getCorporation().getLogo().getLarge().getHref());
        assertEquals("huge corp logo is wrong", new URI("http://imageserver.eveonline.com/Corporation/98019139_256.png"), character.getCorporation().getLogo().getHuge().getHref());
        assertEquals("corp id is wrong.", 98019139, character.getCorporation().getId());
        
    }
}
