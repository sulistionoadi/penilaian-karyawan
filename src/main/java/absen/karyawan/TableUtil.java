/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package absen.karyawan;

import java.util.Enumeration;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author adi
 */
public class TableUtil {
    public static void initColumn(JTable jTable1) {
        JTableHeader header = jTable1.getTableHeader();
        int rowCount = jTable1.getRowCount();

        Enumeration columns = jTable1.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) jTable1.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(jTable1, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();

            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) jTable1.getCellRenderer(row, col).getTableCellRendererComponent(jTable1,
                        jTable1.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // this line is very important
            width += 50;
            column.setWidth(width + jTable1.getIntercellSpacing().width);
        }
    }
}
