package ProcessData;

import SCHEMA.NGUOIDUNG;
import ConnectionData.CONNECTIONSQLSERVER;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NGUOIDUNGS {

    public NGUOIDUNG dangNhap(String email, String matKhau) {
        String sql = "SELECT * FROM NguoiDung WHERE Email = ? AND MatKhau = ?";
        
        try (Connection conn = CONNECTIONSQLSERVER.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, email);
            ps.setString(2, matKhau);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NGUOIDUNG(
                        rs.getString("NguoiDungId"),
                        rs.getString("HoTen"),
                        rs.getString("Email"),
                        rs.getString("SoDienThoai"),
                        rs.getString("MatKhau"),
                        rs.getString("Role")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}