package absen.karyawan.service;


import absen.karyawan.domain.Absensi;
import absen.karyawan.domain.Jabatan;
import absen.karyawan.domain.Karyawan;
import absen.karyawan.domain.PenilaianDTO;
import absen.karyawan.domain.Point;
import absen.karyawan.domain.RekapNilai;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adi
 */
public class AbsenServiceImpl implements AbsenService{
    
    private final String SQL_INSERT_JABATAN = "INSERT INTO jabatan (kode, nama) VALUES (?,?)";
    private final String SQL_UPDATE_JABATAN = "UPDATE jabatan set nama=? WHERE kode=?";
    private final String SQL_DELETE_JABATAN = "DELETE FROM jabatan WHERE kode=?";
    private final String SQL_FINDALL_JABATAN = "SELECT * FROM jabatan";
    private final String SQL_FINDONE_JABATAN = "SELECT * FROM jabatan WHERE kode=?";
    
    private final String SQL_INSERT_KARYAWAN = "INSERT INTO karyawan (nip,nama,alamat,tgl_lahir,no_hp,kode_jabatan) VALUES (?,?,?,?,?,?)";
    private final String SQL_UPDATE_KARYAWAN = "UPDATE karyawan set nama=?, alamat=?, tgl_lahir=?, no_hp=?, kode_jabatan=? WHERE nip=?";
    private final String SQL_DELETE_KARYAWAN = "DELETE FROM karyawan WHERE nip=?";
    private final String SQL_FINDALL_KARYAWAN = "SELECT j.nama as nama_jabatan, k.* FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan";
    private final String SQL_FINDLIKE_KARYAWAN = "SELECT j.nama as nama_jabatan, k.* FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan where k.nip like ? or k.nama like ?";
    private final String SQL_FINDONE_KARYAWAN = "SELECT j.nama as nama_jabatan, k.* FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan WHERE k.nip=?";
    
    private final String SQL_SIMPAN_ABSENSI = "INSERT INTO absensi (nip, tanggal, hadir) VALUES (?,?,?)";
    private final String SQL_SIMPAN_POINT = "INSERT INTO point (nip, tanggal, point) VALUES (?,?,?)";
    private final String SQL_HITUNG_REKAP_ABSENSI_HARIAN = "SELECT j.nama as nama_jabatan, k.*, sum(a.hadir) as jml_kehadiran FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN absensi a on a.nip=k.nip WHERE a.tanggal between ? and ? group by k.nip";
    private final String SQL_HITUNG_REKAP_ABSENSI_HARIAN_PER_KARYAWAN = "SELECT j.nama as nama_jabatan, k.*, sum(a.hadir) as jml_kehadiran FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN absensi a on a.nip=k.nip WHERE a.tanggal between ? and ? and k.nip=?";
    private final String SQL_HITUNG_REKAP_POINT_HARIAN = "SELECT j.nama as nama_jabatan, k.*, sum(p.point) as jml_point FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN point p on p.nip=k.nip WHERE p.tanggal between ? and ? group by k.nip";
    private final String SQL_HITUNG_REKAP_POINT_HARIAN_PER_KARYAWAN = "SELECT j.nama as nama_jabatan, k.*, sum(p.point) as jml_point FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN point p on p.nip=k.nip WHERE p.tanggal between ? and ? and k.nip=?";
    private final String SQL_HITUNG_REKAP_ALL_HARIAN = "SELECT j.nama AS nama_jabatan, k.*, SUM(a.hadir) AS jml_kehadiran, (SELECT SUM(p.point) FROM point p WHERE p.nip = k.nip) AS jml_point FROM karyawan k INNER JOIN jabatan j ON j.kode = k.kode_jabatan INNER JOIN absensi a ON a.nip = k.nip WHERE a.tanggal BETWEEN ? AND ? GROUP BY k.nip";
    private final String SQL_HITUNG_REKAP_ALL_HARIAN_PER_KARYAWAN = "SELECT j.nama AS nama_jabatan, k.*, SUM(a.hadir) AS jml_kehadiran, (SELECT SUM(p.point) FROM point p WHERE p.nip = k.nip) AS jml_point FROM karyawan k INNER JOIN jabatan j ON j.kode = k.kode_jabatan INNER JOIN absensi a ON a.nip = k.nip WHERE a.tanggal BETWEEN ? AND ? and k.nip=? GROUP BY k.nip";
    
    private final String SQL_INSERT_REKAP_NILAI_KARYAWAN = "INSERT INTO rekap_nilai (nip, bulan, tahun, saldo_awal, point) VALUES (?,?,?,?,?)";
    private final String SQL_FINDONE_REKAP_NILAI = "SELECT j.nama as nama_jabatan, k.*, r.id as id_rekap, r.bulan as bulan_rekap, r.tahun as tahun_rekap, r.saldo_awal as saldo_awal, r.point as point FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN rekap_nilai r on r.nip=k.nip WHERE k.nip=? and r.bulan=? and r.tahun=?";
    private final String SQL_FINDALL_REKAP_NILAI = "SELECT j.nama as nama_jabatan, k.*, r.id as id_rekap, r.bulan as bulan_rekap, r.tahun as tahun_rekap, r.saldo_awal as saldo_awal, r.point as point FROM karyawan k INNER JOIN jabatan j on j.kode=k.kode_jabatan INNER JOIN rekap_nilai r on r.nip=k.nip WHERE r.bulan=? and r.tahun=?";
            
    private Connection conn;
    public AbsenServiceImpl(Connection conn) {
        this.conn = conn;
    }
    
    @Override
    public void simpan(Jabatan k) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_JABATAN);
            ps.setString(1, k.getKode());
            ps.setString(2, k.getNama());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Insert ke table jabatan ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void update(Jabatan k) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_JABATAN);
            ps.setString(1, k.getNama());
            ps.setString(2, k.getKode());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Update ke table jabatan ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void deleteJabatan(String kode) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE_JABATAN);
            ps.setString(1, kode);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Delete data ditable jabatan ["+ex.getMessage()+"]");
        }   
    }

    @Override
    public List<Jabatan> getAllJabatan() throws Exception {
        try {
            List<Jabatan> result = new ArrayList<Jabatan>();
            PreparedStatement ps = conn.prepareStatement(SQL_FINDALL_JABATAN);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode"));
                j.setNama(rs.getString("nama"));
                result.add(j);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query jabatan error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public Jabatan getJabatanByKode(String kode) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_FINDONE_JABATAN);
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan pc = new Jabatan();
                pc.setKode(rs.getString("kode"));
                pc.setNama(rs.getString("nama"));
                return pc;
            }
            return null;
        } catch (SQLException ex) {
            throw new Exception("Query jabatan error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void simpan(Karyawan k) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_KARYAWAN);
            ps.setString(1, k.getNip());
            ps.setString(2, k.getNama());
            ps.setString(3, k.getAlamat());
            ps.setDate(4, new java.sql.Date(k.getTglLahir().getTime()));
            ps.setString(5, k.getNoHP());
            ps.setString(6, k.getJabatan().getKode());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Insert ke table karyawan ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void update(Karyawan k) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_KARYAWAN);
            ps.setString(1, k.getNama());
            ps.setString(2, k.getAlamat());
            ps.setDate(3, new java.sql.Date(k.getTglLahir().getTime()));
            ps.setString(4, k.getNoHP());
            ps.setString(5, k.getJabatan().getKode());
            ps.setString(6, k.getNip());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Update ke table karyawan ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void deleteKaryawan(String kode) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_DELETE_KARYAWAN);
            ps.setString(1, kode);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Delete data ditable karyawan ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<Karyawan> getAllKaryawan() throws Exception {
        try {
            List<Karyawan> result = new ArrayList<Karyawan>();
            PreparedStatement ps = conn.prepareStatement(SQL_FINDALL_KARYAWAN);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                result.add(k);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query karyawan error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public Karyawan findKaryawanByNip(String nip) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_FINDONE_KARYAWAN);
            ps.setString(1, nip);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                return k;
            }
            return null;
        } catch (SQLException ex) {
            throw new Exception("Query jabatan error : ["+ex.getMessage()+"]");
        }
    }
    
    @Override
    public List<Karyawan> getKaryawanByNipAndNameLike(String criteria) throws Exception {
        try {
            List<Karyawan> result = new ArrayList<Karyawan>();
            PreparedStatement ps = conn.prepareStatement(SQL_FINDLIKE_KARYAWAN);
            ps.setString(1, '%'+criteria+'%');
            ps.setString(2, '%'+criteria+'%');
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                result.add(k);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query karyawan error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void simpan(Absensi abs) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_SIMPAN_ABSENSI);
            ps.setString(1, abs.getKaryawan().getNip());
            ps.setDate(2, new Date(abs.getTanggal().getTime()));
            ps.setBoolean(3, abs.isHadir());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Insert ke table absensi ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void simpan(Point point) throws Exception {
        try {
            PreparedStatement ps = conn.prepareStatement(SQL_SIMPAN_POINT);
            ps.setString(1, point.getKaryawan().getNip());
            ps.setDate(2, new Date(point.getTanggal().getTime()));
            ps.setInt(3, point.getPoint());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Gagal Insert ke table point ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<PenilaianDTO> hitungRekapAbsensi(String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_ABSENSI_HARIAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setJumlahKehadiran(rs.getInt("jml_kehadiran"));
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<PenilaianDTO> hitungRekapAbsensi(Karyawan karyawan, String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_ABSENSI_HARIAN_PER_KARYAWAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ps.setString(3, karyawan.getNip());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setJumlahKehadiran(rs.getInt("jml_kehadiran"));
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<PenilaianDTO> hitungRekapPoint(String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_POINT_HARIAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setPoint(rs.getInt("jml_point"));
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<PenilaianDTO> hitungRekapPoint(Karyawan karyawan ,String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_POINT_HARIAN_PER_KARYAWAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ps.setString(3, karyawan.getNip());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setPoint(rs.getInt("jml_point"));
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }
    
    @Override
    public List<PenilaianDTO> hitungRekapAll(String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_ALL_HARIAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setJumlahKehadiran(rs.getInt("jml_kehadiran"));
                
                Integer point = rs.getInt("jml_point");
                if(point==null) point = 0;
                dto.setPoint(point);
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<PenilaianDTO> hitungRekapAll(Karyawan karyawan ,String sdate, String edate) throws Exception{
        try {
            List<PenilaianDTO> result = new ArrayList<PenilaianDTO>();
            PreparedStatement ps = conn.prepareStatement(SQL_HITUNG_REKAP_ALL_HARIAN_PER_KARYAWAN);
            ps.setString(1, sdate);
            ps.setString(2, edate);
            ps.setString(3, karyawan.getNip());
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan k = new Karyawan();
                k.setNip(rs.getString("nip"));
                k.setNama(rs.getString("nama"));
                k.setAlamat(rs.getString("alamat"));
                k.setNoHP(rs.getString("no_hp"));
                k.setTglLahir(rs.getDate("tgl_lahir"));
                k.setJabatan(j);
                
                PenilaianDTO dto = new PenilaianDTO();
                dto.setTanggal(new java.util.Date());
                dto.setKaryawan(k);
                dto.setJumlahKehadiran(rs.getInt("jml_kehadiran"));
                
                Integer point = rs.getInt("jml_point");
                if(point==null) point = 0;
                dto.setPoint(point);
                result.add(dto);
            }
            return result;
        } catch (SQLException ex) {
            throw new Exception("Query Penilaian DTO error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public void generateRekap(java.util.Date tanggal) throws Exception {
        PreparedStatement ps = null;
        try{
            
            conn.setAutoCommit(false);

            List<Karyawan> listKaryawans = getAllKaryawan();

            DateTimeFormatter formatBulan = DateTimeFormat.forPattern("MM");
            DateTimeFormatter formatTahun = DateTimeFormat.forPattern("yyyy");
            DateTimeFormatter formatTanggalSQL = DateTimeFormat.forPattern("yyyy-MM-dd");

            DateTime dateBulanLalu = new DateTime(tanggal.getTime()).minusMonths(1);

            for (Karyawan k : listKaryawans) {
                Integer saldoAwal = 0;
                RekapNilai rekapBulanLalu = findOneRekapNilai(k, formatBulan.print(dateBulanLalu), formatTahun.print(dateBulanLalu));
                if(rekapBulanLalu!=null){
                    saldoAwal = rekapBulanLalu.getPointAkhir();
                }
                
                DateTime startDate = new DateTime(tanggal.getTime()).withDayOfMonth(1);
                DateTime endDate = new DateTime(tanggal.getTime()).plusMonths(1).minusDays(1);

                Integer point = 0;
                List<PenilaianDTO> dto = hitungRekapAll(k, formatTanggalSQL.print(startDate), formatTanggalSQL.print(endDate));
                if(!dto.isEmpty() && dto.size()==1){
                    point = (dto.get(0).getJumlahKehadiran()*2) + dto.get(0).getPoint();
                }

                ps = conn.prepareStatement(SQL_INSERT_REKAP_NILAI_KARYAWAN);
                ps.setString(1, k.getNip());
                ps.setString(2, formatBulan.print(tanggal.getTime()));
                ps.setString(3, formatTahun.print(tanggal.getTime()));
                ps.setInt(4, saldoAwal);
                ps.setInt(5, point);
                ps.executeUpdate();
            }

            conn.commit();
        } catch(SQLException e){
            System.out.println("Generate Rekap Nilai Error : " + e.getMessage());
            conn.rollback();
        } finally {
            if(ps!=null) ps.close();
            if(conn!=null) conn.close();
        }
    }

    @Override
    public RekapNilai findOneRekapNilai(Karyawan k, String bulan, String tahun) throws Exception {
        try{
            PreparedStatement ps = conn.prepareStatement(SQL_FINDONE_REKAP_NILAI);
            ps.setString(1, k.getNip());
            ps.setString(2, bulan);
            ps.setString(3, tahun);
            ResultSet rs = ps.executeQuery();
            
            RekapNilai rn = null;
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan kar = new Karyawan();
                kar.setNip(rs.getString("nip"));
                kar.setNama(rs.getString("nama"));
                kar.setAlamat(rs.getString("alamat"));
                kar.setNoHP(rs.getString("no_hp"));
                kar.setTglLahir(rs.getDate("tgl_lahir"));
                kar.setJabatan(j);
                
                rn = new RekapNilai();
                rn.setId(rs.getLong("id_rekap"));
                rn.setBulan(rs.getString("bulan_rekap"));
                rn.setTahun(rs.getString("tahun_rekap"));
                rn.setKaryawan(kar);
                rn.setPoint(rs.getInt("point"));
                rn.setSaldoAwal(rs.getInt("saldo_awal"));
            }
            return rn;
        } catch(SQLException ex){
            throw new Exception("Query FindOne Rekap Nilai error : ["+ex.getMessage()+"]");
        }
    }

    @Override
    public List<RekapNilai> getRekapNilai(String bulan, String tahun) throws Exception {
        try{
            PreparedStatement ps = conn.prepareStatement(SQL_FINDALL_REKAP_NILAI);
            ps.setString(1, bulan);
            ps.setString(2, tahun);
            ResultSet rs = ps.executeQuery();
            
            List<RekapNilai> result = new ArrayList<RekapNilai>();
            while(rs.next()){
                Jabatan j = new Jabatan();
                j.setKode(rs.getString("kode_jabatan"));
                j.setNama(rs.getString("nama_jabatan"));
                
                Karyawan kar = new Karyawan();
                kar.setNip(rs.getString("nip"));
                kar.setNama(rs.getString("nama"));
                kar.setAlamat(rs.getString("alamat"));
                kar.setNoHP(rs.getString("no_hp"));
                kar.setTglLahir(rs.getDate("tgl_lahir"));
                kar.setJabatan(j);
                
                RekapNilai rn = new RekapNilai();
                rn.setId(rs.getLong("id_rekap"));
                rn.setBulan(rs.getString("bulan_rekap"));
                rn.setTahun(rs.getString("tahun_rekap"));
                rn.setKaryawan(kar);
                rn.setPoint(rs.getInt("point"));
                rn.setSaldoAwal(rs.getInt("saldo_awal"));
                result.add(rn);
            }
            return result;
        }catch(SQLException ex){
            throw new Exception("Query FindAll Rekap Nilai error : ["+ex.getMessage()+"]");
        }
    }
    
}
