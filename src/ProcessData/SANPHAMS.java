package ProcessData;

import SCHEMA.SANPHAM;
import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SANPHAMS {

    /**
     * Lấy toàn bộ danh sách món ăn từ CSDL
     */
    public List<SANPHAM> layDanhSachThucDon() {
        List<SANPHAM> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM SanPham";
        
        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
             
            while (rs.next()) {
                // Đọc từng dòng dữ liệu và tạo đối tượng SANPHAM
                SANPHAM sp = new SANPHAM();
                sp.setSanPhamId(rs.getString("SanPhamId"));
                sp.setQuanAnId(rs.getString("QuanAnId"));
                sp.setTenSanPham(rs.getString("TenSanPham"));
                sp.setGia(rs.getDouble("Gia"));
                
                // Thêm vào danh sách
                danhSach.add(sp);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy danh sách sản phẩm: " + e.getMessage());
        }
        return danhSach;
    }
}