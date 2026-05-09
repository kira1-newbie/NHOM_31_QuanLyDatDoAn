package ProcessData;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.CallableStatement;
import java.sql.Connection;

public class GioHangDAO {

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    public boolean themVaoGioHang(String gioHangId, String sanPhamId, int soLuong) {
        String sql = "{call sp_ThemGioHang(?, ?, ?)}";
        boolean isSuccess = false;

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, gioHangId);
            cs.setString(2, sanPhamId);
            cs.setInt(3, soLuong);

            int rowsAffected = cs.executeUpdate();
            isSuccess = rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
    /**
     * Lấy danh sách chi tiết giỏ hàng kết hợp với thông tin Sản Phẩm
     */
    public java.util.List<SCHEMA.ChiTietGioHangDTO> layDanhSachChiTietGio(String gioHangId) {
        java.util.List<SCHEMA.ChiTietGioHangDTO> list = new java.util.ArrayList<>();

        // Câu lệnh SQL JOIN 2 bảng ChiTietGioHang và SanPham
        String sql = "SELECT ct.SanPhamId, sp.TenSanPham, sp.Gia, ct.SoLuong, (sp.Gia * ct.SoLuong) AS ThanhTien " +
                "FROM ChiTietGioHang ct " +
                "JOIN SanPham sp ON ct.SanPhamId = sp.SanPhamId " +
                "WHERE ct.GioHangId = ?";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, gioHangId);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SCHEMA.ChiTietGioHangDTO dto = new SCHEMA.ChiTietGioHangDTO();
                dto.setSanPhamId(rs.getString("SanPhamId"));
                dto.setTenSanPham(rs.getString("TenSanPham"));
                dto.setDonGia(rs.getDouble("Gia"));
                dto.setSoLuong(rs.getInt("SoLuong"));
                dto.setThanhTien(rs.getDouble("ThanhTien"));

                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public boolean xoaKhoiGioHang(String gioHangId, String sanPhamId) {
        String sql = "DELETE FROM ChiTietGioHang WHERE GioHangId = ? AND SanPhamId = ?";
        boolean isSuccess = false;
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gioHangId);
            ps.setString(2, sanPhamId);
            int rowsAffected = ps.executeUpdate();
            isSuccess = rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * Lấy mã giỏ hàng theo khách hàng (bảng GioHang)
     */
    public String layGioHangIdTheoKhachHang(String khachHangId) {
        String sql = "SELECT GioHangId FROM GioHang WHERE KhachHangId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, khachHangId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("GioHangId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy mã quán hiện tại của giỏ hàng
     */
    public String layQuanAnIdTheoGioHang(String gioHangId) {
        String sql = "SELECT QuanAnId FROM GioHang WHERE GioHangId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gioHangId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("QuanAnId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra giỏ hàng có món nào chưa
     */
    public boolean gioHangCoMon(String gioHangId) {
        String sql = "SELECT TOP 1 1 FROM ChiTietGioHang WHERE GioHangId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gioHangId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cập nhật quán cho giỏ hàng (chỉ nên dùng khi giỏ đang trống)
     */
    public boolean capNhatQuanAnChoGio(String gioHangId, String quanAnId) {
        String sql = "UPDATE GioHang SET QuanAnId = ? WHERE GioHangId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, quanAnId);
            ps.setString(2, gioHangId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Tạo giỏ hàng mới cho khách (nếu chưa tồn tại)
     */
    public String taoGioHangMoi(String gioHangId, String khachHangId, String quanAnId) {
        String sql = "INSERT INTO GioHang (GioHangId, KhachHangId, QuanAnId) VALUES (?, ?, ?)";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gioHangId);
            ps.setString(2, khachHangId);
            ps.setString(3, quanAnId);
            int rows = ps.executeUpdate();
            return rows > 0 ? gioHangId : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}