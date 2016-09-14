package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.ApplicationConfiguration;
import de.fomad.sigthing.model.Constants;
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
    
    
    
    
    public static void readConfig(ApplicationConfiguration configuration) throws IOException{
        File configFile = new File(Constants.Config.DEFAULT_CONFIG_FILE_LOCATION);
        if(configFile.exists()){
            try(FileInputStream stream = new FileInputStream(configFile)){
                Properties properties = new Properties();
                properties.load(stream);
                
                if(properties.containsKey(Constants.Config.Keys.PLAY_SOUNDS)){
                    configuration.setPlaySounds(Boolean.valueOf(properties.getProperty(Constants.Config.Keys.PLAY_SOUNDS)));
                }
                if(properties.containsKey(Constants.Config.Keys.ALWAYS_ON_TOP)){
                    configuration.setAlwaysOnTop(Boolean.valueOf(properties.getProperty(Constants.Config.Keys.ALWAYS_ON_TOP)));
                }
                if(properties.containsKey(Constants.Config.Keys.VOLUME)){
                    configuration.setVolume(Integer.parseInt(properties.getProperty(Constants.Config.Keys.VOLUME)));
                }
            }
        }
    }
    
    public static void writeConfig(ApplicationConfiguration configuration) throws IOException{
        
        File configFile = new File(Constants.Config.DEFAULT_CONFIG_FILE_LOCATION);
        if(!configFile.exists() && !configFile.createNewFile()){
            throw new IOException("unable to create empty configuration file \""+configFile.getAbsolutePath()+"\".");
        }
        try(FileOutputStream stream = new FileOutputStream(configFile)){
            
            Properties properties = new Properties();
            properties.setProperty(Constants.Config.Keys.PLAY_SOUNDS, String.valueOf(configuration.isPlaySounds()));
            properties.setProperty(Constants.Config.Keys.ALWAYS_ON_TOP, String.valueOf(configuration.isAlwaysOnTop()));
            properties.setProperty(Constants.Config.Keys.VOLUME, String.valueOf(configuration.getVolume()));
            DateFormat dateFormat = new SimpleDateFormat(Constants.Formats.DATE);
            String comment = "created by sigtrack at "+dateFormat.format(new Date());
            
            properties.store(stream, comment);
        }
    }
}
