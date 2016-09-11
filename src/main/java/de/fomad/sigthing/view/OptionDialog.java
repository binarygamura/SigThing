package de.fomad.sigthing.view;

import de.fomad.sigthing.model.ApplicationConfiguration;
import de.fomad.sigthing.view.icons.IconCache;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JSlider;

/**
 *
 * @author boreas
 */
public class OptionDialog extends JDialog {
    
    private final GUI parentGui;
    
    private JCheckBox playSoundCheckbox;
    
    private JCheckBox alwaysOnTopCheckbox;
    
    private JSlider volumeSlider;
    
    public OptionDialog(GUI parentGui){
        super(parentGui);
        this.parentGui = parentGui;
        
        init();
    }
    
    private void init(){
        setIconImage(parentGui.getIconCache().getIcon(IconCache.IconId.CONFIGURATION));
        setResizable(false);
        setTitle("Options");
        setModal(true);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        
        JButton closeButton = new JButton("close", parentGui.getIconCache().getImageIcon(IconCache.IconId.CANCEL_ICON) );
        closeButton.addActionListener(e -> {setVisible(false);});
        closeButton.setMnemonic('c');
        
        ApplicationConfiguration configuration = parentGui.getConfig();
        
        volumeSlider = new JSlider(-20, 10, configuration.getVolume());
        volumeSlider.addChangeListener(l -> {configuration.setVolume(volumeSlider.getValue());});
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        
        playSoundCheckbox = new JCheckBox();
        playSoundCheckbox.setSelected(configuration.isPlaySounds());
        playSoundCheckbox.addChangeListener(l -> {configuration.setPlaySounds(playSoundCheckbox.isSelected());});
        
        alwaysOnTopCheckbox = new JCheckBox();
        alwaysOnTopCheckbox.setSelected(configuration.isAlwaysOnTop());
        alwaysOnTopCheckbox.addChangeListener(l -> {configuration.setAlwaysOnTop(alwaysOnTopCheckbox.isSelected());});
        
        SimpleForm form = new SimpleForm();
        form.addRow("enable sounds", playSoundCheckbox);
        form.addRow("alway on top", alwaysOnTopCheckbox);
        form.addRow("volume", volumeSlider);
        form.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        
        add(form, BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
        
        validate();
        pack();
    }
    
    public void openCentered(){
        setLocationRelativeTo(parentGui);
        setVisible(true);
    }
    
}
