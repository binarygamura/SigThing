package de.fomad.sigthing.view.sounds;

import de.fomad.sigthing.model.ApplicationConfiguration;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary gamura
 */
public class SoundManager {

    private static final Logger LOGGER = LogManager.getLogger(SoundManager.class);

    private final ApplicationConfiguration configuration;
    
    public SoundManager(ApplicationConfiguration configuration){
        this.configuration = configuration;
    }
    
    public void playSound(SoundId id) {
        
        try {
            if(configuration.isPlaySounds()){
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundManager.class.getResourceAsStream(id.file));
                AudioFormat format = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                
                Clip audioClip = (Clip) AudioSystem.getLine(info);
                audioClip.open(audioStream);
                FloatControl volume = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN); 
                volume.setValue(configuration.getVolume());
                audioClip.start();
            }
        }
        catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            LOGGER.warn("unable to play sound \"" + id.getFile() + "\".", ex);
        }
    }

    public enum SoundId {

	NOTIFICATION_SOLAR_SYSTEM_CHANGE("sms-alert-5-daniel_simon.wav"),
        
        NOTIFICATION_NEW_SIGNATURES("sms-alert-1-daniel_simon.wav");

	private String file;

	private SoundId(String file) {
	    this.file = file;
	}

	public String getFile() {
	    return file;
	}
    }
}
