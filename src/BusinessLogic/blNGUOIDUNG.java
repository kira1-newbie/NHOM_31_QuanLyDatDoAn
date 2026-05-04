package BusinessLogic;

import ProcessData.NGUOIDUNGS;
import SCHEMA.NGUOIDUNG;

public class blNGUOIDUNG {
    
    private NGUOIDUNGS nguoiDungProcess;

    public blNGUOIDUNG() {
        this.nguoiDungProcess = new NGUOIDUNGS();
    }

    public NGUOIDUNG kiemTraDangNhap(String email, String matKhau) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("Bạn chưa nhập Email!");
        }
        if (matKhau == null || matKhau.trim().isEmpty()) {
            throw new Exception("Bạn chưa nhập Mật khẩu!");
        }
        
        return nguoiDungProcess.dangNhap(email, matKhau);
    }
}