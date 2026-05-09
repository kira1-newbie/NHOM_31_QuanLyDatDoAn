package ProcessData;

import ConnectionData.CONNECTIONSQLSERVER;
import SCHEMA.SanPham;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SanPhamDAO {

    /**
     * Lấy danh sách sản phẩm đang còn hàng
     */
    public List<SanPham> layDanhSachSanPham() {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE TrangThai = N'CÒN HÀNG'";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setSanPhamId(rs.getString("SanPhamId"));
                sp.setQuanAnId(rs.getString("QuanAnId"));
                sp.setDanhMucId(rs.getInt("DanhMucId"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setHinhAnh(rs.getString("HinhAnh"));
                sp.setGia(rs.getDouble("Gia"));
                sp.setTrangThai(rs.getString("TrangThai"));

                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy danh sách sản phẩm theo mã quán ăn (KHÔNG lọc trạng thái).
     * Dùng cho giao diện quản lý của quán: vẫn hiển thị cả món HẾT HÀNG.
     */
    public List<SanPham> layDanhSachSanPhamTheoQuanTatCa(String quanAnId) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE QuanAnId = ? ORDER BY SanPhamId";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quanAnId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setSanPhamId(rs.getString("SanPhamId"));
                    sp.setQuanAnId(rs.getString("QuanAnId"));
                    sp.setDanhMucId(rs.getInt("DanhMucId"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setGia(rs.getDouble("Gia"));
                    sp.setTrangThai(rs.getString("TrangThai"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Lấy danh sách sản phẩm theo mã quán ăn
     */
    public List<SanPham> layDanhSachSanPhamTheoQuan(String quanAnId) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SanPham WHERE QuanAnId = ? AND TrangThai = N'CÒN HÀNG'";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quanAnId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setSanPhamId(rs.getString("SanPhamId"));
                    sp.setQuanAnId(rs.getString("QuanAnId"));
                    sp.setDanhMucId(rs.getInt("DanhMucId"));
                    sp.setTenSanPham(rs.getString("TenSanPham"));
                    sp.setHinhAnh(rs.getString("HinhAnh"));
                    sp.setGia(rs.getDouble("Gia"));
                    sp.setTrangThai(rs.getString("TrangThai"));

                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /**
     * Thêm một món ăn mới vào CSDL
     */
    public boolean themSanPham(SCHEMA.SanPham sp) {
        String sql = "INSERT INTO SanPham (SanPhamId, QuanAnId, DanhMucId, TenSanPham, Gia, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getSanPhamId());
            ps.setString(2, sp.getQuanAnId());
            ps.setInt(3, sp.getDanhMucId()); // Mặc định 1 (Cơm) để đơn giản
            ps.setString(4, sp.getTenSanPham());
            ps.setDouble(5, sp.getGia());
            ps.setString(6, sp.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin món ăn (Giá, Tên, Trạng thái)
     */
    public boolean capNhatSanPham(SCHEMA.SanPham sp) {
        String sql = "UPDATE SanPham SET TenSanPham = ?, Gia = ?, TrangThai = ? WHERE SanPhamId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sp.getTenSanPham());
            ps.setDouble(2, sp.getGia());
            ps.setString(3, sp.getTrangThai());
            ps.setString(4, sp.getSanPhamId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa món ăn khỏi CSDL
     */
    public boolean xoaSanPham(String sanPhamId) {
        String sql = "DELETE FROM SanPham WHERE SanPhamId = ?";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sanPhamId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}