package SCHEMA;

/**
 * Lớp lưu trữ cấu trúc dữ liệu của bảng SanPham trong Database
 */
public class SANPHAM {
    private String SanPhamId;
    private String QuanAnId;
    private String TenSanPham;
    private double Gia;

    public SANPHAM() {
    }

    public SANPHAM(String SanPhamId, String QuanAnId, String TenSanPham, double Gia) {
        this.SanPhamId = SanPhamId;
        this.QuanAnId = QuanAnId;
        this.TenSanPham = TenSanPham;
        this.Gia = Gia;
    }

    // --- Các hàm Getters và Setters ---
    public String getSanPhamId() { return SanPhamId; }
    public void setSanPhamId(String SanPhamId) { this.SanPhamId = SanPhamId; }

    public String getQuanAnId() { return QuanAnId; }
    public void setQuanAnId(String QuanAnId) { this.QuanAnId = QuanAnId; }

    public String getTenSanPham() { return TenSanPham; }
    public void setTenSanPham(String TenSanPham) { this.TenSanPham = TenSanPham; }

    public double getGia() { return Gia; }
    public void setGia(double Gia) { this.Gia = Gia; }
}