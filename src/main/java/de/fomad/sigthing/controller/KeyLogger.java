package de.fomad.sigthing.controller;

import de.fomad.sigthing.model.KeyLoggerEvent;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Observable;
import org.apache.log4j.Logger;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author binary
 */
public class KeyLogger extends Observable implements NativeKeyListener
{

    private static final long ONE_SECOND = 1000L;
    
    private long lastKeyPress;
    
    private static final Logger LOGGER = Logger.getLogger(KeyLogger.class);
    
    private boolean alreadyPressed;
    
    @Override
    public void nativeKeyPressed(NativeKeyEvent nke)
    {
        
    }
    @Override
    public void nativeKeyReleased(NativeKeyEvent nke)
    {
        try
        {
            LOGGER.debug("key \""+nke.getKeyCode()+"\" pressed.");
            if(alreadyPressed){
                alreadyPressed = false;
                if((nke.getKeyCode() & NativeKeyEvent.VC_X) == NativeKeyEvent.VC_X){
                    
                    if(System.currentTimeMillis() < lastKeyPress + ONE_SECOND){
                        String data = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor); 
                        LOGGER.info("HIT "+data);
                        setChanged();
                        notifyObservers(new KeyLoggerEvent(data));
                    }
                    else {
                        LOGGER.info("too slow...");
                    }
                }
            }
            else {
                if((nke.getKeyCode() & NativeKeyEvent.CTRL_MASK) == NativeKeyEvent.CTRL_MASK)
                {
                    LOGGER.info("preparing start of sequence!");
                    if((nke.getKeyCode() & NativeKeyEvent.VC_C) == NativeKeyEvent.VC_C){
                        alreadyPressed = true;
                    }
                }
                else {
                    alreadyPressed = false;
                }
            }
            lastKeyPress = System.currentTimeMillis();
        }
        catch(UnsupportedFlavorException | IOException ex){
            LOGGER.fatal("unable to extract data from the clipboard.");
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nke)
    {
        
    }
}
