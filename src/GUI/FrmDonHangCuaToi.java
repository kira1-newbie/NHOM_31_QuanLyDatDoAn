package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Giao diện Theo dõi Đơn Hàng của Khách Hàng
 * @author Acer
 */
public class FrmDonHangCuaToi extends JFrame {

    private Color colorPrimary = new Color(238, 77, 45); // Màu cam chủ đạo FoodApp
    private Color colorBg = new Color(245, 245, 245);    // Màu nền xám nhạt
    private JPanel pnlDanhSachDon;

    public FrmDonHangCuaToi() {
        // 1. Thiết lập cửa sổ chính
        setTitle("FoodApp - Đơn Hàng Của Tôi");
        setSize(550, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(colorBg);

        // 2. Khu vực Tiêu đề (Header)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("📋 LỊCH SỬ ĐƠN HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(colorPrimary);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        add(pnlHeader, BorderLayout.NORTH);

        // 3. Khu vực Danh sách đơn hàng (Center)
        pnlDanhSachDon = new JPanel();
        pnlDanhSachDon.setLayout(new BoxLayout(pnlDanhSachDon, BoxLayout.Y_AXIS));
        pnlDanhSachDon.setBackground(colorBg);
        pnlDanhSachDon.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Bọc danh sách vào thanh cuộn
        JScrollPane scrollPane = new JScrollPane(pnlDanhSachDon);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // 4. Tải dữ liệu lên giao diện
        taiDuLieuDonHang();
    }

    /**
     * Hàm giả lập việc gọi dữ liệu. 
     * Khi có CSDL, bạn sẽ thay thế bằng việc gọi BusinessLogic.
     */
    private void taiDuLieuDonHang() {
        pnlDanhSachDon.removeAll();
        
        // Đơn 1: Đang chờ xác nhận (Màu vàng cam)
        pnlDanhSachDon.add(taoCardDonHang("#DH00124", "Phở Hà Nội 1946", "245,000 đ", "CHỜ XÁC NHẬN", new Color(245, 158, 11)));
        pnlDanhSachDon.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Đơn 2: Đang giao hàng (Màu xanh dương)
        pnlDanhSachDon.add(taoCardDonHang("#DH00123", "Cơm Tấm Sài Gòn", "65,000 đ", "ĐANG GIAO", new Color(59, 130, 246)));
        pnlDanhSachDon.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Đơn 3: Hoàn thành (Màu xanh lá)
        pnlDanhSachDon.add(taoCardDonHang("#DH00110", "Pizza Mamma Mia", "150,000 đ", "HOÀN THÀNH", new Color(16, 185, 129)));
        
        // Cập nhật lại giao diện
        pnlDanhSachDon.revalidate();
        pnlDanhSachDon.repaint();
    }

    /**
     * Hàm thiết kế chi tiết 1 thẻ (Card) hiển thị thông tin đơn hàng
     */
    private JPanel taoCardDonHang(String maDon, String tenQuan, String tongTien, String trangThai, Color mauTrangThai) {
        RoundedPanel pnlCard = new RoundedPanel(15, Color.WHITE);
        pnlCard.setLayout(new BorderLayout());
        pnlCard.setMaximumSize(new Dimension(500, 120));
        pnlCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // --- Phần trên: Mã đơn và Nhãn Trạng thái ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setOpaque(false);
        
        JLabel lblMaDon = new JLabel("Mã đơn: " + maDon);
        lblMaDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Nhãn Trạng thái (Tô nền màu để nổi bật)
        JLabel lblTrangThai = new JLabel("  " + trangThai + "  ");
        lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTrangThai.setForeground(Color.WHITE);
        lblTrangThai.setOpaque(true);
        lblTrangThai.setBackground(mauTrangThai);
        // Bo góc nhẹ cho nhãn trạng thái bằng cách dùng LineBorder đồng màu
        lblTrangThai.setBorder(BorderFactory.createLineBorder(mauTrangThai, 4, true));

        pnlTop.add(lblMaDon, BorderLayout.WEST);
        pnlTop.add(lblTrangThai, BorderLayout.EAST);

        // --- Phần giữa: Tên quán ăn ---
        JPanel pnlMid = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlMid.setOpaque(false);
        pnlMid.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel lblTenQuan = new JLabel("🏪 " + tenQuan);
        lblTenQuan.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTenQuan.setForeground(Color.DARK_GRAY);
        pnlMid.add(lblTenQuan);

        // --- Phần dưới: Tổng tiền và Nút Xem Chi Tiết ---
        JPanel pnlBot = new JPanel(new BorderLayout());
        pnlBot.setOpaque(false);
        
        JLabel lblTongTien = new JLabel("Tổng: " + tongTien);
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongTien.setForeground(colorPrimary);
        
        JButton btnChiTiet = new JButton("Chi tiết");
        btnChiTiet.setBackground(new Color(240, 240, 240));
        btnChiTiet.setForeground(Color.BLACK);
        btnChiTiet.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnChiTiet.setFocusPainted(false);
        btnChiTiet.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btnChiTiet.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Sự kiện xem chi tiết đơn
        btnChiTiet.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đang tải chi tiết cho đơn hàng: " + maDon);
        });

        pnlBot.add(lblTongTien, BorderLayout.WEST);
        pnlBot.add(btnChiTiet, BorderLayout.EAST);

        // Ghép 3 phần vào Thẻ chính
        pnlCard.add(pnlTop, BorderLayout.NORTH);
        pnlCard.add(pnlMid, BorderLayout.CENTER);
        pnlCard.add(pnlBot, BorderLayout.SOUTH);

        return pnlCard;
    }

    // --- Lớp hỗ trợ vẽ khối bo tròn góc ---
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

    // Hàm Main để chạy thử nghiệm giao diện trực tiếp
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDonHangCuaToi().setVisible(true));
    }
}