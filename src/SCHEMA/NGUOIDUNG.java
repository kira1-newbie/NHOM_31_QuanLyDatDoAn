package SCHEMA;

public class NGUOIDUNG {
    
    private String NguoiDungId;
    private String HoTen;
    private String Email;
    private String SoDienThoai;
    private String MatKhau;
    private String Role;

    public NGUOIDUNG() {
    }

    public NGUOIDUNG(String NguoiDungId, String HoTen, String Email, String SoDienThoai, String MatKhau, String Role) {
        this.NguoiDungId = NguoiDungId;
        this.HoTen = HoTen;
        this.Email = Email;
        this.SoDienThoai = SoDienThoai;
        this.MatKhau = MatKhau;
        this.Role = Role;
    }

    public String getNguoiDungId() { return NguoiDungId; }
    public void setNguoiDungId(String NguoiDungId) { this.NguoiDungId = NguoiDungId; }

    public String getHoTen() { return HoTen; }
    public void setHoTen(String HoTen) { this.HoTen = HoTen; }

    public String getEmail() { return Email; }
    public void setEmail(String Email) { this.Email = Email; }

    public String getSoDienThoai() { return SoDienThoai; }
    public void setSoDienThoai(String SoDienThoai) { this.SoDienThoai = SoDienThoai; }

    public String getMatKhau() { return MatKhau; }
    public void setMatKhau(String MatKhau) { this.MatKhau = MatKhau; }

    public String getRole() { return Role; }
    public void setRole(String Role) { this.Role = Role; }
}