package GUI;

import BusinessLogic.blSANPHAM;
import SCHEMA.NGUOIDUNG;
import SCHEMA.SANPHAM;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class FrmThucDon extends JFrame {
    
    private blSANPHAM sanPhamBLL;
    private Color colorPrimary = new Color(238, 77, 45); // Màu cam chủ đạo
    private JPanel pnlListMonAn; // Bảng chứa danh sách thẻ món ăn

    // Form nhận vào đối tượng NGUOIDUNG để biết ai đang đăng nhập
    public FrmThucDon (NGUOIDUNG user) {
        sanPhamBLL = new blSANPHAM();

        // 1. Cài đặt cửa sổ chính
        setTitle("FoodApp - Thực Đơn Hôm Nay");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // 2. Thanh tiêu đề (Header)
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("🍜 DANH SÁCH MÓN ĂN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(colorPrimary);
        
        // Hiển thị tên người dùng đã đăng nhập
        String tenHienThi = (user != null) ? user.getHoTen() : "Khách";
        JLabel lblUser = new JLabel("👤 Xin chào, " + tenHienThi);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(lblUser, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // 3. Khu vực chứa thẻ món ăn
        // FlowLayout.LEFT giúp các thẻ xếp từ trái qua phải, tự rớt dòng nếu hết chỗ
        pnlListMonAn = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        pnlListMonAn.setBackground(new Color(245, 245, 245));
        
        // Bọc vào thanh cuộn để người dùng lướt xuống được
        JScrollPane scrollPane = new JScrollPane(pnlListMonAn);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Tăng tốc độ cuộn chuột
        add(scrollPane, BorderLayout.CENTER);

        // 4. Gọi dữ liệu từ Database để vẽ lên giao diện
        taiDuLieuTuDatabase();
    }

    /**
     * Hàm lấy dữ liệu và tạo các thẻ hiển thị động
     */
    private void taiDuLieuTuDatabase() {
        List<SANPHAM> danhSach = sanPhamBLL.hienThiDanhSach();
        
        if(danhSach.isEmpty()) {
            pnlListMonAn.add(new JLabel("Hiện tại chưa có món ăn nào trong hệ thống."));
            return;
        }

        // Lặp qua từng món ăn và vẽ thành 1 thẻ (Card)
        for (SANPHAM sp : danhSach) {
            // Định dạng giá tiền cho đẹp
            String giaBan = String.format("%,.0f đ", sp.getGia()); 
            
            // Gọi hàm thiết kế thẻ
            JPanel card = taoCardMonAn("🍲", sp.getTenSanPham(), "Cung cấp bởi quán: " + sp.getQuanAnId(), giaBan);
            pnlListMonAn.add(card);
        }
        
        // Cập nhật lại kích thước bảng chứa để thanh cuộn hoạt động đúng
        int soHang = (danhSach.size() / 4) + 1; // Giả sử mỗi hàng chứa 4 món
        pnlListMonAn.setPreferredSize(new Dimension(850, soHang * 260));
    }

    /**
     * Hàm thiết kế giao diện cho một thẻ Món Ăn đơn lẻ
     */
    private JPanel taoCardMonAn(String icon, String tenMon, String moTa, String gia) {
        RoundedPanel card = new RoundedPanel(15, Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 240));

        // Nửa trên: Nền màu nhạt chứa Icon
        RoundedPanel pnlImage = new RoundedPanel(15, new Color(255, 235, 225));
        pnlImage.setLayout(new BorderLayout());
        pnlImage.setPreferredSize(new Dimension(200, 120));
        JLabel lblIcon = new JLabel(icon, SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        pnlImage.add(lblIcon, BorderLayout.CENTER);

        // Nửa dưới: Chứa tên, mô tả và giá
        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
        pnlInfo.setOpaque(false);
        pnlInfo.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTen = new JLabel(tenMon);
        lblTen.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextArea txtMoTa = new JTextArea(moTa);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtMoTa.setForeground(Color.GRAY);
        txtMoTa.setOpaque(false);
        txtMoTa.setEditable(false);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);

        // Khu vực giá và nút thêm vào giỏ
        JPanel pnlBot = new JPanel(new BorderLayout());
        pnlBot.setOpaque(false);
        JLabel lblGia = new JLabel(gia);
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblGia.setForeground(colorPrimary);
        
        JButton btnAdd = new JButton("+");
        btnAdd.setBackground(colorPrimary);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setBorderPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // TODO: Viết sự kiện khi click nút '+' sẽ lưu món này vào Giỏ Hàng
        btnAdd.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Đã thêm " + tenMon + " vào giỏ hàng!");
        });
        
        pnlBot.add(lblGia, BorderLayout.WEST);
        pnlBot.add(btnAdd, BorderLayout.EAST);

        pnlInfo.add(lblTen);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlInfo.add(txtMoTa);
        pnlInfo.add(Box.createRigidArea(new Dimension(0, 10)));
        pnlInfo.add(pnlBot);

        card.add(pnlImage, BorderLayout.NORTH);
        card.add(pnlInfo, BorderLayout.CENTER);

        return card;
    }

    // --- Lớp hỗ trợ vẽ khối bo tròn (Tái sử dụng) ---
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

    // Chạy thử trực tiếp form Thực đơn (Truyền null vào user để test)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmThucDon(null).setVisible(true));
    }
}