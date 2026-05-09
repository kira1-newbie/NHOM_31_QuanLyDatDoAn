package BusinessLogic;

import ProcessData.DonHangDAO;
import java.util.Random;

public class DonHangBLL {
    private DonHangDAO donHangDAO = new DonHangDAO();

    /**
     * Xử lý luồng đặt hàng
     */
    public boolean datHang(String khachHangId, String gioHangId, String diaChiGiao) {
        // 1. Kiểm tra địa chỉ
        if (diaChiGiao == null || diaChiGiao.trim().isEmpty()) {
            System.out.println("Địa chỉ giao hàng không được để trống.");
            return false;
        }

        // 2. Tạo Mã Đơn Hàng tự động (Đảm bảo độ dài 7 ký tự theo cấu trúc DB CHAR(7))
        // Ví dụ: DH + 5 số ngẫu nhiên (DH12345)
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);
        String donHangId = "DH" + randomNumber;

        // 3. Gọi DAO để thực hiện Transaction đặt hàng
        boolean ketQua = donHangDAO.taoDonHang(donHangId, khachHangId, gioHangId, diaChiGiao);

        if (ketQua) {
            System.out.println("Đặt hàng thành công! Mã đơn: " + donHangId);
        } else {
            System.out.println("Đặt hàng thất bại. Vui lòng thử lại.");
        }

        return ketQua;
    }
    /**
     * Xử lý lấy danh sách đơn hàng cho giao diện
     */
    public java.util.List<SCHEMA.DonHang> layLichSuDonHang(String khachHangId) {
        if (khachHangId == null || khachHangId.trim().isEmpty()) {
            System.out.println("Lỗi: Mã khách hàng không hợp lệ.");
            return new java.util.ArrayList<>();
        }
        return donHangDAO.layDanhSachDonHangTheoKhach(khachHangId);
    }
    /**
     * Lấy danh sách đơn hàng cho giao diện Quán ăn
     */
    public java.util.List<SCHEMA.DonHang> layDanhSachDonHangQuanAn(String quanAnId) {
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return donHangDAO.layDanhSachDonHangTheoQuan(quanAnId);
    }

    /**
     * Cập nhật trạng thái đơn hàng
     */
    public boolean capNhatTrangThai(String donHangId, String trangThaiMoi) {
        if (donHangId == null || donHangId.trim().isEmpty()) return false;
        return donHangDAO.capNhatTrangThaiDon(donHangId, trangThaiMoi);
    }
    /**
     * Lấy dữ liệu thống kê kinh doanh cho Quán ăn
     */
    public double[] layThongKeDoanhThu(String quanAnId) {
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            return new double[]{0, 0};
        }
        return donHangDAO.thongKeDoanhThuQuanAn(quanAnId);
    }
}