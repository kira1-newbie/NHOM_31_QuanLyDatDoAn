package BusinessLogic;

import ProcessData.GioHangDAO;

public class GioHangBLL {
    private GioHangDAO gioHangDAO = new GioHangDAO();

    /**
     * Xử lý thêm sản phẩm vào giỏ hàng
     */
    public boolean themVaoGio(String gioHangId, String sanPhamId, int soLuong) {
        // 1. Kiểm tra nghiệp vụ
        if (soLuong <= 0) {
            System.out.println("Số lượng thêm vào giỏ phải lớn hơn 0.");
            return false;
        }

        if (gioHangId == null || sanPhamId == null) {
            System.out.println("Thông tin giỏ hàng hoặc sản phẩm không hợp lệ.");
            return false;
        }

        // 2. Gọi DAO thực thi
        return gioHangDAO.themVaoGioHang(gioHangId, sanPhamId, soLuong);
    }
    public java.util.List<SCHEMA.ChiTietGioHangDTO> layDanhSachChiTietGio(String gioHangId) {
        if (gioHangId == null || gioHangId.trim().isEmpty()) {
            System.out.println("Mã giỏ hàng không hợp lệ.");
            return new java.util.ArrayList<>();
        }
        return gioHangDAO.layDanhSachChiTietGio(gioHangId);
    }

    public boolean xoaKhoiGio(String gioHangId, String sanPhamId) {
        if (gioHangId == null || sanPhamId == null) {
            System.out.println("Thông tin giỏ hàng hoặc sản phẩm không hợp lệ.");
            return false;
        }
        return gioHangDAO.xoaKhoiGioHang(gioHangId, sanPhamId);
    }

    public String layGioHangIdTheoKhachHang(String khachHangId) {
        if (khachHangId == null || khachHangId.trim().isEmpty()) return null;
        return gioHangDAO.layGioHangIdTheoKhachHang(khachHangId);
    }

    public String layQuanAnIdTheoGioHang(String gioHangId) {
        if (gioHangId == null || gioHangId.trim().isEmpty()) return null;
        return gioHangDAO.layQuanAnIdTheoGioHang(gioHangId);
    }

    public boolean gioHangCoMon(String gioHangId) {
        if (gioHangId == null || gioHangId.trim().isEmpty()) return false;
        return gioHangDAO.gioHangCoMon(gioHangId);
    }

    public boolean capNhatQuanAnChoGio(String gioHangId, String quanAnId) {
        if (gioHangId == null || quanAnId == null) return false;
        return gioHangDAO.capNhatQuanAnChoGio(gioHangId, quanAnId);
    }

    public String taoGioHangMoi(String khachHangId, String quanAnId) {
        if (khachHangId == null || quanAnId == null) return null;
        java.util.Random random = new java.util.Random();
        String gioHangId = "GH" + (10000 + random.nextInt(90000)); // GH12345
        return gioHangDAO.taoGioHangMoi(gioHangId, khachHangId, quanAnId);
    }
}