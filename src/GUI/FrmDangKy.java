package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmDangKy extends JFrame {
    
    private JTextField txtHoTen;
    private JTextField txtEmail;
    private JTextField txtSoDienThoai;
    private JPasswordField txtMatKhau;

    public FrmDangKy() {
        setTitle("FoodApp - Đăng Ký Tài Khoản");
        setSize(480, 580);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new GridBagLayout());

        // Bảng chứa nội dung chính bo góc
        RoundedPanel pnlMain = new RoundedPanel(20, Color.WHITE);
        pnlMain.setPreferredSize(new Dimension(400, 500));
        pnlMain.setBorder(new EmptyBorder(25, 35, 25, 35));
        
        // Sử dụng GridBagLayout cho pnlMain để căn chỉnh siêu chuẩn xác
        pnlMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Giãn rộng hết cỡ theo chiều ngang
        gbc.weightx = 1.0;
        gbc.gridx = 0; // Tất cả đều nằm ở cột 0

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("TẠO TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(238, 77, 45));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0); // Khoảng cách dưới 25px
        pnlMain.add(lblTitle, gbc);

        // 2. Nhóm Họ và tên
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0); // Khoảng cách dưới 5px
        pnlMain.add(taoNhanCachTrai("Họ và tên:"), gbc);

        txtHoTen = new JTextField();
        lamDepOTxt(txtHoTen);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0); // Khoảng cách dưới 15px
        pnlMain.add(txtHoTen, gbc);

        // 3. Nhóm Email
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(taoNhanCachTrai("Email (@gmail.com):"), gbc);

        txtEmail = new JTextField();
        lamDepOTxt(txtEmail);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 15, 0);
        pnlMain.add(txtEmail, gbc);

        // 4. Nhóm Số điện thoại
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(taoNhanCachTrai("Số điện thoại:"), gbc);

        txtSoDienThoai = new JTextField();
        lamDepOTxt(txtSoDienThoai);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 15, 0);
        pnlMain.add(txtSoDienThoai, gbc);

        // 5. Nhóm Mật khẩu
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(taoNhanCachTrai("Mật khẩu:"), gbc);

        txtMatKhau = new JPasswordField();
        lamDepOTxt(txtMatKhau);
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 25, 0);
        pnlMain.add(txtMatKhau, gbc);

        // 6. Nút Đăng Ký
        JButton btnDangKy = new JButton("Đăng Ký Ngay");
        btnDangKy.setPreferredSize(new Dimension(300, 42));
        btnDangKy.setBackground(new Color(238, 77, 45));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangKy.setFocusPainted(false);
        btnDangKy.setBorderPainted(false);
        btnDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnDangKy.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(this, "Đang xử lý đăng ký cho: " + txtHoTen.getText());
        });
        
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 15, 0);
        pnlMain.add(btnDangKy, gbc);

        // 7. Nút chuyển hướng Đăng nhập
        JLabel lblLogin = new JLabel("Đã có tài khoản? Đăng nhập", SwingConstants.CENTER);
        lblLogin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLogin.setForeground(new Color(100, 100, 100));
        lblLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Tạo hiệu ứng hover đổi màu khi di chuột vào
        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblLogin.setForeground(new Color(238, 77, 45));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblLogin.setForeground(new Color(100, 100, 100));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Đóng form đăng ký
                new FrmDangNhap().setVisible(true); // Mở lại form đăng nhập
            }
        });

        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlMain.add(lblLogin, gbc);

        add(pnlMain);
    }

    // --- CÁC HÀM PHỤ TRỢ GIÚP MÃ GỌN GÀNG ---

    // Hàm tạo Nhãn (Label) căn trái, chữ đậm mờ
    private JLabel taoNhanCachTrai(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));
        return lbl;
    }

    // Hàm thiết kế lại ô nhập liệu (Thêm padding, viền xám nhạt bo tròn)
    private void lamDepOTxt(JTextField field) {
        field.setPreferredSize(new Dimension(300, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10) // Lề trong: Trên 5, Trái 10, Dưới 5, Phải 10
        ));
    }

    // Lớp vẽ giao diện bo tròn
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

    // Hàm Main để chạy thử trực tiếp
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDangKy().setVisible(true));
    }
}