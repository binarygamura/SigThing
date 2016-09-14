package de.fomad.sigthing.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author binary
 */
public class SimpleForm extends JPanel {

    private final GridBagConstraints c;

    public SimpleForm() {
	super(new GridBagLayout());
	c = new GridBagConstraints();
	c.insets = new Insets(2, 5, 2, 5);
	c.gridx = 0;
	c.gridy = -1;
	c.gridheight = 1;
	c.gridwidth = 1;
    }

    public void addLine(JComponent component){
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        add(component, c);
        c.gridwidth = 1;
    }
    
    public void addRow(String title, JComponent component) {
	c.gridy++;
	c.gridx = 0;
	c.anchor = GridBagConstraints.LINE_END;
	add(new JLabel(title), c);
	c.gridx++;
	c.anchor = GridBagConstraints.LINE_START;
	add(component, c);
    }
}
