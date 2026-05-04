package GUI;

// Nhập các lớp từ cấu trúc mới của bạn
import BusinessLogic.blNGUOIDUNG;
import SCHEMA.NGUOIDUNG;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmDangNhap extends JFrame {
    
    // Khai báo các ô nhập liệu
    private JTextField txtEmail;
    private JPasswordField txtMatKhau;
    
    // Khai báo lớp xử lý logic nghiệp vụ
    private blNGUOIDUNG nguoiDungBLL;

    public FrmDangNhap() {
        // Khởi tạo BLL (Business Logic)
        nguoiDungBLL = new blNGUOIDUNG();

        // Cài đặt các thuộc tính cơ bản của cửa sổ
        setTitle("FoodApp - Đăng Nhập");
        setSize(480, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));
        setLayout(new GridBagLayout());

        // Bảng chứa nội dung chính bo góc 20px
        RoundedPanel pnlMain = new RoundedPanel(20, Color.WHITE);
        pnlMain.setPreferredSize(new Dimension(400, 350));
        pnlMain.setBorder(new EmptyBorder(25, 35, 25, 35));
        
        // Sử dụng GridBagLayout để căn chỉnh chuẩn xác
        pnlMain.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.weightx = 1.0;
        gbc.gridx = 0; 

        // 1. Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(238, 77, 45)); 
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0); 
        pnlMain.add(lblTitle, gbc);

        // 2. Nhóm Email
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(taoNhanCachTrai("Email (@gmail.com):"), gbc);

        txtEmail = new JTextField();
        lamDepOTxt(txtEmail);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0); 
        pnlMain.add(txtEmail, gbc);

        // 3. Nhóm Mật khẩu
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        pnlMain.add(taoNhanCachTrai("Mật khẩu:"), gbc);

        txtMatKhau = new JPasswordField();
        lamDepOTxt(txtMatKhau);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 25, 0); 
        pnlMain.add(txtMatKhau, gbc);

        // 4. Nút Đăng Nhập
        JButton btnDangNhap = new JButton("Đăng Nhập");
        btnDangNhap.setPreferredSize(new Dimension(300, 42));
        btnDangNhap.setBackground(new Color(238, 77, 45));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnDangNhap.setFocusPainted(false);
        btnDangNhap.setBorderPainted(false);
        btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Sự kiện khi bấm Đăng nhập
        btnDangNhap.addActionListener((ActionEvent e) -> {
            thucHienDangNhap();
        });
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 15, 0);
        pnlMain.add(btnDangNhap, gbc);

        // 5. Nút chuyển hướng sang form Đăng Ký
        JLabel lblDangKy = new JLabel("Chưa có tài khoản? Đăng ký ngay", SwingConstants.CENTER);
        lblDangKy.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDangKy.setForeground(new Color(100, 100, 100));
        lblDangKy.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng đổi màu khi di chuột vào
        lblDangKy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblDangKy.setForeground(new Color(238, 77, 45));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblDangKy.setForeground(new Color(100, 100, 100));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); 
                new FrmDangKy().setVisible(true); // Đã cập nhật tên form theo chuẩn mới
            }
        });

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        pnlMain.add(lblDangKy, gbc);

        add(pnlMain);
    }

    // --- HÀM XỬ LÝ LOGIC ---
    private void thucHienDangNhap() {
        try {
            String email = txtEmail.getText();
            String matKhau = new String(txtMatKhau.getPassword());
            
            // Lấy thông tin từ BusinessLogic
            NGUOIDUNG user = nguoiDungBLL.kiemTraDangNhap(email, matKhau);
            
            if (user != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công! Xin chào " + user.getHoTen());
                this.dispose(); 
                // TODO: Chuyển sang form chính (Ví dụ: frm_Main hoặc frmTHUCDON)
            } else {
                JOptionPane.showMessageDialog(this, "Email hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    // --- CÁC HÀM PHỤ TRỢ ---
    private JLabel taoNhanCachTrai(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));
        return lbl;
    }

    private void lamDepOTxt(JTextField field) {
        field.setPreferredSize(new Dimension(300, 38));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    // Lớp vẽ khối nền bo tròn
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

    // Hàm Main để chạy thử
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
    }
}