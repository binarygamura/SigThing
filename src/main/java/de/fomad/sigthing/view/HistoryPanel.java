package de.fomad.sigthing.view;

import de.fomad.sigthing.model.Model;
import de.fomad.sigthing.model.SolarSystem;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private final JList<HistoryNode> history;

    private static final int DEFAULT_WIDTH = 185;

    private final Model model;
    
    private final DefaultListModel<HistoryNode> listModel;
    
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
	    listModel.addElement(new HistoryNode(solarSystem));
	});
	history.setSelectedIndex(selectedIndex);
	history.validate();
	history.repaint();
    }

    public static class SolarSystemRenderer extends DefaultListCellRenderer {

        private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	    HistoryNode historyNode = (HistoryNode) value;
            StringBuilder builder = new StringBuilder("<html><b>");
            builder.append(historyNode.solarSystem.getName());
            builder.append("</b>").append("<br><i>");
            builder.append(dateFormat.format(historyNode.added)).append("</i>");
	    label.setText(builder.toString());
	    return label;
	}
    }
    
    private static class HistoryNode {
        private final Date added;
        
        private final SolarSystem solarSystem;
        
        private HistoryNode(SolarSystem solarSystem){            
            this.solarSystem = solarSystem;
            added = new Date();
        }
    }
}
