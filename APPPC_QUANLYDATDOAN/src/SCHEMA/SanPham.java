package SCHEMA;

public class SanPham {
    private String sanPhamId;
    private String quanAnId;
    private int danhMucId;
    private String tenSanPham;
    private String hinhAnh;
    private double gia;
    private String trangThai;

    public SanPham() {}

    public SanPham(String sanPhamId, String quanAnId, int danhMucId, String tenSanPham, String hinhAnh, double gia, String trangThai) {
        this.sanPhamId = sanPhamId;
        this.quanAnId = quanAnId;
        this.danhMucId = danhMucId;
        this.tenSanPham = tenSanPham;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    public String getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(String sanPhamId) { this.sanPhamId = sanPhamId; }

    public String getQuanAnId() { return quanAnId; }
    public void setQuanAnId(String quanAnId) { this.quanAnId = quanAnId; }

    public int getDanhMucId() { return danhMucId; }
    public void setDanhMucId(int danhMucId) { this.danhMucId = danhMucId; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public double getGia() { return gia; }
    public void setGia(double gia) { this.gia = gia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}