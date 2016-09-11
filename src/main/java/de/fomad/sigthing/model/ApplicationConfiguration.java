package de.fomad.sigthing.model;

/**
 *
 * @author boreas
 */
public class ApplicationConfiguration {
    
    private boolean playSounds;
    
    private boolean alwaysOnTop;
    
    private int volume;
    
    public ApplicationConfiguration(){
        playSounds = true;
        alwaysOnTop = true;
        volume = 0;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
    }
    
    public boolean isPlaySounds() {
        return playSounds;
    }

    public void setPlaySounds(boolean playSounds) {
        this.playSounds = playSounds;
    }
    
    
}
