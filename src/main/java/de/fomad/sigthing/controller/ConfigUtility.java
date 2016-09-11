package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.ApplicationConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author boreas
 */
public class ConfigUtility {
    
    private static final String DEFAULT_CONFIG_FILE = System.getProperty("user.home")+"/sigtrack_config.properties";
    
    private static class ConfigKeys {
        static final String PLAY_SOUNDS = "play_sounds";
        static final String ALWAYS_ON_TOP = "always_on_top";
        static final String VOLUME = "volume";
    }
    
    public static void readConfig(ApplicationConfiguration configuration) throws IOException{
        File configFile = new File(DEFAULT_CONFIG_FILE);
        if(configFile.exists()){
            try(FileInputStream stream = new FileInputStream(configFile)){
                Properties properties = new Properties();
                properties.load(stream);
                
                if(properties.containsKey(ConfigKeys.PLAY_SOUNDS)){
                    configuration.setPlaySounds(Boolean.valueOf(properties.getProperty(ConfigKeys.PLAY_SOUNDS)));
                }
                if(properties.containsKey(ConfigKeys.ALWAYS_ON_TOP)){
                    configuration.setAlwaysOnTop(Boolean.valueOf(properties.getProperty(ConfigKeys.ALWAYS_ON_TOP)));
                }
                if(properties.containsKey(ConfigKeys.VOLUME)){
                    configuration.setVolume(Integer.valueOf(properties.getProperty(ConfigKeys.VOLUME)));
                }
            }
        }
    }
    
    public static void writeConfig(ApplicationConfiguration configuration) throws IOException{
        
        File configFile = new File(DEFAULT_CONFIG_FILE);
        if(!configFile.exists() && !configFile.createNewFile()){
            throw new IOException("unable to create empty configuration file \""+DEFAULT_CONFIG_FILE+"\".");
        }
        try(FileOutputStream stream = new FileOutputStream(DEFAULT_CONFIG_FILE)){
            
            Properties properties = new Properties();
            properties.setProperty(ConfigKeys.PLAY_SOUNDS, String.valueOf(configuration.isPlaySounds()));
            properties.setProperty(ConfigKeys.ALWAYS_ON_TOP, String.valueOf(configuration.isAlwaysOnTop()));
            properties.setProperty(ConfigKeys.VOLUME, String.valueOf(configuration.getVolume()));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String comment = "created by sigtrack at "+dateFormat.format(new Date());
            
            properties.store(stream, comment);
        }
    }
}
