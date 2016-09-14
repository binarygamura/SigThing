package de.fomad.sigthing.model;

/**
 *
 * @author binary
 */
public final class Constants {

    private Constants() {

    }

    public static final class Formats {
        
        private Formats(){
            
        }
        public static final String DATE = "yyyy-MM-dd HH:mm:ss";
    }
    
    public static final class Config {

        private Config() {

        }

        public static final String DEFAULT_CONFIG_FILE_LOCATION = System.getProperty("user.home") + "/sigtrack_config.properties";

        public static final class Keys {
            
            private Keys(){
                
            }
            
            public static final String PLAY_SOUNDS = "play_sounds";
            public static final String ALWAYS_ON_TOP = "always_on_top";
            public static final String VOLUME = "volume";
        }
    }
}
