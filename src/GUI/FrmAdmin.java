package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class FrmAdmin extends JFrame {

    // Định nghĩa màu sắc chủ đạo
    private Color colorBg = new Color(245, 245, 245);
    private Color colorSidebar = new Color(28, 20, 16); // Màu xám đen sang trọng
    private Color colorAccent = new Color(238, 77, 45); // Màu cam nhấn

    public FrmAdmin() {
        // Cài đặt thuộc tính cửa sổ
        setTitle("Admin Dashboard - FoodApp PC");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Lắp ráp Sidebar (Bên trái) và Content (Ở giữa)
        add(taoSidebar(), BorderLayout.WEST);
        add(taoMainContent(), BorderLayout.CENTER);
    }

    // Thiết kế thanh Menu bên trái
    private JPanel taoSidebar() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBackground(colorSidebar);
        pnlSidebar.setPreferredSize(new Dimension(220, 0));
        pnlSidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Tiêu đề Admin
        JLabel lblLogo = new JLabel("🛡️ ADMIN PANEL");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlSidebar.add(lblLogo);
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // Các nút Menu
        pnlSidebar.add(taoNutMenu("📊 Tổng quan (Dashboard)", true));
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(taoNutMenu("📋 Quản lý Đơn Hàng", false));
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(taoNutMenu("🏪 Quản lý Quán Ăn", false));
        pnlSidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlSidebar.add(taoNutMenu("👤 Quản lý Người Dùng", false));

        return pnlSidebar;
    }

    // Thiết kế nút Menu động (Đổi màu nếu đang chọn)
    private JPanel taoNutMenu(String text, boolean isActived) {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnl.setMaximumSize(new Dimension(200, 40));
        
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        if (isActived) {
            pnl.setBackground(colorAccent); // Sáng màu nếu đang chọn
            lbl.setForeground(Color.WHITE);
        } else {
            pnl.setBackground(colorSidebar); // Chìm màu nếu không chọn
            lbl.setForeground(new Color(180, 180, 180));
        }
        
        pnl.add(lbl);
        return pnl;
    }

    // Thiết kế nội dung trang Tổng Quan bên phải
    private JPanel taoMainContent() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(colorBg);
        pnlMain.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Tiêu đề trang
        JLabel lblTitle = new JLabel("Tổng quan Hệ thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        pnlMain.add(lblTitle, BorderLayout.NORTH);

        // Lưới hiển thị các thẻ Thống Kê (4 thẻ)
        JPanel pnlStats = new JPanel(new GridLayout(1, 4, 15, 0));
        pnlStats.setBackground(colorBg);
        pnlStats.setBorder(new EmptyBorder(20, 0, 20, 0));
        pnlStats.setPreferredSize(new Dimension(0, 150));

        // TODO: Dữ liệu này sẽ được lấy từ Database trong tương lai
        pnlStats.add(taoTheThongKe("Người dùng", "1,250", new Color(76, 175, 80)));
        pnlStats.add(taoTheThongKe("Quán ăn", "42", new Color(33, 150, 243)));
        pnlStats.add(taoTheThongKe("Shipper", "85", new Color(156, 39, 176)));
        pnlStats.add(taoTheThongKe("Tổng đơn hàng", "3,400", colorAccent));

        pnlMain.add(pnlStats, BorderLayout.CENTER);
        
        return pnlMain;
    }

    // Thiết kế Thẻ Thống Kê (Bo tròn, hiển thị con số)
    private JPanel taoTheThongKe(String title, String value, Color colorLine) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(4, 0, 0, 0, colorLine), // Đường viền màu ở trên cùng
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.DARK_GRAY);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblValue);

        return card;
    }

    // Lớp nội hỗ trợ vẽ bo góc
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

    // Hàm Main để chạy thử form độc lập
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmAdmin().setVisible(true));
    }
}