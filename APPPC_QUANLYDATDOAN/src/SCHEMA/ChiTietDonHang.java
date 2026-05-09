package SCHEMA;

public class ChiTietDonHang {
    private int chiTietId;
    private String donHangId;
    private String sanPhamId;
    private int soLuong;
    private double giaLucDat;

    public ChiTietDonHang() {}

    public ChiTietDonHang(int chiTietId, String donHangId, String sanPhamId, int soLuong, double giaLucDat) {
        this.chiTietId = chiTietId;
        this.donHangId = donHangId;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
        this.giaLucDat = giaLucDat;
    }

    public int getChiTietId() { return chiTietId; }
    public void setChiTietId(int chiTietId) { this.chiTietId = chiTietId; }

    public String getDonHangId() { return donHangId; }
    public void setDonHangId(String donHangId) { this.donHangId = donHangId; }

    public String getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(String sanPhamId) { this.sanPhamId = sanPhamId; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getGiaLucDat() { return giaLucDat; }
    public void setGiaLucDat(double giaLucDat) { this.giaLucDat = giaLucDat; }
}