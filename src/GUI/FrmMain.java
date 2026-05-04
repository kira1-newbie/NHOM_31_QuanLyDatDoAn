package GUI;

import SCHEMA.NGUOIDUNG;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;

public class FrmMain extends JFrame implements ActionListener {

    // ── Màu sắc chủ đạo của FoodApp ───────────────────────────────────────────
    private static final Color BG_DARK = new Color(245, 245, 245);       // Xám nền desktop
    private static final Color BG_SURFACE = new Color(255, 255, 255);    // Trắng cho menu/sidebar
    private static final Color COLOR_PRIMARY = new Color(238, 77, 45);   // Cam FoodApp
    private static final Color TEXT_MAIN = new Color(50, 50, 50);        // Đen xám cho chữ
    private static final Color TEXT_MUTED = new Color(130, 130, 130);    // Xám mờ cho tiêu đề phụ
    private static final Color COLOR_GREEN = new Color(16, 185, 129);    // Xanh lá (Hoàn thành/Đăng nhập)
    private static final Color COLOR_RED = new Color(220, 38, 38);       // Đỏ (Hủy/Đăng xuất)
    private static final Color BORDER_COLOR = new Color(230, 230, 230);  // Màu viền

    // ── Menu & menu items ─────────────────────────────────────────────────────
    private JMenuBar menubar;
    private JMenu menuHeThong, menuKhachHang, menuDoiTac;
    private JMenuItem miDangNhap, miDangXuat, miDangKy;
    private JMenuItem miThucDon, miGioHang, miDonHangCuaToi;
    private JMenuItem miQuanLyQuan, miCungShipper, miAdmin;

    // ── Desktop (Vùng trống ở giữa để hiển thị logo hoặc MDI) ─────────────────
    public JDesktopPane theDesktop;

    // ── Sidebar buttons ───────────────────────────────────────────────────────
    private JButton btnThucDon, btnGioHang, btnDonHangCuaToi;
    private JButton btnQuanLyQuan, btnShipper, btnAdmin;
    private JButton btnDangNhap, btnDangXuat;

    // ── Status bar components ─────────────────────────────────────────────────
    private JLabel lblClock;
    private JLabel lblUserStatus;   
    private Timer clockTimer;

    // ── Trạng thái đăng nhập ─────────────────────────────────────────────────
    private boolean isLoggedIn = false;
    private NGUOIDUNG currentUser = null;

    // ═════════════════════════════════════════════════════════════════════════
    public FrmMain() {
        setTitle("FoodApp PC - Hệ Thống Quản Lý Đặt Đồ Ăn");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        buildMenuBar();

        // Tách màn hình: Trái là Sidebar, Phải là Desktop
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildDesktop());
        split.setDividerLocation(240);
        split.setDividerSize(1);
        split.setBorder(null);
        add(split, BorderLayout.CENTER);

        add(buildStatusBar(), BorderLayout.SOUTH);
        startClock();

        // Trạng thái ban đầu: chưa đăng nhập
        updateLoginState(false, null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  ĐĂNG NHẬP — Gọi khi đăng nhập thành công
    // ─────────────────────────────────────────────────────────────────────────
    public void updateLoginState(boolean loggedIn, NGUOIDUNG user) {
        isLoggedIn = loggedIn;
        currentUser = user;

        if (loggedIn && user != null) {
            lblUserStatus.setText("👤 " + user.getHoTen() + " (" + user.getRole() + ")");
            lblUserStatus.setForeground(COLOR_PRIMARY);
        } else {
            lblUserStatus.setText("👤 Khách (Chưa đăng nhập)");
            lblUserStatus.setForeground(TEXT_MUTED);
        }

        // Cập nhật trạng thái các nút
        miDangNhap.setEnabled(!loggedIn);
        miDangKy.setEnabled(!loggedIn);
        miDangXuat.setEnabled(loggedIn);
        btnDangNhap.setEnabled(!loggedIn);
        btnDangXuat.setEnabled(loggedIn);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  MENU BAR
    // ─────────────────────────────────────────────────────────────────────────
    private void buildMenuBar() {
        menubar = new JMenuBar();
        menubar.setBackground(BG_SURFACE);
        menubar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        menubar.setPreferredSize(new Dimension(0, 45));

        JLabel logo = new JLabel("  🍜  FoodApp PC  ");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        logo.setForeground(COLOR_PRIMARY);
        menubar.add(logo);
        menubar.add(Box.createHorizontalStrut(16));

        // ── Hệ thống ──
        menuHeThong = makeMenu("Hệ thống");
        miDangNhap = makeItem("🔑  Đăng nhập");
        miDangKy = makeItem("📝  Đăng ký");
        miDangXuat = makeItem("🚪  Đăng xuất");
        menuHeThong.add(miDangNhap);
        menuHeThong.add(miDangKy);
        menuHeThong.addSeparator();
        menuHeThong.add(miDangXuat);

        // ── Khách Hàng ──
        menuKhachHang = makeMenu("Khách hàng");
        miThucDon = makeItem("🍲  Thực đơn hôm nay");
        miGioHang = makeItem("🛒  Giỏ hàng của tôi");
        miDonHangCuaToi = makeItem("📋  Lịch sử đơn hàng");
        menuKhachHang.add(miThucDon);
        menuKhachHang.add(miGioHang);
        menuKhachHang.add(miDonHangCuaToi);

        // ── Đối Tác & Quản Trị ──
        menuDoiTac = makeMenu("Đối tác & Quản trị");
        miQuanLyQuan = makeItem("🏪  Quản lý Quán ăn");
        miCungShipper = makeItem("🛵  Cổng Shipper");
        miAdmin = makeItem("🛡️  Admin Panel");
        menuDoiTac.add(miQuanLyQuan);
        menuDoiTac.add(miCungShipper);
        menuDoiTac.addSeparator();
        menuDoiTac.add(miAdmin);

        // Gắn sự kiện
        JMenuItem[] allItems = {miDangNhap, miDangKy, miDangXuat, miThucDon, miGioHang, miDonHangCuaToi, miQuanLyQuan, miCungShipper, miAdmin};
        for (JMenuItem mi : allItems) {
            mi.addActionListener(this);
        }

        menubar.add(menuHeThong);
        menubar.add(menuKhachHang);
        menubar.add(menuDoiTac);
        menubar.add(Box.createHorizontalGlue());
        setJMenuBar(menubar);
    }

    private JMenu makeMenu(String text) {
        JMenu m = new JMenu(text);
        m.setFont(new Font("Segoe UI", Font.BOLD, 13));
        m.setForeground(TEXT_MAIN);
        m.setBorderPainted(false);
        return m;
    }

    private JMenuItem makeItem(String text) {
        JMenuItem mi = new JMenuItem(text);
        mi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        mi.setBackground(BG_SURFACE);
        mi.setBorder(new EmptyBorder(8, 15, 8, 20));
        return mi;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  SIDEBAR (Thanh menu bên trái)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SURFACE);
        sidebar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 0, 1, BORDER_COLOR),
                new EmptyBorder(20, 10, 20, 10)));
        sidebar.setPreferredSize(new Dimension(240, 0));

        // Nhóm Khách Hàng
        addLabel(sidebar, "KHÁCH HÀNG");
        btnThucDon = sideBtn("🍲  Thực đơn hôm nay", "thucdon");
        btnGioHang = sideBtn("🛒  Giỏ hàng", "giohang");
        btnDonHangCuaToi = sideBtn("📋  Đơn hàng của tôi", "donhang");
        sidebar.add(btnThucDon);
        sidebar.add(vgap(5));
        sidebar.add(btnGioHang);
        sidebar.add(vgap(5));
        sidebar.add(btnDonHangCuaToi);
        sidebar.add(vgap(20));

        // Nhóm Đối Tác
        addLabel(sidebar, "ĐỐI TÁC & QUẢN TRỊ");
        btnQuanLyQuan = sideBtn("🏪  Quản lý Quán ăn", "quanlyquan");
        btnShipper = sideBtn("🛵  Cổng Shipper", "shipper");
        btnAdmin = sideBtn("🛡️  Admin Dashboard", "admin");
        sidebar.add(btnQuanLyQuan);
        sidebar.add(vgap(5));
        sidebar.add(btnShipper);
        sidebar.add(vgap(5));
        sidebar.add(btnAdmin);
        sidebar.add(vgap(20));

        // Nhóm Hệ Thống
        addLabel(sidebar, "HỆ THỐNG");
        btnDangNhap = sideBtn("🔑  Đăng nhập", "dangnhap");
        btnDangXuat = sideBtn("🚪  Đăng xuất", "dangxuat");
        sidebar.add(btnDangNhap);
        sidebar.add(vgap(5));
        sidebar.add(btnDangXuat);
        
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private void addLabel(JPanel p, String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 10, 8, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
    }

    private Component vgap(int h) { return Box.createVerticalStrut(h); }

    private JButton sideBtn(String label, String cmd) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Hiệu ứng hover
                if (getModel().isRollover() && isEnabled()) {
                    g2.setColor(new Color(COLOR_PRIMARY.getRed(), COLOR_PRIMARY.getGreen(), COLOR_PRIMARY.getBlue(), 30));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    
                    // Vẽ gạch đứng màu cam bên trái
                    g2.setColor(COLOR_PRIMARY);
                    g2.fillRoundRect(0, 8, 4, getHeight() - 16, 4, 4);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(TEXT_MAIN);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setActionCommand(cmd);
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { if (btn.isEnabled()) btn.setForeground(COLOR_PRIMARY); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setForeground(TEXT_MAIN); }
        });
        btn.addActionListener(this);
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  DESKTOP (Chính giữa màn hình)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildDesktop() {
        theDesktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vẽ logo lớn chìm ở giữa màn hình nền
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 230, 230));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 60));
                FontMetrics fm = g2.getFontMetrics();
                String text = "🍜 FoodApp PC";
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, x, y);
            }
        };
        theDesktop.setBackground(BG_DARK);
        
        JPanel w = new JPanel(new BorderLayout());
        w.add(theDesktop, BorderLayout.CENTER);
        return w;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  STATUS BAR (Thanh trạng thái)
    // ─────────────────────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        bar.setPreferredSize(new Dimension(0, 30));
        bar.setBackground(BG_SURFACE);
        bar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JLabel dot = new JLabel("●");
        dot.setForeground(COLOR_GREEN);
        new Timer(800, e -> dot.setVisible(!dot.isVisible())).start();

        JLabel lblKetNoi = new JLabel("Hệ thống Online");
        lblKetNoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        lblClock = new JLabel("--:--:--");
        lblClock.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblClock.setForeground(COLOR_PRIMARY);

        lblUserStatus = new JLabel("👤 Khách (Chưa đăng nhập)");
        lblUserStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUserStatus.setForeground(TEXT_MUTED);

        bar.add(dot);
        bar.add(lblKetNoi);
        bar.add(new JLabel(" | "));
        bar.add(lblClock);
        bar.add(new JLabel(" | "));
        bar.add(lblUserStatus);
        
        return bar;
    }

    private void startClock() {
        clockTimer = new Timer(1000, e -> lblClock.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        clockTimer.start();
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  XỬ LÝ SỰ KIỆN KHI BẤM NÚT (Điều hướng)
    // ─────────────────────────────────────────────────────────────────────────
    @Override
    public void actionPerformed(ActionEvent evt) {
        String cmd = evt.getActionCommand();
        Object src = evt.getSource();

        if (cmd == null) {
            // Xử lý JMenuItem
            if (src == miDangNhap) cmd = "dangnhap";
            else if (src == miDangXuat) cmd = "dangxuat";
            else if (src == miDangKy) cmd = "dangky";
            else if (src == miThucDon) cmd = "thucdon";
            else if (src == miGioHang) cmd = "giohang";
            else if (src == miDonHangCuaToi) cmd = "donhang";
            else if (src == miQuanLyQuan) cmd = "quanlyquan";
            else if (src == miCungShipper) cmd = "shipper";
            else if (src == miAdmin) cmd = "admin";
        }

        switch (cmd) {
            case "dangnhap":
                // Gọi form đăng nhập
                new FrmDangNhap().setVisible(true);
                break;
            case "dangky":
                new FrmDangKy().setVisible(true);
                break;
            case "dangxuat":
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    updateLoginState(false, null);
                    JOptionPane.showMessageDialog(this, "Đã đăng xuất thành công.");
                }
                break;
            case "thucdon":
                new FrmThucDon(currentUser).setVisible(true);
                break;
            case "giohang":
                new FrmGioHang().setVisible(true);
                break;
            case "donhang":
                new FrmDonHangCuaToi().setVisible(true);
                break;
            case "quanlyquan":
                new FrmQuanLyQuan().setVisible(true);
                break;
            case "shipper":
                new FrmShipper().setVisible(true);
                break;
            case "admin":
                new FrmAdmin().setVisible(true);
                break;
            default:
                break;
        }
    }

    // Chạy form Main
    public static void main(String[] args) {
        SwingUtilities. invokeLater(() -> new FrmMain().setVisible(true));
    }
}