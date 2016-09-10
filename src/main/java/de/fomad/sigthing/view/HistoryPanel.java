package de.fomad.sigthing.view;

import de.fomad.sigthing.model.Model;
import de.fomad.sigthing.model.SolarSystem;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author binary
 */
public class HistoryPanel extends JPanel {

    private final JList<SolarSystem> history;

    private static final int DEFAULT_WIDTH = 200;

    private final Model model;
    
    private DefaultListModel<SolarSystem> listModel;
    
    public HistoryPanel(Model model) {
	super(new BorderLayout());
	this.model = model;
	setBorder(BorderFactory.createTitledBorder("travel history"));
	listModel = new DefaultListModel<>();
	history = new JList<>(listModel);
	history.setCellRenderer(new SolarSystemRenderer());
	JScrollPane scrollPane = new JScrollPane(history);
	scrollPane.setMaximumSize(new Dimension(DEFAULT_WIDTH, 2500));
	scrollPane.setPreferredSize(new Dimension(DEFAULT_WIDTH, 150));
	scrollPane.setMinimumSize(new Dimension(DEFAULT_WIDTH, 150));
	add(scrollPane, BorderLayout.CENTER);
    }
    
    public void repaintList(){
	
	int selectedIndex = history.getSelectedIndex();
	listModel.clear();
	model.getTravelRoute().stream().forEach((solarSystem) -> {
	    listModel.addElement(solarSystem);
	});
	history.setSelectedIndex(selectedIndex);
	history.validate();
	history.repaint();
    }

    public static class SolarSystemRenderer extends DefaultListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    SolarSystem solarSystem = (SolarSystem) value;
	    label.setText(solarSystem.getName());
	    return label;
	}
    }
}
