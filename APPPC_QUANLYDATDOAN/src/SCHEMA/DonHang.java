package SCHEMA;
import java.sql.Timestamp;

public class DonHang {
    private String donHangId;
    private String khachHangId;
    private String quanAnId;
    private String diaChiGiao;
    private double tongTien;
    private String trangThai;
    private Timestamp thoiGianDat;

    public DonHang() {}

    public DonHang(String donHangId, String khachHangId, String quanAnId, String diaChiGiao, double tongTien, String trangThai, Timestamp thoiGianDat) {
        this.donHangId = donHangId;
        this.khachHangId = khachHangId;
        this.quanAnId = quanAnId;
        this.diaChiGiao = diaChiGiao;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.thoiGianDat = thoiGianDat;
    }

    public String getDonHangId() { return donHangId; }
    public void setDonHangId(String donHangId) { this.donHangId = donHangId; }

    public String getKhachHangId() { return khachHangId; }
    public void setKhachHangId(String khachHangId) { this.khachHangId = khachHangId; }

    public String getQuanAnId() { return quanAnId; }
    public void setQuanAnId(String quanAnId) { this.quanAnId = quanAnId; }

    public String getDiaChiGiao() { return diaChiGiao; }
    public void setDiaChiGiao(String diaChiGiao) { this.diaChiGiao = diaChiGiao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Timestamp getThoiGianDat() { return thoiGianDat; }
    public void setThoiGianDat(Timestamp thoiGianDat) { this.thoiGianDat = thoiGianDat; }
}