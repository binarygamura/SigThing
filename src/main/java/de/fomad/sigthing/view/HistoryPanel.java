package de.fomad.sigthing.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author binary
 */
public class HistoryPanel extends JPanel
{
    private final JList<String> history;
    
    private int WIDTH = 200;
    
    public HistoryPanel(){
        super(new BorderLayout());        
        setBorder(BorderFactory.createTitledBorder("travel history"));
        history = new JList<>();
        JScrollPane scrollPane = new JScrollPane(history);
        scrollPane.setMaximumSize(new Dimension(WIDTH, 2500));
        scrollPane.setPreferredSize(new Dimension(WIDTH, 150));
        scrollPane.setMinimumSize(new Dimension(WIDTH, 150));
        add(scrollPane, BorderLayout.CENTER);
    }
}
