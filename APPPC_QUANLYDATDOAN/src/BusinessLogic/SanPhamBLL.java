package BusinessLogic;

import ProcessData.SanPhamDAO;
import SCHEMA.SanPham;
import java.util.List;

public class SanPhamBLL {
    private SanPhamDAO sanPhamDAO = new SanPhamDAO();

    /**
     * Lấy toàn bộ danh sách sản phẩm để hiển thị lên giao diện
     */
    public List<SanPham> layDanhSachMenu() {
        // Có thể thêm logic kiểm tra phân quyền hoặc cache dữ liệu ở đây nếu cần
        return sanPhamDAO.layDanhSachSanPham();
    }
    /**
     * Lấy danh sách sản phẩm theo mã quán ăn
     */
    public List<SanPham> layDanhSachSanPhamTheoQuan(String quanAnId) {
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return sanPhamDAO.layDanhSachSanPhamTheoQuan(quanAnId);
    }

    /**
     * Dành cho giao diện quản lý của quán: lấy tất cả món (kể cả HẾT HÀNG).
     */
    public List<SanPham> layDanhSachSanPhamTheoQuanTatCa(String quanAnId) {
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return sanPhamDAO.layDanhSachSanPhamTheoQuanTatCa(quanAnId);
    }

    /**
     * Kiểm tra dữ liệu và thêm sản phẩm
     */
    public boolean themSanPham(SCHEMA.SanPham sp) {
        if (sp.getTenSanPham() == null || sp.getTenSanPham().trim().isEmpty()) {
            System.out.println("Tên sản phẩm không được để trống!");
            return false;
        }
        if (sp.getGia() < 0) {
            System.out.println("Giá sản phẩm phải lớn hơn hoặc bằng 0!");
            return false;
        }
        return sanPhamDAO.themSanPham(sp);
    }

    public boolean capNhatSanPham(SCHEMA.SanPham sp) {
        if (sp.getSanPhamId() == null || sp.getSanPhamId().trim().isEmpty()) return false;
        return sanPhamDAO.capNhatSanPham(sp);
    }

    public boolean xoaSanPham(String sanPhamId) {
        if (sanPhamId == null || sanPhamId.trim().isEmpty()) return false;
        return sanPhamDAO.xoaSanPham(sanPhamId);
    }
}