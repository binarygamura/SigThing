package de.fomad.sigthing.view;

import de.fomad.sigthing.model.Signature;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author boreas
 */
public class SignatureTable extends JTable {

    private final SignatureTableModel tableModel;
    
    private static final Logger LOGGER = LogManager.getLogger(SignatureTable.class);

    public SignatureTable() {
        super();
        tableModel = new SignatureTableModel();
        getTableHeader().setReorderingAllowed(false);
        setModel(tableModel);
        setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
        setAutoCreateRowSorter(true);
        
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
    }

    public void setData(List<Signature> signatures) {
        ((SignatureTableModel) getModel()).mergeData(signatures);
    }

    public Signature getSignatureAtRow(int index) {
        Signature result = null;
        if (index >= 0 && index < tableModel.getRowCount()) {
            result = tableModel.signatures.get(index);
        }
        return result;
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        String tip = null;
        Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);

        try {
            if (rowIndex >= 0) {
                Signature signature = getSignatureAtRow(rowIndex);
                if(signature != null){
                    tip = signature.getComment();
                }
            }
        } catch (Exception ex) {
            LOGGER.warn("error while getting tooltip text.");
            //catch null pointer exception if mouse is over an empty line
        }
        System.out.println(tip);
        return tip;
    }

    private static class SignatureTableModel extends DefaultTableModel {

        private List<Signature> signatures;

        private final DecimalFormat decimalFormat;
        
        private SignatureTableModel() {
            signatures = Collections.EMPTY_LIST;
            decimalFormat = new DecimalFormat("0.00");
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        
        
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "ID";
                case 1:
                    return "Name";
                case 2:
                    return "Group";
                case 3:
                    return "Signal";
                case 4:
                    return "Added by";
                default:
                    return "unknown!";
            }
        }

        @Override
        public Object getValueAt(int row, int column) {
            Object result = "";
            if (row < signatures.size()) {
                Signature signature = signatures.get(row);
                switch (column) {
                    case 0:
                        result = signature.getSignature();
                        break;
                    case 1:
                        if (signature.getName().isEmpty()) {
                            result = signature.getScanGroup();
                        } else {
                            result = signature.getName();
                        }

                        break;
                    case 2:
                        result = signature.getScanGroup();
                        break;
                    case 3:
                        result = decimalFormat.format(signature.getSignalStrength())+"%";
                        break;
                    case 4:
                        result = signature.getAddedBy();
                        break;
                    default:
                        result = "";
                }
            }
            return result;
        }

        private void mergeData(List<Signature> signatures) {
            this.signatures = signatures;
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public int getRowCount() {
            return signatures == null ? 0 : signatures.size();
        }
    }
}
