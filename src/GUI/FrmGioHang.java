package GUI;

import BusinessLogic.blGIOHANG;
import SCHEMA.CHITIETGIOHANG_DISPLAY;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Giao diện quản lý Giỏ Hàng của khách hàng
 * @author Acer
 */
public class FrmGioHang extends JFrame {

    private blGIOHANG gioHangBLL;
    private Color colorPrimary = new Color(238, 77, 45); 
    private JPanel pnlDanhSachMon; 
    private JLabel lblTongTien; 

    public FrmGioHang() {
        // Khởi tạo tầng Business Logic
        gioHangBLL = new blGIOHANG();

        // Thiết lập các thuộc tính cơ bản của Form
        setTitle("FoodApp - Giỏ Hàng Của Bạn");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // 1. Khu vực Tiêu đề (Header)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("🛒 GIỎ HÀNG CỦA BẠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(colorPrimary);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Khu vực Danh sách món ăn (Center)
        pnlDanhSachMon = new JPanel();
        pnlDanhSachMon.setLayout(new BoxLayout(pnlDanhSachMon, BoxLayout.Y_AXIS));
        pnlDanhSachMon.setBackground(new Color(245, 245, 245));
        pnlDanhSachMon.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Bọc danh sách vào thanh cuộn
        JScrollPane scrollPane = new JScrollPane(pnlDanhSachMon);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Khu vực Thanh toán (Footer)
        add(taoThanhThanhToan(), BorderLayout.SOUTH);

        // 4. Lấy dữ liệu và hiển thị lên giao diện
        taiDuLieuGioHang();
    }

    // Hàm thiết kế khu vực Tổng tiền và Nút Đặt hàng
    private JPanel taoThanhThanhToan() {
        JPanel pnlFooter = new JPanel(new BorderLayout());
        pnlFooter.setBackground(Color.WHITE);
        pnlFooter.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Cụm hiển thị tổng tiền
        JPanel pnlTongTien = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTongTien.setBackground(Color.WHITE);
        JLabel lblChuTongTien = new JLabel("Tổng thanh toán: ");
        lblChuTongTien.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        lblTongTien = new JLabel("0 đ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongTien.setForeground(colorPrimary);
        
        pnlTongTien.add(lblChuTongTien);
        pnlTongTien.add(lblTongTien);

        // Nút tiến hành đặt hàng
        JButton btnDatHang = new JButton("Đặt Hàng Ngay");
        btnDatHang.setPreferredSize(new Dimension(160, 45));
        btnDatHang.setBackground(colorPrimary);
        btnDatHang.setForeground(Color.WHITE);
        btnDatHang.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDatHang.setFocusPainted(false);
        btnDatHang.setBorderPainted(false);
        btnDatHang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Sự kiện khi bấm đặt hàng
        btnDatHang.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang tiến hành tạo đơn hàng...");
            // TODO: Viết logic lưu đơn hàng vào CSDL
        });

        pnlFooter.add(pnlTongTien, BorderLayout.WEST);
        pnlFooter.add(btnDatHang, BorderLayout.EAST);
        
        // Tạo đường viền phân cách phía trên
        pnlFooter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        return pnlFooter;
    }

    // Hàm gọi Business Logic để lấy dữ liệu
    private void taiDuLieuGioHang() {
        // Xóa sạch giao diện cũ trước khi tải mới
        pnlDanhSachMon.removeAll();
        
        List<CHITIETGIOHANG_DISPLAY> danhSach = gioHangBLL.layChiTietGioHang();
        
        // Lặp qua từng món và vẽ lên màn hình
        for (CHITIETGIOHANG_DISPLAY item : danhSach) {
            pnlDanhSachMon.add(taoItemGioHang(item));
            pnlDanhSachMon.add(Box.createRigidArea(new Dimension(0, 12))); 
        }
        
        // Cập nhật nhãn tổng tiền
        double tong = gioHangBLL.tinhTongTien(danhSach);
        lblTongTien.setText(String.format("%,.0f đ", tong));
        
        // Cập nhật lại giao diện (refresh)
        pnlDanhSachMon.revalidate();
        pnlDanhSachMon.repaint();
    }

    // Hàm thiết kế chi tiết 1 dòng món ăn trong giỏ
    private JPanel taoItemGioHang(CHITIETGIOHANG_DISPLAY item) {
        RoundedPanel pnlItem = new RoundedPanel(15, Color.WHITE);
        pnlItem.setLayout(new BorderLayout());
        pnlItem.setMaximumSize(new Dimension(500, 85));
        pnlItem.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Thông tin Tên và Giá
        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        
        JLabel lblTen = new JLabel(item.getTenSanPham());
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblGia = new JLabel(String.format("%,.0f đ", item.getGia()));
        lblGia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblGia.setForeground(Color.GRAY);
        
        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(lblGia);

        // Khu vực điều chỉnh số lượng
        JPanel pnlSoLuong = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        pnlSoLuong.setOpaque(false);
        
        JButton btnGiam = new JButton("-");
        JLabel lblSoLuong = new JLabel(String.valueOf(item.getSoLuong()), SwingConstants.CENTER);
        JButton btnTang = new JButton("+");
        
        dinhDangNutSoLuong(btnGiam);
        dinhDangNutSoLuong(btnTang);
        lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSoLuong.setPreferredSize(new Dimension(35, 35));

        pnlSoLuong.add(btnGiam);
        pnlSoLuong.add(lblSoLuong);
        pnlSoLuong.add(btnTang);

        pnlItem.add(pnlInfo, BorderLayout.CENTER);
        pnlItem.add(pnlSoLuong, BorderLayout.EAST);

        return pnlItem;
    }

    // Hàm phụ trợ làm đẹp nút tăng giảm
    private void dinhDangNutSoLuong(JButton btn) {
        btn.setPreferredSize(new Dimension(35, 35));
        btn.setBackground(new Color(240, 240, 240));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Lớp hỗ trợ vẽ khối bo tròn góc
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            this.cornerRadius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }

    // Khởi chạy để kiểm tra giao diện
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmGioHang().setVisible(true));
    }
}