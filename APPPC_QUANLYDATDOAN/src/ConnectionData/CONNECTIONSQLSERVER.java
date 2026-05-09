package ConnectionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CONNECTIONSQLSERVER {
    // 1. Kiểm tra kỹ các thông số này
    private static final String SERVER_NAME = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE_NAME = "dbQuanLyDatDoAn";
    private static final String USER = "sa";
    private static final String PASSWORD = "12345"; // Mật khẩu của bạn

    private static final String URL = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT
            + ";databaseName=" + DATABASE_NAME
            + ";encrypt=true;trustServerCertificate=true;";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("LỖI: Chưa thêm thư viện mssql-jdbc.jar vào Project!");
        } catch (SQLException e) {
            System.err.println("LỖI KẾT NỐI: " + e.getMessage());
            // In chi tiết lỗi để biết sai User, Pass hay Database không tồn tại
        }
        return conn;
    }
}