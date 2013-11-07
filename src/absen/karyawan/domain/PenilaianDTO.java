/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.domain;

import java.util.Date;

/**
 *
 * @author adi
 */
public class PenilaianDTO {
    
    public Karyawan karyawan;
    public Date tanggal;
    public Integer jumlahKehadiran;
    public Integer point;

    public Karyawan getKaryawan() {
        return karyawan;
    }

    public void setKaryawan(Karyawan karyawan) {
        this.karyawan = karyawan;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public Integer getJumlahKehadiran() {
        return jumlahKehadiran;
    }

    public void setJumlahKehadiran(Integer jumlahKehadiran) {
        this.jumlahKehadiran = jumlahKehadiran;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
    
}
