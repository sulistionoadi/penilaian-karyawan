/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.tablemodel;

import absen.karyawan.domain.RekapNilai;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adi
 */
public class RekapBulananTableModel extends AbstractTableModel {
    
    private List<RekapNilai> listRekapNilai;
    private String[] header = { "NIP", "Nama Karyawan", "Jabatan", "BL/THN", "Nilai Point" };

    public RekapBulananTableModel(List<RekapNilai> listRekapNilai) {
        this.listRekapNilai = listRekapNilai;
    }

    @Override
    public int getRowCount() {
        return listRekapNilai.size();
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
        RekapNilai r = listRekapNilai.get(rowIndex);
        switch(columnIndex){
            case 0 : return r.getKaryawan().getNip();
            case 1 : return r.getKaryawan().getNama();
            case 2 : return r.getKaryawan().getJabatan().getNama();
            case 3 : return r.getBulan() + '/' + r.getTahun();
            case 4 : return r.getPointAkhir();
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 4 : return Integer.class;
            default: return String.class;
        }
    }
}
