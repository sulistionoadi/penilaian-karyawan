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
public class Karyawan {
    
    private String nip;
    private String nama;
    private String alamat;
    private String noHP;
    private Date tglLahir;
    private Jabatan jabatan;

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHP() {
        return noHP;
    }

    public void setNoHP(String noHP) {
        this.noHP = noHP;
    }

    public Date getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(Date tglLahir) {
        this.tglLahir = tglLahir;
    }

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }
    
    
    
}
