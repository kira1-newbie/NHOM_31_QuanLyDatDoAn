package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Giao diện dành cho Người Giao Hàng (Shipper)
 */
public class FrmShipper extends JFrame {

    private Color colorPrimary = new Color(5, 150, 105); // Xanh lá đặc trưng Shipper

    public FrmShipper() {
        setTitle("FoodApp - Cổng Shipper");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // 1. Header Shipper
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(colorPrimary);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("🛵 TÀI XẾ TRẦN VĂN TỐC", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblStatus = new JLabel("SẴN SÀNG");
        lblStatus.setForeground(Color.WHITE);
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblStatus.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1, true));
        
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(lblStatus, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Danh sách Đơn Phát (Broadcasting)
        JPanel pnlDanhSachDon = new JPanel();
        pnlDanhSachDon.setLayout(new BoxLayout(pnlDanhSachDon, BoxLayout.Y_AXIS));
        pnlDanhSachDon.setBackground(new Color(245, 245, 245));
        pnlDanhSachDon.setBorder(new EmptyBorder(15, 15, 15, 15));

        // TODO: Lấy dữ liệu bảng GiaoHang nơi chưa có ShipperId nhận
        pnlDanhSachDon.add(taoCardNhanDon("Phở Hà Nội 1946", "123 Nguyễn Trãi, Q1", "456 Lê Văn Lương, Q7", "2.4 km", "+18,000 đ"));
        pnlDanhSachDon.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlDanhSachDon.add(taoCardNhanDon("Cơm Tấm Sài Gòn", "789 CMT8, Q3", "111 Điện Biên Phủ, Bình Thạnh", "3.1 km", "+22,000 đ"));

        JScrollPane scrollPane = new JScrollPane(pnlDanhSachDon);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel taoCardNhanDon(String quan, String dcQuan, String dcKhach, String khoangCach, String thuNhap) {
        JPanel pnlCard = new JPanel(new BorderLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setMaximumSize(new Dimension(500, 140));
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(110, 231, 183), 2), // Viền xanh lá mạ
            new EmptyBorder(15, 15, 15, 15)
        ));

        // Nội dung địa chỉ
        JPanel pnlInfo = new JPanel(new GridLayout(3, 1, 0, 5));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.add(new JLabel("<html><b>Quán:</b> " + quan + " (" + dcQuan + ")</html>"));
        pnlInfo.add(new JLabel("<html><b>Giao đến:</b> " + dcKhach + "</html>"));
        
        JLabel lblGia = new JLabel("Khoảng cách: " + khoangCach + "  |  Thu nhập: " + thuNhap);
        lblGia.setForeground(colorPrimary);
        lblGia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlInfo.add(lblGia);

        // Nút nhận đơn
        JButton btnNhan = new JButton("Nhận Đơn Ngay");
        btnNhan.setBackground(colorPrimary);
        btnNhan.setForeground(Color.WHITE);
        btnNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNhan.setFocusPainted(false);
        
        pnlCard.add(pnlInfo, BorderLayout.CENTER);
        pnlCard.add(btnNhan, BorderLayout.SOUTH);
        
        return pnlCard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmShipper().setVisible(true));
    }
}