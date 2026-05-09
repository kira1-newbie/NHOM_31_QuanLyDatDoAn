package ProcessData;

import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.CallableStatement;
import java.sql.Connection;

public class DonHangDAO {

    /**
     * Tạo đơn hàng từ giỏ hàng hiện tại
     */
    public boolean taoDonHang(String donHangId, String khachHangId, String gioHangId, String diaChiGiao) {
        String sql = "{call sp_TaoDonHang(?, ?, ?, ?)}";
        boolean isSuccess = false;

        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, donHangId);
            cs.setString(2, khachHangId);
            cs.setString(3, gioHangId);
            cs.setString(4, diaChiGiao);

            // executeUpdate trả về số dòng bị ảnh hưởng (nếu > 0 nghĩa là đã insert thành công)
            cs.executeUpdate();
            isSuccess = true; // Nếu không có Exception ném ra từ SP (Transaction an toàn) thì xem như thành công

        } catch (Exception e) {
            System.err.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return isSuccess;
    }
    /**
     * Lấy danh sách lịch sử đơn hàng của một khách hàng cụ thể
     * @param khachHangId Mã của khách hàng đang đăng nhập
     * @return Danh sách các đối tượng DonHang
     */
    public java.util.List<SCHEMA.DonHang> layDanhSachDonHangTheoKhach(String khachHangId) {
        java.util.List<SCHEMA.DonHang> list = new java.util.ArrayList<>();

        // Lấy dữ liệu và sắp xếp đơn hàng mới nhất lên đầu
        String sql = "SELECT DonHangId, KhachHangId, QuanAnId, DiaChiGiao, TongTien, TrangThai, ThoiGianDat " +
                "FROM DonHang WHERE KhachHangId = ? ORDER BY ThoiGianDat DESC";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, khachHangId);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SCHEMA.DonHang dh = new SCHEMA.DonHang();
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
     * Lấy danh sách các đơn hàng được đặt tại một quán cụ thể
     */
    public java.util.List<SCHEMA.DonHang> layDanhSachDonHangTheoQuan(String quanAnId) {
        java.util.List<SCHEMA.DonHang> list = new java.util.ArrayList<>();
        String sql = "SELECT * FROM DonHang WHERE QuanAnId = ? ORDER BY ThoiGianDat DESC";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quanAnId);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                SCHEMA.DonHang dh = new SCHEMA.DonHang();
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
     * Cập nhật trạng thái của đơn hàng (Dùng chung cho Quán ăn và Shipper)
     */
    public boolean capNhatTrangThaiDon(String donHangId, String trangThaiMoi) {
        String sql = "{call sp_CapNhatTrangThaiDon(?, ?)}";
        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, donHangId);
            cs.setString(2, trangThaiMoi);
            return cs.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Lỗi cập nhật trạng thái đơn: " + e.getMessage());
            return false;
        }
    }
    /**
     * Thống kê tổng số đơn và doanh thu của một Quán ăn
     * Chỉ tính các đơn hàng ở trạng thái HOÀN THÀNH
     * @return mảng double: index 0 là Tổng số đơn, index 1 là Tổng doanh thu
     */
    public double[] thongKeDoanhThuQuanAn(String quanAnId) {
        double[] ketQua = new double[2]; // [0] = Số đơn, [1] = Doanh thu
        ketQua[0] = 0;
        ketQua[1] = 0;

        String sql = "SELECT COUNT(DonHangId) AS TongSoDon, SUM(TongTien) AS TongDoanhThu " +
                "FROM DonHang WHERE QuanAnId = ? AND TrangThai = N'HOÀN THÀNH'";

        try (java.sql.Connection conn = ConnectionData.CONNECTIONSQLSERVER.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, quanAnId);
            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ketQua[0] = rs.getDouble("TongSoDon");
                ketQua[1] = rs.getDouble("TongDoanhThu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ketQua;
    }
}