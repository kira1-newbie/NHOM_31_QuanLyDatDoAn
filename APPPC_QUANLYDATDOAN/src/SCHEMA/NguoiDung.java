package SCHEMA;

public class NguoiDung {
    private String nguoiDungId;
    private int roleId;
    private String hoTen;
    private String email;
    private String soDienThoai;
    private String matKhau;
    private String trangThai;

    public NguoiDung() {}

    public NguoiDung(String nguoiDungId, int roleId, String hoTen, String email, String soDienThoai, String matKhau, String trangThai) {
        this.nguoiDungId = nguoiDungId;
        this.roleId = roleId;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
    }

    // Các hàm Getters và Setters
    public String getNguoiDungId() { return nguoiDungId; }
    public void setNguoiDungId(String nguoiDungId) { this.nguoiDungId = nguoiDungId; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}