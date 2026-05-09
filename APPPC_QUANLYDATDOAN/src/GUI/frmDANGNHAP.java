package GUI;

import BusinessLogic.NguoiDungBLL;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class frmDANGNHAP extends JFrame {

    // --- CÁC THÀNH PHẦN GIAO DIỆN ---
    private CustomTextField txtEmail;
    private CustomPasswordField txtMatKhau;
    private JButton btnDangNhap;
    private JButton btnDangKy;
    private JLabel lblError;
    private JCheckBox chkHienThiMatKhau;

    // --- TẦNG NGHIỆP VỤ ---
    private NguoiDungBLL nguoiDungBLL = new NguoiDungBLL();

    // --- BẢNG MÀU CHUẨN ---
    private final Color COLOR_BG = new Color(24, 24, 24); // #181818
    private final Color COLOR_PRIMARY = new Color(0, 195, 0); // #00C300
    private final Color COLOR_TEXT = Color.WHITE; // White

    public frmDANGNHAP() {
        initUI();
        addEvents();
    }

    /**
     * 1. KHỞI TẠO VÀ SẮP XẾP GIAO DIỆN
     */
    private void initUI() {
        setTitle("LINE - Login to your Account");
        setSize(500, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, không tắt app
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel nền chính
        JPanel pnlBackground = new JPanel(new GridBagLayout());
        pnlBackground.setBackground(COLOR_BG);

        // Panel chứa Form (Card Login)
        JPanel pnlCard = new JPanel();
        pnlCard.setOpaque(false);
        pnlCard.setPreferredSize(new Dimension(420, 480));
        pnlCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 46));
        lblTitle.setForeground(COLOR_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 20, 30, 20);
        pnlCard.add(lblTitle, gbc);

        // Nhập Email
        txtEmail = new CustomTextField("Địa chỉ email   ", 20);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 5, 20);
        pnlCard.add(txtEmail, gbc);

        // Nhập Mật khẩu
        txtMatKhau = new CustomPasswordField("Mật khẩu", 20);
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 20, 5, 20);
        pnlCard.add(txtMatKhau, gbc);

        // Sub-options: Hiện MK
        JPanel pnlOptions = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlOptions.setOpaque(false);

        chkHienThiMatKhau = new JCheckBox("Hiện mật khẩu");
        chkHienThiMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chkHienThiMatKhau.setOpaque(false);
        chkHienThiMatKhau.setForeground(COLOR_TEXT);
        chkHienThiMatKhau.setFocusPainted(false);

        pnlOptions.add(chkHienThiMatKhau);

        gbc.gridy = 3;
        gbc.insets = new Insets(5, 20, 15, 20);
        pnlCard.add(pnlOptions, gbc);

        // Nút Đăng Nhập
        btnDangNhap = new JButton("Đăng Nhập") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setBackground(new Color(0, 0, 0));
        btnDangNhap.setContentAreaFilled(false);
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setOpaque(true);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangNhap.setPreferredSize(new Dimension(380, 48));
        btnDangNhap.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        gbc.gridy = 4;
        gbc.insets = new Insets(5, 20, 5, 20);
        pnlCard.add(btnDangNhap, gbc);

        // Label hiển thị lỗi Inline
        lblError = new JLabel(" ");
        lblError.setForeground(new Color(255, 100, 100));
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 20, 10, 20);
        pnlCard.add(lblError, gbc);

        // Nút Đăng Ký
        btnDangKy = new JButton("ĐĂNG KÝ") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setBackground(new Color(0, 0, 0));
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setFocusPainted(false);
        btnDangKy.setOpaque(true);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangKy.setPreferredSize(new Dimension(380, 45));
        btnDangKy.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        gbc.gridy = 6;
        gbc.insets = new Insets(5, 20, 20, 20);
        pnlCard.add(btnDangKy, gbc);

        pnlBackground.add(pnlCard);
        add(pnlBackground);
    }

    /**
     * 2. GẮN CÁC SỰ KIỆN (EVENTS)
     */
    private void addEvents() {
        btnDangNhap.addActionListener(e -> xuLyDangNhap());
        btnDangKy.addActionListener(e -> {
            new FrmDangKy().setVisible(true);
            this.dispose();
        });

        // UX: Nhấn Enter ở ô mật khẩu sẽ submit luôn
        txtMatKhau.addActionListener(e -> xuLyDangNhap());
        txtEmail.addActionListener(e -> txtMatKhau.requestFocus());

        chkHienThiMatKhau.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                txtMatKhau.setEchoChar((char) 0);
            } else {
                txtMatKhau.setEchoChar('•');
            }
        });

        // Hover effect cho Button
        btnDangNhap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnDangNhap.isEnabled())
                    btnDangNhap.setBackground(new Color(30, 30, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnDangNhap.isEnabled())
                    btnDangNhap.setBackground(new Color(0, 0, 0));
            }
        });

        btnDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnDangKy.isEnabled())
                    btnDangKy.setBackground(new Color(30, 30, 30));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btnDangKy.isEnabled())
                    btnDangKy.setBackground(new Color(0, 0, 0));
            }
        });
    }

    /**
     * 3. KIỂM TRA ĐẦU VÀO (VALIDATION)
     */
    private boolean validateInput() {
        String email = txtEmail.getText().trim();
        String password = new String(txtMatKhau.getPassword());

        if (email.isEmpty()) {
            lblError.setText("Vui lòng nhập Email!");
            txtEmail.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            lblError.setText("Vui lòng nhập Mật khẩu!");
            txtMatKhau.requestFocus();
            return false;
        }

        lblError.setText(" ");
        return true;
    }

    /**
     * 4. XỬ LÝ ĐĂNG NHẬP (BẤT ĐỒNG BỘ VỚI SWINGWORKER)
     */
    private void xuLyDangNhap() {
        if (!validateInput())
            return;

        String email = txtEmail.getText().trim();
        String password = new String(txtMatKhau.getPassword());

        btnDangNhap.setEnabled(false);
        btnDangNhap.setText("Đang xử lý...");
        btnDangNhap.setBackground(new Color(30, 30, 30));

        SwingWorker<NguoiDung, Void> worker = new SwingWorker<NguoiDung, Void>() {
            @Override
            protected NguoiDung doInBackground() {
                try {
                    Thread.sleep(500);
                } catch (Exception ignored) {
                }
                return nguoiDungBLL.dangNhap(email, password);
            }

            @Override
            protected void done() {
                btnDangNhap.setEnabled(true);
                btnDangNhap.setText("Đăng Nhập");
                btnDangNhap.setBackground(new Color(0, 0, 0));

                try {
                    NguoiDung user = get();
                    if (user != null) {
                        dieuHuongTheoRole(user);
                    } else {
                        lblError.setText("Sai Email hoặc Mật khẩu!");
                    }
                } catch (Exception ex) {
                    lblError.setText("Lỗi kết nối hệ thống!");
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    /**
     * 5. ROUTING: ĐIỀU HƯỚNG MÀN HÌNH THEO ROLE
     */
    private void dieuHuongTheoRole(NguoiDung user) {


        switch (user.getRoleId()) {
            case 1:
                new FrmAdmin(user).setVisible(true);
                break;
            case 2:
                new FrmThucDon(user).setVisible(true);
                break;
            case 3:
                new FrmShipper(user).setVisible(true);
                break;
            case 4:
                new FrmQuanLyQuan(user).setVisible(true);
                break;
        }

        this.dispose(); // Đóng form đăng nhập sau khi xử lý xong
    }

    // ==========================================================
    // CÁC LỚP UI TIỆN ÍCH (INNER CLASSES)
    // ==========================================================

    class CustomTextField extends JTextField {
        private String placeholder;

        public CustomTextField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            setBackground(new Color(24, 24, 24));
            setCaretColor(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    new EmptyBorder(10, 10, 10, 10)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty() && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 120, 120));
                g2.setFont(getFont());
                int x = getInsets().left;
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    class CustomPasswordField extends JPasswordField {
        private String placeholder;

        public CustomPasswordField(String placeholder, int columns) {
            super(columns);
            this.placeholder = placeholder;
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setForeground(Color.WHITE);
            setBackground(new Color(24, 24, 24));
            setCaretColor(Color.WHITE);
            setEchoChar('•');
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                    new EmptyBorder(10, 10, 10, 10)));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (new String(getPassword()).isEmpty() && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(120, 120, 120));
                g2.setFont(getFont());
                int x = getInsets().left;
                int y = (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}