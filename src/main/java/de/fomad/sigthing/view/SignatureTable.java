package de.fomad.sigthing.view;

import de.fomad.sigthing.model.Signature;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author boreas
 */
public class SignatureTable extends JTable {
    
    private SignatureTableModel tableModel;
    
    public SignatureTable(){
        super();
        tableModel = new SignatureTableModel();
        getTableHeader().setReorderingAllowed(false);
        setModel(tableModel);
    }
    
    public void setData(Signature[] signatures){
        ((SignatureTableModel) getModel()).mergeData(signatures);
    }
    
    
    private static class SignatureTableModel extends DefaultTableModel{
        
        private Signature[] signatures;

        private SignatureTableModel() {
            signatures = new Signature[0];
        }

        @Override
        public String getColumnName(int column) {
            switch(column){
                case 0:
                    return "ID";
                case 1:
                    return "Name";
                case 2:
                    return "Group";
                case 3:
                    return "Signal";
                default:
                    return "unknown!";
            }
        }
        
        private void mergeData(Signature[] signatures){
            
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public int getRowCount() {
            return signatures == null ? 0 : signatures.length;
        }
    }
}
