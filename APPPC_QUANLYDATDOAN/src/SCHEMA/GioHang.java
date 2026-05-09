package SCHEMA;

public class GioHang {
    private String gioHangId;
    private String khachHangId;
    private String quanAnId;

    public GioHang() {}

    public GioHang(String gioHangId, String khachHangId, String quanAnId) {
        this.gioHangId = gioHangId;
        this.khachHangId = khachHangId;
        this.quanAnId = quanAnId;
    }

    public String getGioHangId() { return gioHangId; }
    public void setGioHangId(String gioHangId) { this.gioHangId = gioHangId; }

    public String getKhachHangId() { return khachHangId; }
    public void setKhachHangId(String khachHangId) { this.khachHangId = khachHangId; }

    public String getQuanAnId() { return quanAnId; }
    public void setQuanAnId(String quanAnId) { this.quanAnId = quanAnId; }
}