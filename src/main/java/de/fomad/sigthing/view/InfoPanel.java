package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import de.fomad.sigthing.model.Character;
import de.fomad.sigthing.model.Signature;
import de.fomad.sigthing.model.SolarSystem;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;

/**
 *
 * @author binary
 */
public class InfoPanel extends JPanel {

    private JTextField signatureField;

    private JTextField signalStrenghField;

    private JTextField signatureScanGroupField;

    private JLabel iconLabel;

    private JTextField solarSystemNameField;

    private JTextField securityStatusField;
    
    private final DecimalFormat secStatusFormat;
    
    private final DecimalFormat strenghtFormat;

    public InfoPanel() {
	super(new BorderLayout());
        secStatusFormat = new DecimalFormat("0.0"); 
        strenghtFormat = new DecimalFormat("0.00");
	init();
    }
    
    public void setCurrentSignature(Signature signature){
        if(signature == null){
            signatureField.setText("");
            signalStrenghField.setText("");
            signatureScanGroupField.setText("");            
        }
        else {
            signatureField.setText(signature.getSignature());
            signalStrenghField.setText(strenghtFormat.format(signature.getSignalStrength())+"%");
            signatureScanGroupField.setText(signature.getScanGroup());
        }
    }
    
    public void setCurrentSolarSystem(SolarSystem solarSystem){
	solarSystemNameField.setText(solarSystem.getName());
	solarSystemNameField.setCaretPosition(0);
        securityStatusField.setText(secStatusFormat.format(solarSystem.getInformation().getSecurityStatus()));
    }

    private void init() {
	signatureField = new JTextField(10);
	signatureField.setEditable(false);
	signalStrenghField = new JTextField(10);
	signalStrenghField.setEditable(false);
        signalStrenghField.setHorizontalAlignment(JTextField.RIGHT);
	signatureScanGroupField = new JTextField(10);
	signatureScanGroupField.setEditable(false);

	iconLabel = new JLabel();
	iconLabel.setMinimumSize(new Dimension(156, 156));
	iconLabel.setPreferredSize(new Dimension(156, 156));
	iconLabel.setBorder(BorderFactory.createTitledBorder("current pilot"));
	iconLabel.setHorizontalAlignment(JLabel.CENTER);

	solarSystemNameField = new JTextField(10);
	solarSystemNameField.setEditable(false);
	securityStatusField = new JTextField(10);
        securityStatusField.setHorizontalAlignment(JTextField.RIGHT);
	securityStatusField.setEditable(false);

	SimpleForm solarSystemForm = new SimpleForm();
	solarSystemForm.setBorder(BorderFactory.createTitledBorder("current system"));
	solarSystemForm.addRow("Name", solarSystemNameField);
	solarSystemForm.addRow("Security", securityStatusField);

	SimpleForm selectedItemForm = new SimpleForm();
	selectedItemForm.setBorder(BorderFactory.createTitledBorder("selected item"));
	selectedItemForm.addRow("Signature", signatureField);
	selectedItemForm.addRow("Strenght", signalStrenghField);
	selectedItemForm.addRow("Scan Group", signatureScanGroupField);

	add(iconLabel, BorderLayout.WEST);
	add(selectedItemForm, BorderLayout.CENTER);
	add(solarSystemForm, BorderLayout.EAST);
    }

    public void setIcon(Character character) throws MalformedURLException {
	iconLabel.setIcon(new ImageIcon(character.getPortrait().getLarge().getHref().toURL()));
    }
    

}
