package SCHEMA;

/**
 * Lớp này lưu trữ thông tin chi tiết giỏ hàng kết hợp với Tên và Giá sản phẩm để hiển thị.
 */
public class CHITIETGIOHANG_DISPLAY {
    private String SanPhamId;
    private String TenSanPham;
    private double Gia;
    private int SoLuong;

    public CHITIETGIOHANG_DISPLAY(String SanPhamId, String TenSanPham, double Gia, int SoLuong) {
        this.SanPhamId = SanPhamId;
        this.TenSanPham = TenSanPham;
        this.Gia = Gia;
        this.SoLuong = SoLuong;
    }

    public String getSanPhamId() { return SanPhamId; }
    public String getTenSanPham() { return TenSanPham; }
    public double getGia() { return Gia; }
    public int getSoLuong() { return SoLuong; }
    public void setSoLuong(int SoLuong) { this.SoLuong = SoLuong; }
}