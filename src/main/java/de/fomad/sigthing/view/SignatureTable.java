package de.fomad.sigthing.view;

import de.fomad.sigthing.model.Signature;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
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
        setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
    }
    
    public void setData(List<Signature> signatures){
        ((SignatureTableModel) getModel()).mergeData(signatures);
    }
    
    
    private static class SignatureTableModel extends DefaultTableModel{
        
        private List<Signature> signatures;

        private SignatureTableModel() {
            signatures = Collections.EMPTY_LIST;
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

        @Override
        public Object getValueAt(int row, int column) {
            Object result = "";
            if(row < signatures.size()){
                Signature signature = signatures.get(row);
                switch(column){
                    case 0:
                        result = signature.getSignature();
                        break;
                    case 1:
                        if(signature.getName().isEmpty()){
                            result = signature.getScanGroup();
                        }
                        else {
                            result = signature.getName();
                        }
                        
                        break;
                    case 2:
                        result = signature.getScanGroup();
                        break;
                    case 3:
                        result = signature.getSignalStrength();
                        break;
                }
            }
            return result;
        }
        
        private void mergeData(List<Signature> signatures){
            this.signatures = signatures;
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public int getRowCount() {
            return signatures == null ? 0 : signatures.size();
        }
    }
}
