/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.tablemodel;

import absen.karyawan.domain.Karyawan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adi
 */
public class KaryawanTableModel extends AbstractTableModel {
    
    private List<Karyawan> datas = new ArrayList<Karyawan>();
    private String[] header = { "NIP", "Nama", "Alamat", "Tgl Lahir", "No. HP", "Jabatan" };

    public KaryawanTableModel(List<Karyawan> datas) {
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
        Karyawan k = datas.get(rowIndex);
        switch (columnIndex) {
            case 0: return k.getNip();
            case 1: return k.getNama();
            case 2: return k.getAlamat();
            case 3: return k.getTglLahir();
            case 4: return k.getNoHP();
            case 5: return k.getJabatan().getNama();
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 3 : return Date.class;
            default: return String.class;
        }
    }
    
}
