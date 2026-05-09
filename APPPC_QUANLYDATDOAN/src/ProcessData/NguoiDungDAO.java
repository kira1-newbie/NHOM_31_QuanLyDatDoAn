package ProcessData;

import ConnectionData.CONNECTIONSQLSERVER;
import SCHEMA.NguoiDung;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

public class NguoiDungDAO {

    /**
     * Gọi SP lấy thông tin người dùng theo Email
     */
    public NguoiDung layNguoiDungTheoEmail(String email) {
        NguoiDung nd = null;
        String sql = "{call sp_LayNguoiDungTheoEmail(?)}";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, email);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                nd = new NguoiDung();
                nd.setNguoiDungId(rs.getString("NguoiDungId"));
                nd.setRoleId(rs.getInt("RoleId"));
                nd.setHoTen(rs.getString("HoTen"));
                nd.setMatKhau(rs.getString("MatKhau")); // Lấy hash về cho BLL kiểm tra
                nd.setTrangThai(rs.getString("TrangThai"));
                // Ghi chú: SP của chúng ta hiện chỉ select 5 cột này,
                // nếu cần thêm cột, bạn hãy cập nhật lại câu Select trong SP nhé.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nd;
    }
    /**
     * Lấy danh sách toàn bộ người dùng trong hệ thống
     */
    public java.util.List<SCHEMA.NguoiDung> layDanhSachNguoiDung() {
        java.util.List<SCHEMA.NguoiDung> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM NguoiDung ORDER BY NguoiDungId ASC";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SCHEMA.NguoiDung nd = new SCHEMA.NguoiDung();
                nd.setNguoiDungId(rs.getString("NguoiDungId"));
                nd.setRoleId(rs.getInt("RoleId"));
                nd.setHoTen(rs.getString("HoTen"));
                nd.setEmail(rs.getString("Email"));
                nd.setSoDienThoai(rs.getString("SoDienThoai"));
                nd.setTrangThai(rs.getString("TrangThai"));
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách các quán ăn (RoleId = 4 và đang hoạt động)
     */
    public java.util.List<SCHEMA.NguoiDung> layDanhSachQuanAn() {
        java.util.List<SCHEMA.NguoiDung> list = new java.util.ArrayList<>();
        String sql = "SELECT ND.NguoiDungId, ND.RoleId, QA.TenQuan AS HoTen, ND.Email, ND.SoDienThoai, ND.TrangThai " +
                "FROM NguoiDung ND " +
                "INNER JOIN QuanAn QA ON ND.NguoiDungId = QA.ChuQuanId " +
                "WHERE ND.RoleId = 4 AND ND.TrangThai = N'HOẠT ĐỘNG' " +
                "ORDER BY QA.TenQuan ASC";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql);
             java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SCHEMA.NguoiDung nd = new SCHEMA.NguoiDung();
                nd.setNguoiDungId(rs.getString("NguoiDungId"));
                nd.setRoleId(rs.getInt("RoleId"));
                nd.setHoTen(rs.getString("HoTen"));
                nd.setEmail(rs.getString("Email"));
                nd.setSoDienThoai(rs.getString("SoDienThoai"));
                nd.setTrangThai(rs.getString("TrangThai"));
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật trạng thái của người dùng (HOẠT ĐỘNG <-> BỊ KHÓA)
     */
    public boolean capNhatTrangThai(String nguoiDungId, String trangThaiMoi) {
        String sql = "UPDATE NguoiDung SET TrangThai = ? WHERE NguoiDungId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, trangThaiMoi);
            ps.setString(2, nguoiDungId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Kiểm tra xem Email đã được đăng ký trong hệ thống chưa
     */
    public boolean kiemTraEmailTonTai(String email) {
        String sql = "SELECT 1 FROM NguoiDung WHERE Email = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            java.sql.ResultSet rs = ps.executeQuery();
            return rs.next(); // Trả về true nếu đã có dữ liệu (Email đã tồn tại)

        } catch (Exception e) {
            e.printStackTrace();
            return true; // Trả về true để an toàn (chặn đăng ký nếu lỗi DB)
        }
    }

    /**
     * Lưu thông tin người dùng mới vào cơ sở dữ liệu
     */
    public boolean themNguoiDung(SCHEMA.NguoiDung user) {
        String sql = "INSERT INTO NguoiDung (NguoiDungId, RoleId, HoTen, Email, SoDienThoai, MatKhau, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNguoiDungId());
            ps.setInt(2, user.getRoleId());
            ps.setString(3, user.getHoTen());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getSoDienThoai());
            ps.setString(6, user.getMatKhau());
            ps.setString(7, user.getTrangThai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String layQuanAnId(String chuQuanId) {
        String sql = "SELECT QuanAnId FROM QuanAn WHERE ChuQuanId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, chuQuanId);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("QuanAnId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}