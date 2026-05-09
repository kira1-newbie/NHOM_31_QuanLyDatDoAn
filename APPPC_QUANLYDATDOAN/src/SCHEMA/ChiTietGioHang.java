package SCHEMA;

public class ChiTietGioHang {
    private int chiTietGioHangId;
    private String gioHangId;
    private String sanPhamId;
    private int soLuong;

    public ChiTietGioHang() {
    }

    public ChiTietGioHang(int chiTietGioHangId, String gioHangId, String sanPhamId, int soLuong) {
        this.chiTietGioHangId = chiTietGioHangId;
        this.gioHangId = gioHangId;
        this.sanPhamId = sanPhamId;
        this.soLuong = soLuong;
    }

    public int getChiTietGioHangId() {
        return chiTietGioHangId;
    }

    public void setChiTietGioHangId(int chiTietGioHangId) {
        this.chiTietGioHangId = chiTietGioHangId;
    }

    public String getGioHangId() {
        return gioHangId;
    }

    public void setGioHangId(String gioHangId) {
        this.gioHangId = gioHangId;
    }

    public String getSanPhamId() {
        return sanPhamId;
    }

    public void setSanPhamId(String sanPhamId) {
        this.sanPhamId = sanPhamId;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}