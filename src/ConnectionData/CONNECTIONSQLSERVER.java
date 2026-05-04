package ConnectionData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp chịu trách nhiệm tạo kết nối từ ứng dụng Java đến CSDL SQL Server.
 * @author Acer
 */
public class CONNECTIONSQLSERVER {
    
    // 1. Khai báo thông tin kết nối (Các tham số này có thể điều chỉnh)
    // - localhost:1433 là địa chỉ máy chủ và cổng mặc định của SQL Server.
    // - databaseName=QuanLyDatDoAn là tên CSDL của chúng ta.
    // - encrypt=false giúp tránh lỗi chứng chỉ (SSL) khi chạy trên máy cá nhân.
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=dbQuanLyDatDoAn;encrypt=false";
    
    // 2. Tài khoản đăng nhập SQL Server
    private static final String USER = "sa"; 
    
    // 3. Mật khẩu đăng nhập SQL Server (Thay đổi '123' thành mật khẩu thực tế máy bạn)
    private static final String PASSWORD = ""; 

    /**
     * Hàm mở kết nối đến SQL Server.
     * @return Đối tượng Connection chứa đường truyền đến CSDL, hoặc null nếu lỗi.
     * @throws SQLException nếu có lỗi xảy ra trong quá trình kết nối.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            // Thực hiện kết nối bằng DriverManager của thư viện JDBC
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // Bỏ ghi chú dòng dưới đây nếu bạn muốn in ra console để kiểm tra kết nối thành công
             System.out.println("Kết nối CSDL thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối CSDL: " + e.getMessage());
            // Ném lỗi ra ngoài để lớp gọi (ví dụ: NGUOIDUNGS) biết mà xử lý
            throw e; 
        }
        return conn;
    }
    
    // Hàm main nhỏ để bạn có thể chạy thử trực tiếp tệp này xem kết nối OK chưa
    public static void main(String[] args) {
        try {
            Connection testConn = CONNECTIONSQLSERVER.getConnection();
            if (testConn != null) {
                System.out.println("Chúc mừng! Bạn đã kết nối thành công tới Database QuanLyDatDoAn.");
                testConn.close(); // Nhớ đóng kết nối sau khi test xong
            }
        } catch (SQLException ex) {
            System.out.println("Kết nối thất bại. Vui lòng kiểm tra lại SQL Server, Mật khẩu hoặc thư viện JDBC.");
        }
    }
}