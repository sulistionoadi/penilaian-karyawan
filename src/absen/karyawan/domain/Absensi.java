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
public class Absensi {
    
    private Long id;
    private Karyawan karyawan;
    private Date tanggal;
    private Boolean hadir;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean isHadir() {
        return hadir;
    }

    public void setHadir(Boolean hadir) {
        this.hadir = hadir;
    }
    
}
