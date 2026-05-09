package BusinessLogic;

import ProcessData.NguoiDungDAO;
import SCHEMA.NguoiDung;

public class NguoiDungBLL {
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    /**
     * Xử lý đăng nhập
     * Trả về đối tượng NguoiDung nếu thành công, trả về null nếu thất bại
     */
    public NguoiDung dangNhap(String email, String matKhauNhap) {
        // 1. Kiểm tra đầu vào cơ bản
        if (email == null || email.trim().isEmpty() || matKhauNhap == null || matKhauNhap.trim().isEmpty()) {
            System.out.println("Email hoặc mật khẩu không được để trống.");
            return null;
        }

        // 2. Gọi DAO lấy thông tin user từ Database
        NguoiDung user = nguoiDungDAO.layNguoiDungTheoEmail(email);

        // 3. Kiểm tra user có tồn tại và khớp mật khẩu không
        if (user != null) {
            // Lưu ý: Để code chạy ngay không cần thư viện ngoài, ở đây dùng so sánh chuỗi cơ bản.
            // Nếu bạn dùng thư viện BCrypt, hãy thay bằng: BCrypt.checkpw(matKhauNhap, user.getMatKhau())
            if (user.getMatKhau().equals(matKhauNhap)) {
                return user; // Đăng nhập thành công
            } else {
                System.out.println("Sai mật khẩu.");
            }
        } else {
            System.out.println("Tài khoản không tồn tại hoặc đã bị khóa.");
        }

        return null; // Đăng nhập thất bại
    }
    /**
     * Lấy danh sách tài khoản cho giao diện Admin
     */
    public java.util.List<SCHEMA.NguoiDung> layDanhSachTaiKhoan() {
        return nguoiDungDAO.layDanhSachNguoiDung();
    }

    /**
     * Lấy danh sách các quán ăn đang hoạt động
     */
    public java.util.List<SCHEMA.NguoiDung> layDanhSachQuanAn() {
        return nguoiDungDAO.layDanhSachQuanAn();
    }

    /**
     * Logic đảo ngược trạng thái tài khoản
     */
    public boolean daoNguocTrangThai(String nguoiDungId, String trangThaiHienTai) {
        if (nguoiDungId == null || nguoiDungId.trim().isEmpty()) return false;

        // Nếu đang hoạt động thì khóa, nếu đang khóa thì mở lại
        String trangThaiMoi = trangThaiHienTai.equals("HOẠT ĐỘNG") ? "BỊ KHÓA" : "HOẠT ĐỘNG";

        return nguoiDungDAO.capNhatTrangThai(nguoiDungId, trangThaiMoi);
    }
    /**
     * Xử lý logic đăng ký tài khoản Khách Hàng mới
     */
    public boolean dangKyKhachHang(String hoTen, String email, String sdt, String matKhau, String nhapLaiMK) {
        // 1. Kiểm tra rỗng
        if (hoTen.isEmpty() || email.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
            System.out.println("Vui lòng điền đầy đủ thông tin.");
            return false;
        }

        // 2. Kiểm tra mật khẩu khớp nhau
        if (!matKhau.equals(nhapLaiMK)) {
            System.out.println("Mật khẩu nhập lại không khớp.");
            return false;
        }

        // 3. Kiểm tra Email tồn tại
        if (nguoiDungDAO.kiemTraEmailTonTai(email)) {
            System.out.println("Email này đã được sử dụng.");
            return false;
        }

        // 4. Tạo đối tượng và dữ liệu mặc định
        SCHEMA.NguoiDung newUser = new SCHEMA.NguoiDung();

        // Tạo ID ngẫu nhiên: USR + 4 số (Ví dụ: USR5678)
        int randomNum = 1000 + new java.util.Random().nextInt(9000);
        newUser.setNguoiDungId("USR" + randomNum);

        newUser.setRoleId(2); // 2 = Mặc định là Khách Hàng (CUSTOMER)
        newUser.setHoTen(hoTen);
        newUser.setEmail(email);
        newUser.setSoDienThoai(sdt);
        newUser.setMatKhau(matKhau); // Lưu ý: Ở hệ thống thực tế cần mã hóa (Hash) mật khẩu tại đây
        newUser.setTrangThai("HOẠT ĐỘNG");

        // 5. Gọi DAO lưu vào CSDL
        return nguoiDungDAO.themNguoiDung(newUser);
    }
}