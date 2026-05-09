package GUI;

import BusinessLogic.DonHangBLL;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class FrmThongKe extends JFrame {

    private NguoiDung currentUser;
    private DonHangBLL donHangBLL = new DonHangBLL();

    // UI Components
    private JLabel lblTongDon;
    private JLabel lblDoanhThu;

    // --- Dark Theme Palette ---
    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    public FrmThongKe(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Thống Kê Doanh Thu");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // --- Header ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(COLOR_BG);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel lblTitle = new JLabel("BÁO CÁO KẾT QUẢ KINH DOANH");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        pnlHeader.add(lblTitle, BorderLayout.CENTER);
        pnlTop.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlNavBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlNavBar.setBackground(COLOR_BG);

        JButton btnTabThucDon = createNavButton("Thực Đơn Quán", false);
        JButton btnTabDonHang = createNavButton("Đơn Hàng Quán", false);
        JButton btnTabThongKe = createNavButton("Thống Kê", true);

        btnTabThucDon.addActionListener(e -> {
            new FrmQuanLyQuan(currentUser).setVisible(true);
            this.dispose();
        });
        btnTabDonHang.addActionListener(e -> {
            new FrmQuanLyDonHang(currentUser).setVisible(true);
            this.dispose();
        });

        pnlNavBar.add(btnTabThucDon);
        pnlNavBar.add(btnTabDonHang);
        pnlNavBar.add(btnTabThongKe);

        pnlTop.add(pnlNavBar, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // --- Center (Các thẻ thống kê) ---
        JPanel pnlCenter = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 50));
        pnlCenter.setBackground(COLOR_BG);

        // Thẻ 1: Tổng số đơn hàng
        RoundedPanel pnlDonHang = createStatCard("TỔNG SỐ ĐƠN HÀNG (HỢP LỆ)", new Color(41, 128, 185));
        lblTongDon = new JLabel("0 Đơn");
        lblTongDon.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTongDon.setForeground(Color.WHITE);
        lblTongDon.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlDonHang.add(Box.createVerticalStrut(20));
        pnlDonHang.add(lblTongDon);
        pnlCenter.add(pnlDonHang);

        // Thẻ 2: Tổng doanh thu
        RoundedPanel pnlDoanhThu = createStatCard("TỔNG DOANH THU", new Color(39, 174, 96)); // Xanh lá
        lblDoanhThu = new JLabel("0 VNĐ");
        lblDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblDoanhThu.setForeground(Color.WHITE);
        lblDoanhThu.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlDoanhThu.add(Box.createVerticalStrut(20));
        pnlDoanhThu.add(lblDoanhThu);
        pnlCenter.add(pnlDoanhThu);

        add(pnlCenter, BorderLayout.CENTER);

        // --- Bottom ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(COLOR_BG);
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));
        JButton btnDong = new JButton("ĐÓNG");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.setBackground(COLOR_PRIMARY);
        btnDong.setForeground(Color.WHITE);
        btnDong.setContentAreaFilled(false);
        btnDong.setFocusPainted(false);
        btnDong.setOpaque(true);
        btnDong.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btnDong.setPreferredSize(new Dimension(100, 40));
        btnDong.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDong.addActionListener(e -> this.dispose());
        pnlBottom.add(btnDong);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * Hàm tiện ích tạo một thẻ (Card) thống kê bo góc
     */
    private RoundedPanel createStatCard(String title, Color bgColor) {
        RoundedPanel card = new RoundedPanel(20, bgColor);
        card.setPreferredSize(new Dimension(280, 150));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(255, 255, 255, 200)); // Trắng hơi mờ
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(15));
        card.add(lblTitle);

        return card;
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (isActive) {
            styleButton(btn, COLOR_PRIMARY, COLOR_PRIMARY, new Dimension(150, 38));
        } else {
            styleButton(btn, COLOR_CARD, COLOR_PRIMARY, new Dimension(150, 38));
        }
        return btn;
    }

    private static void styleButton(JButton btn, Color bg, Color border, Dimension size) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(border, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (size != null) btn.setPreferredSize(size);
    }

    /**
     * Nạp dữ liệu từ CSDL
     */
    private void loadData() {
        // Lấy đúng mã quán theo chủ quán (tránh tự ghép mã dễ sai)
        String quanAnId = new ProcessData.NguoiDungDAO().layQuanAnId(currentUser.getNguoiDungId());
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            lblTongDon.setText("0 Đơn");
            lblDoanhThu.setText("0 VNĐ");
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy quán ăn của tài khoản này.\nVui lòng kiểm tra dữ liệu bảng QuanAn (ChuQuanId).",
                    "Thiếu dữ liệu quán ăn",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double[] ketQua = donHangBLL.layThongKeDoanhThu(quanAnId);

        // Hiển thị số đơn
        lblTongDon.setText((int)ketQua[0] + " Đơn");

        // Hiển thị doanh thu
        DecimalFormat df = new DecimalFormat("#,### VNĐ");
        lblDoanhThu.setText(df.format(ketQua[1]));
    }

    // --- Lớp Inner Class tạo Panel bo góc ---
    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Vẽ Shadow
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);

            // Vẽ nền
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}