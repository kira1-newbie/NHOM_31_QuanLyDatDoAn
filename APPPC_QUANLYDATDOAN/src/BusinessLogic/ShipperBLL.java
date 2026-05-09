package BusinessLogic;

import ProcessData.ShipperDAO;
import SCHEMA.DonHang;
import java.util.List;
import java.util.Random;

public class ShipperBLL {
    private ShipperDAO shipperDAO = new ShipperDAO();

    /**
     * Lấy danh sách các đơn hàng khả dụng để giao
     */
    public List<DonHang> layDanhSachDonCho() {
        return shipperDAO.layDanhSachDonChuaGiao();
    }

    /**
     * Lấy danh sách đơn hàng đang giao của shipper hiện tại
     */
    public List<DonHang> layDanhSachDonDangGiao(String shipperId) {
        if (shipperId == null || shipperId.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return shipperDAO.layDanhSachDonDangGiaoCuaShipper(shipperId);
    }

    /**
     * Xử lý nghiệp vụ Shipper nhận đơn
     */
    public boolean nhanDonGiao(String donHangId, String shipperId) {
        if (donHangId == null || donHangId.trim().isEmpty() || shipperId == null) {
            return false;
        }

        // Tạo mã Giao Hàng ngẫu nhiên (Đảm bảo độ dài CHAR(7))
        Random rand = new Random();
        int randomNumber = 10000 + rand.nextInt(90000);
        String giaoHangId = "GH" + randomNumber;

        return shipperDAO.nhanDonHang(giaoHangId, donHangId, shipperId);
    }

    /**
     * Shipper xác nhận đã giao xong
     */
    public boolean xacNhanDaGiao(String donHangId, String shipperId) {
        return shipperDAO.xacNhanDaGiao(donHangId, shipperId);
    }
}