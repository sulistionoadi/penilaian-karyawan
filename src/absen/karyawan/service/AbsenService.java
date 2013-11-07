/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package absen.karyawan.service;

import absen.karyawan.domain.Absensi;
import absen.karyawan.domain.Jabatan;
import absen.karyawan.domain.Karyawan;
import absen.karyawan.domain.PenilaianDTO;
import absen.karyawan.domain.Point;
import absen.karyawan.domain.RekapNilai;
import java.util.Date;
import java.util.List;

/**
 *
 * @author adi
 */
public interface AbsenService {
    
    public void simpan(Jabatan k) throws Exception;
    public void update(Jabatan k) throws Exception;
    public void deleteJabatan(String kode) throws Exception;
    public List<Jabatan> getAllJabatan() throws Exception;
    public Jabatan getJabatanByKode(String kode) throws Exception;
    
    public void simpan(Karyawan k) throws Exception;
    public void update(Karyawan k) throws Exception;
    public void deleteKaryawan(String kode) throws Exception;
    public List<Karyawan> getAllKaryawan() throws Exception;
    public Karyawan findKaryawanByNip(String nip) throws Exception;
    public List<Karyawan> getKaryawanByNipAndNameLike(String criteria) throws Exception;
    
    public void simpan(Absensi abs) throws Exception;
    public void simpan(Point point) throws Exception;
    public List<PenilaianDTO> hitungRekapAbsensi(String sdate, String edate) throws Exception;
    public List<PenilaianDTO> hitungRekapAbsensi(Karyawan k, String sdate, String edate) throws Exception;
    public List<PenilaianDTO> hitungRekapPoint(String sdate, String edate) throws Exception;
    public List<PenilaianDTO> hitungRekapPoint(Karyawan k, String sdate, String edate) throws Exception;
    public List<PenilaianDTO> hitungRekapAll(String sdate, String edate) throws Exception;
    public List<PenilaianDTO> hitungRekapAll(Karyawan k, String sdate, String edate) throws Exception;
    
    public void generateRekap(Date tanggal) throws Exception;
    public RekapNilai findOneRekapNilai(Karyawan k, String bulan, String tahun) throws Exception;
    public List<RekapNilai> getRekapNilai(String bulan, String tahun) throws Exception;
    
}
