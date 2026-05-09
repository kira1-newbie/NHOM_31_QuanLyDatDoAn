package SCHEMA;

/**
 * Lớp DTO (Data Transfer Object) dùng để chứa dữ liệu gộp
 * hiển thị lên bảng Giỏ Hàng của giao diện.
 */
public class ChiTietGioHangDTO {
    private String sanPhamId;
    private String tenSanPham;
    private double donGia;
    private int soLuong;
    private double thanhTien;

    public ChiTietGioHangDTO() {}

    public ChiTietGioHangDTO(String sanPhamId, String tenSanPham, double donGia, int soLuong, double thanhTien) {
        this.sanPhamId = sanPhamId;
        this.tenSanPham = tenSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    // Các hàm Getters và Setters
    public String getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(String sanPhamId) { this.sanPhamId = sanPhamId; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getThanhTien() { return thanhTien; }
    public void setThanhTien(double thanhTien) { this.thanhTien = thanhTien; }
}