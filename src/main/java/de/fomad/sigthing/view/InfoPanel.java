package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.fomad.sigthing.model.Character;
import java.net.MalformedURLException;
import javax.swing.ImageIcon;

/**
 *
 * @author binary
 */
public class InfoPanel extends JPanel 
{
    private JTextField signatureField;
    
    private JTextField signalStrenghField;
    
    private JTextField segmentField;
    
    private JLabel iconLabel;   
    
    private JTextField solarSystemNameField;
    
    private JTextField securityStatusField;
    
    public InfoPanel(){
        super(new BorderLayout());
        init();
    }
    
    private void init(){
        signatureField = new JTextField(10);
        signatureField.setEditable(false);
        signalStrenghField = new JTextField(10);
        signalStrenghField.setEditable(false);
        segmentField = new JTextField(10);
        segmentField.setEditable(false);
        
        iconLabel = new JLabel();
        iconLabel.setMinimumSize(new Dimension(156, 156));
        iconLabel.setPreferredSize(new Dimension(156, 156));
        iconLabel.setBorder(BorderFactory.createTitledBorder("current pilot"));
        iconLabel.setHorizontalAlignment(JLabel.CENTER);
        
        solarSystemNameField = new JTextField(10);
        solarSystemNameField.setEditable(false);
        securityStatusField = new JTextField(10);
        securityStatusField.setEditable(false);
        
        SimpleForm solarSystemForm = new SimpleForm();
        solarSystemForm.setBorder(BorderFactory.createTitledBorder("current system"));
        solarSystemForm.addRow("Name", solarSystemNameField);
        solarSystemForm.addRow("Security", securityStatusField);
        
        SimpleForm selectedItemForm = new SimpleForm();
        selectedItemForm.setBorder(BorderFactory.createTitledBorder("selected item"));
        selectedItemForm.addRow("Signature", signatureField);
        selectedItemForm.addRow("Strenght", signalStrenghField);
        selectedItemForm.addRow("Segment", segmentField);
        
        add(iconLabel, BorderLayout.WEST);
        add(selectedItemForm, BorderLayout.CENTER);
        add(solarSystemForm, BorderLayout.EAST);
    }
    
    public void setIcon(Character character) throws MalformedURLException{
        iconLabel.setIcon(new ImageIcon(character.getPortrait().getLarge().getHref().toURL()));
    }
}
