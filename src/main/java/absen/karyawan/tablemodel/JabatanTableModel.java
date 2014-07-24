/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.tablemodel;

import absen.karyawan.domain.Jabatan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adi
 */
public class JabatanTableModel extends AbstractTableModel {
    
    private List<Jabatan> datas = new ArrayList<Jabatan>();
    private String[] header = { "Kode", "Nama"};

    public JabatanTableModel(List<Jabatan> datas) {
        this.datas = datas;
    }

    @Override
    public int getRowCount() {
        return datas.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Jabatan k = datas.get(rowIndex);
        switch (columnIndex) {
            case 0: return k.getKode();
            case 1: return k.getNama();
            default: return "";
        }
    }
    
}
