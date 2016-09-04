package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author binary
 */
public class SimpleForm extends JPanel
{
    private GridBagConstraints c;
    
    public SimpleForm(){
        super(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = -1;
        c.gridheight = 1;
        c.gridwidth = 1;
    }
    
    public void addRow(String title, JComponent component){
        c.gridy++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(new JLabel(title), c);
        c.gridx++;
        c.anchor = GridBagConstraints.LINE_START;
        add(component, c);
    }
}
