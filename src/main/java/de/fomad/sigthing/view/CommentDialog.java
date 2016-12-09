package de.fomad.sigthing.view;

import de.fomad.siglib.entities.Signature;
import de.fomad.sigthing.view.icons.IconCache;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author binary
 */
public class CommentDialog extends JDialog{
    
    private static final Logger LOGGER = LogManager.getLogger(CommentDialog.class);
    
    private JEditorPane editorPane;
    
    private transient Signature signature;
    
    public CommentDialog(GUI parent){
        super(parent, "edit comment");
        init();
    }
    
    public void openCentered(Signature signature){
        this.signature = signature;
        setLocationRelativeTo((GUI) getParent());
        editorPane.setText(signature.getComment());
        setVisible(true);
    }
    
    private void init(){
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        JButton closeButton = new JButton("close", ((GUI) getParent()).getIconCache().getImageIcon(IconCache.IconId.CANCEL_ICON));
        editorPane = new JEditorPane();
        
        setMinimumSize(new Dimension(240, 160));
        setPreferredSize(new Dimension(240, 160));
        closeButton.addActionListener(e -> {
            try{
                signature.setComment(editorPane.getText().trim());
                ((GUI) getParent()).getController().getDatabaseController().updateComment(signature);
                setVisible(false);
            }
            catch(SQLException ex){
                LOGGER.warn("error while updating comment for signature.", ex);
                JOptionPane.showMessageDialog(CommentDialog.this, ex.getMessage(), "error", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        add(closeButton, BorderLayout.SOUTH);
        
        validate();
        pack();
    }
}
