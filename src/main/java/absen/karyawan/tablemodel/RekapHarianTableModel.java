/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.tablemodel;

import absen.karyawan.domain.PenilaianDTO;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adi
 */
public class RekapHarianTableModel extends AbstractTableModel {

    private List<PenilaianDTO> listPenilaianDto;
    private String[] header = { "NIP", "Nama Karyawan", "Jabatan", "Tanggal", "Jumlah Kehadiran", "Point" };

    public RekapHarianTableModel(List<PenilaianDTO> listPenilaianDto) {
        this.listPenilaianDto = listPenilaianDto;
    }
            
    @Override
    public int getRowCount() {
        return listPenilaianDto.size();
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
        PenilaianDTO k = listPenilaianDto.get(rowIndex);
        switch (columnIndex) {
            case 0: return k.getKaryawan().getNip();
            case 1: return k.getKaryawan().getNama();
            case 2: return k.getKaryawan().getJabatan().getNama();
            case 3: return k.getTanggal();
            case 4: return k.getJumlahKehadiran();
            case 5: return k.getPoint();
            default: return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 3 : return Date.class;
            case 4 : return Integer.class;
            case 5 : return Integer.class;
            default: return String.class;
        }
    }
    
}
