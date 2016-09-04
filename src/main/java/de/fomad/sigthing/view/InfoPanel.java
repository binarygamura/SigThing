package de.fomad.sigthing.view;

import javax.swing.JTextField;

/**
 *
 * @author binary
 */
public class InfoPanel extends SimpleForm
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
        
        addRow("Signature", signatureField);
        addRow("Strenght", signalStrenghField);
        addRow("Segment", segmentField);
    }
    
    
}
