package de.fomad.sigthing.view.icons;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class IconCache {
    
    private static final Logger LOGGER = LogManager.getLogger(IconCache.class);
    
    private final Map<IconId, Image> cache = new HashMap<>();
    
    public ImageIcon getImageIcon(IconId id){
	return new ImageIcon(getIcon(id));
    }
    
    public Image getIcon(IconId id){
	try{
	    if(!cache.containsKey(id)){
		BufferedImage image = ImageIO.read(IconCache.class.getResourceAsStream(id.getFile()));
		if(image != null){
		    cache.put(id, image);
		}
		else {
		    LOGGER.warn("loading image \""+id.getFile()+"\" resulted in null value.");
		}
	    }
	}
	catch(IOException ex){
	    LOGGER.warn("unable to load image \""+id.getFile()+"\".", ex);
	}
	return cache.get(id);
    }
    
    public enum IconId{
        DOTLAN("dotlan.png"),
        
        ADD("add_16.png"),
        
        ZKILLBOARD_ICON("zkillboard.png"),
        
	CANCEL_ICON("cross_16.png"),
	
        ROUTE("32_bit_16.png"),
        
	INFO_ICON("information_16.png"),
	
        CONFIGURATION("server_configuration_16.png"),
        
        COMMENT("pencil_16.png"),
        
	APP_ICON("gingerbread_man_chocolate_16.png"),
        
        LOGIN_ICON("EVE_SSO_Login_Buttons_Large_White.png");
	
	private final String file;
	
	private IconId(String file){
	    this.file = file;
	}

	private String getFile() {
	    return file;
	}
    }
}
