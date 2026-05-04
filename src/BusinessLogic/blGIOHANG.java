package BusinessLogic;

import SCHEMA.CHITIETGIOHANG_DISPLAY;
import java.util.ArrayList;
import java.util.List;

public class blGIOHANG {

    /**
     * Hàm lấy danh sách các món trong giỏ hàng.
     * Tạm thời sử dụng dữ liệu tĩnh (mock data) để bạn xem giao diện.
     * Khi ráp nối SQL, bạn sẽ gọi ProcessData ở đây.
     */
    public List<CHITIETGIOHANG_DISPLAY> layChiTietGioHang() {
        List<CHITIETGIOHANG_DISPLAY> list = new ArrayList<>();
        list.add(new CHITIETGIOHANG_DISPLAY("SP001", "Phở Bò Đặc Biệt", 85000, 2));
        list.add(new CHITIETGIOHANG_DISPLAY("SP002", "Trà Sữa Trân Châu", 45000, 1));
        return list;
    }

    /**
     * Hàm tính tổng tiền của toàn bộ giỏ hàng
     */
    public double tinhTongTien(List<CHITIETGIOHANG_DISPLAY> danhSach) {
        double tong = 0;
        for (CHITIETGIOHANG_DISPLAY item : danhSach) {
            tong += item.getGia() * item.getSoLuong();
        }
        return tong;
    }
}