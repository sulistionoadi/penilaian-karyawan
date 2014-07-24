/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.domain;

/**
 *
 * @author adi
 */
public class RekapNilai {
    
    public Long id;
    public Karyawan karyawan;
    public String bulan;
    public String tahun;
    public Integer saldoAwal;
    public Integer point;
    
    public Integer getPointAkhir(){
        if(saldoAwal!=null && point!=null){
            return saldoAwal + point;
        } else {
            return 0;
        }
    }

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

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public Integer getSaldoAwal() {
        return saldoAwal;
    }

    public void setSaldoAwal(Integer saldoAwal) {
        this.saldoAwal = saldoAwal;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
    
}
