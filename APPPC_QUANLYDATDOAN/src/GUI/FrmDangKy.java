package GUI;

import BusinessLogic.NguoiDungBLL;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmDangKy extends JFrame {

    private NguoiDungBLL nguoiDungBLL = new NguoiDungBLL();

    // Các thành phần UI
    private CustomTextField txtHoTen, txtEmail, txtSDT;
    private CustomPasswordField txtMatKhau, txtXacNhanMK;
    private JButton btnDangKy, btnHuy;

    private final Color COLOR_BG = new Color(24, 24, 24);     // #181818
    private final Color COLOR_PRIMARY = new Color(0, 195, 0); // #00C300
    private final Color COLOR_TEXT = Color.WHITE;

    public FrmDangKy() {
        initUI();
    }

    private void initUI() {
        setTitle("Đăng Ký Tài Khoản Mới");
        setSize(500, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(COLOR_BG);

        // Panel nền chính
        JPanel pnlBackground = new JPanel(new GridBagLayout());
        pnlBackground.setBackground(COLOR_BG);

        // Panel chứa form nhập liệu
        JPanel pnlForm = new JPanel(new GridBagLayout());
        pnlForm.setOpaque(false);
        pnlForm.setPreferredSize(new Dimension(420, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.weightx = 1.0;

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG KÝ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 42));
        lblTitle.setForeground(COLOR_PRIMARY);
        gbc.gridy = 0; gbc.insets = new Insets(10, 20, 30, 20);
        pnlForm.add(lblTitle, gbc);

        // Các trường nhập liệu
        gbc.insets = new Insets(10, 20, 5, 20);
        txtHoTen = new CustomTextField("Họ và tên", 20);
        gbc.gridy = 1; pnlForm.add(txtHoTen, gbc);

        txtEmail = new CustomTextField("Email", 20);
        gbc.gridy = 2; pnlForm.add(txtEmail, gbc);

        txtSDT = new CustomTextField("Số điện thoại", 20);
        gbc.gridy = 3; pnlForm.add(txtSDT, gbc);

        txtMatKhau = new CustomPasswordField("Mật khẩu", 20);
        gbc.gridy = 4; pnlForm.add(txtMatKhau, gbc);

        txtXacNhanMK = new CustomPasswordField("Xác nhận mật khẩu", 20);
        gbc.gridy = 5; pnlForm.add(txtXacNhanMK, gbc);

        // Nút Đăng ký
        btnDangKy = new JButton("Đăng Ký") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setBackground(new Color(0, 0, 0));
        btnDangKy.setContentAreaFilled(false);
        btnDangKy.setFocusPainted(false);
        btnDangKy.setOpaque(true);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangKy.setPreferredSize(new Dimension(380, 48));
        btnDangKy.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        btnDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnDangKy.isEnabled()) btnDangKy.setBackground(new Color(30, 30, 30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btnDangKy.isEnabled()) btnDangKy.setBackground(new Color(0, 0, 0));
            }
        });
        btnDangKy.addActionListener(e -> xuLyDangKy());
        gbc.gridy = 6; gbc.insets = new Insets(30, 20, 5, 20);
        pnlForm.add(btnDangKy, gbc);

        // Nút Hủy
        btnHuy = new JButton("Hủy bỏ") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setBackground(COLOR_BG);
        btnHuy.setContentAreaFilled(false);
        btnHuy.setFocusPainted(false);
        btnHuy.setOpaque(true);
        btnHuy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuy.setPreferredSize(new Dimension(380, 45));
        btnHuy.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));

        btnHuy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btnHuy.isEnabled()) btnHuy.setBackground(new Color(30, 30, 30));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (btnHuy.isEnabled()) btnHuy.setBackground(COLOR_BG);
            }
        });
        btnHuy.addActionListener(e -> {
            this.dispose();
            new frmDANGNHAP().setVisible(true);
        });
        gbc.gridy = 7; gbc.insets = new Insets(10, 20, 10, 20);
        pnlForm.add(btnHuy, gbc);

        // Đưa form vào giữa màn hình
        pnlBackground.add(pnlForm);
        setLayout(new BorderLayout());
        add(pnlBackground, BorderLayout.CENTER);
    }

    private void xuLyDangKy() {
        String hoTen = txtHoTen.getText().trim();
        String email = txtEmail.getText().trim();
        String sdt = txtSDT.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        String xacNhan = new String(txtXacNhanMK.getPassword());

        // Bỏ qua nếu rỗng
        if (hoTen.isEmpty() || email.isEmpty() || sdt.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gọi BLL xử lý
        boolean thanhCong = nguoiDungBLL.dangKyKhachHang(hoTen, email, sdt, matKhau, xacNhan);

        if (thanhCong) {
            JOptionPane.showMessageDialog(this, "Đăng ký tài khoản thành công!\nBạn có thể đăng nhập ngay bây giờ.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Đóng form đăng ký
            new frmDANGNHAP().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại. Vui lòng kiểm tra lại mật khẩu hoặc Email đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

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