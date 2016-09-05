package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author binary
 */
public class InfoPanel extends JPanel 
{
    private JTextField signatureField;
    
    private JTextField signalStrenghField;
    
    private JTextField segmentField;
    
    public InfoPanel(){
        super();
        init();
    }
    
    private void init(){
        signatureField = new JTextField();
        signalStrenghField = new JTextField();
        segmentField = new JTextField();
        
        JPanel characterInfoPanel = new JPanel(new BorderLayout());
        
        
        SimpleForm form = new SimpleForm();
        
        form.addRow("Signature", signatureField);
        form.addRow("Strenght", signalStrenghField);
        form.addRow("Segment", segmentField);
        
        add(form);
    }
    
    
}
