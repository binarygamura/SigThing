package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.fomad.sigthing.model.Character;
import de.fomad.sigthing.model.Constants;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import de.fomad.sigthing.view.icons.IconCache;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary
 */
public class InfoPanel extends JPanel {

    private static final Logger LOGGER = LogManager.getLogger(InfoPanel.class);
    
    private JTextField signatureField;

    private JTextField signalStrenghField;

    private JTextField signatureScanGroupField;

    private JTextField addedField;
    
    private JLabel iconLabel;

    private JTextField solarSystemNameField;

    private JTextField securityStatusField;
    
    private final DecimalFormat secStatusFormat;
    
    private final DecimalFormat strenghtFormat;
    
    private final DateFormat dateFormat;

    private SolarSystem solarSystem;
    
    private final GUI parent;
    
    public InfoPanel(GUI parent) {
	super(new BorderLayout());
        this.parent = parent;
        secStatusFormat = new DecimalFormat("0.0"); 
        strenghtFormat = new DecimalFormat("0.00");
        dateFormat = new SimpleDateFormat(Constants.Formats.DATE);
	init();
    }
    
    public void setCurrentSignature(Signature signature){
        if(signature == null){
            signatureField.setText("");
            signalStrenghField.setText("");
            signatureScanGroupField.setText("");            
            addedField.setText("");
        }
        else {
            signatureField.setText(signature.getSignature());
            signalStrenghField.setText(strenghtFormat.format(signature.getSignalStrength())+"%");
            signatureScanGroupField.setText(signature.getScanGroup());
            addedField.setText(dateFormat.format(signature.getAdded()));
            addedField.setCaretPosition(0);
        }
    }
    
    public void setCurrentSolarSystem(SolarSystem solarSystem){
        this.solarSystem = solarSystem;
	solarSystemNameField.setText(solarSystem.getName());
	solarSystemNameField.setCaretPosition(0);
        securityStatusField.setText(secStatusFormat.format(solarSystem.getInformation().getSecurityStatus()));
    }

    private void init() {
	signatureField = new JTextField(11);
	signatureField.setEditable(false);
	signalStrenghField = new JTextField(11);
	signalStrenghField.setEditable(false);
	signatureScanGroupField = new JTextField(11);
	signatureScanGroupField.setEditable(false);
        addedField = new JTextField(11);
        addedField.setEditable(false);

        JButton waypointButton  = new JButton("set destination", parent.getIconCache().getImageIcon(IconCache.IconId.ROUTE));
        waypointButton.addActionListener(e -> {
            try{
                if(solarSystem != null){
                    parent.getController().addWaypointTo(solarSystem);
                }
            }
            catch(URISyntaxException | IOException ex){
                LOGGER.warn("unable to set route.", ex);
                JOptionPane.showMessageDialog(parent, ex.getMessage(), "unable to set route", JOptionPane.WARNING_MESSAGE);
            }
        });
        
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
        solarSystemForm.addLine(waypointButton);
        

	SimpleForm selectedItemForm = new SimpleForm();
	selectedItemForm.setBorder(BorderFactory.createTitledBorder("selected item"));
	selectedItemForm.addRow("Signature", signatureField);
	selectedItemForm.addRow("Strength", signalStrenghField);
	selectedItemForm.addRow("Scan Group", signatureScanGroupField);
        selectedItemForm.addRow("First seen", addedField);

	add(iconLabel, BorderLayout.WEST);
	add(selectedItemForm, BorderLayout.CENTER);
	add(solarSystemForm, BorderLayout.EAST);
    }

    public void setIcon(Character character) throws MalformedURLException {
	iconLabel.setIcon(new ImageIcon(character.getPortrait().getLarge().getHref().toURL()));
    }
    

}
