package ProcessData;

import ConnectionData.CONNECTIONSQLSERVER;
import SCHEMA.DonHang;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ShipperDAO {

    /**
     * Lấy danh sách các đơn hàng chưa có người giao
     */
    public List<DonHang> layDanhSachDonChuaGiao() {
        List<DonHang> list = new ArrayList<>();
        // Chỉ lấy các đơn đã được quán xác nhận và đang chuẩn bị
        String sql = "SELECT * FROM DonHang WHERE TrangThai = N'ĐANG CHUẨN BỊ' ORDER BY ThoiGianDat ASC";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DonHang dh = new DonHang();
                dh.setDonHangId(rs.getString("DonHangId"));
                dh.setKhachHangId(rs.getString("KhachHangId"));
                dh.setQuanAnId(rs.getString("QuanAnId"));
                dh.setDiaChiGiao(rs.getString("DiaChiGiao"));
                dh.setTongTien(rs.getDouble("TongTien"));
                dh.setTrangThai(rs.getString("TrangThai"));
                dh.setThoiGianDat(rs.getTimestamp("ThoiGianDat"));

                list.add(dh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Gọi thủ tục cập nhật Shipper nhận đơn
     */
    public boolean nhanDonHang(String giaoHangId, String donHangId, String shipperId) {
        String sql = "{call sp_ShipperNhanDon(?, ?, ?)}";
        boolean isSuccess = false;

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, giaoHangId);
            cs.setString(2, donHangId);
            cs.setString(3, shipperId);

            cs.executeUpdate();
            isSuccess = true;

        } catch (Exception e) {
            System.err.println("Lỗi khi Shipper nhận đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return isSuccess;
    }

    /**
     * Lấy danh sách các đơn hàng đang giao của 1 shipper
     */
    public List<DonHang> layDanhSachDonDangGiaoCuaShipper(String shipperId) {
        List<DonHang> list = new ArrayList<>();
        String sql = "SELECT dh.* " +
                "FROM DonHang dh " +
                "JOIN GiaoHang gh ON gh.DonHangId = dh.DonHangId " +
                "WHERE dh.TrangThai = N'ĐANG GIAO' AND gh.ShipperId = ? " +
                "ORDER BY dh.ThoiGianDat DESC";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, shipperId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DonHang dh = new DonHang();
                    dh.setDonHangId(rs.getString("DonHangId"));
                    dh.setKhachHangId(rs.getString("KhachHangId"));
                    dh.setQuanAnId(rs.getString("QuanAnId"));
                    dh.setDiaChiGiao(rs.getString("DiaChiGiao"));
                    dh.setTongTien(rs.getDouble("TongTien"));
                    dh.setTrangThai(rs.getString("TrangThai"));
                    dh.setThoiGianDat(rs.getTimestamp("ThoiGianDat"));
                    list.add(dh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Shipper xác nhận đã giao xong đơn (ĐANG GIAO -> HOÀN THÀNH)
     */
    public boolean xacNhanDaGiao(String donHangId, String shipperId) {
        if (donHangId == null || donHangId.trim().isEmpty() || shipperId == null || shipperId.trim().isEmpty()) {
            return false;
        }

        String sqlUpdateDon = "UPDATE DonHang SET TrangThai = N'HOÀN THÀNH' WHERE DonHangId = ? AND TrangThai = N'ĐANG GIAO'";
        String sqlUpdateGiao = "UPDATE GiaoHang SET TrangThaiGiao = N'HOÀN THÀNH' WHERE DonHangId = ? AND ShipperId = ?";

        try (Connection conn = CONNECTIONSQLSERVER.getConnection()) {
            conn.setAutoCommit(false);

            int affectedDon;
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateDon)) {
                ps.setString(1, donHangId);
                affectedDon = ps.executeUpdate();
            }

            int affectedGiao;
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateGiao)) {
                ps.setString(1, donHangId);
                ps.setString(2, shipperId);
                affectedGiao = ps.executeUpdate();
            }

            if (affectedDon > 0 && affectedGiao > 0) {
                conn.commit();
                return true;
            }

            conn.rollback();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}