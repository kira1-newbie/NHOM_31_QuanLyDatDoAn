package BusinessLogic;

import ProcessData.SANPHAMS;
import SCHEMA.SANPHAM;
import java.util.List;

public class blSANPHAM {
    
    private SANPHAMS sanPhamProcess;

    public blSANPHAM() {
        this.sanPhamProcess = new SANPHAMS();
    }

    /**
     * Gọi lớp ProcessData để lấy danh sách lên cho GUI
     */
    public List<SANPHAM> hienThiDanhSach() {
        // Trong tương lai bạn có thể thêm logic kiểm tra ở đây (ví dụ: lọc món theo giá)
        return sanPhamProcess.layDanhSachThucDon();
    }
}