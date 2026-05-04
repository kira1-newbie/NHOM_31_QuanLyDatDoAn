package GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Giao diện dành cho Chủ Quán Ăn (Restaurant)
 */
public class FrmQuanLyQuan extends JFrame {

    private Color colorPrimary = new Color(238, 77, 45); 

    public FrmQuanLyQuan() {
        setTitle("FoodApp - Quản Lý Quán Ăn");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // 1. Header quán ăn
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitle = new JLabel("🏪 QUÁN CỦA TÔI (Phở Hà Nội)", SwingConstants.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(colorPrimary);
        
        JButton btnThucDon = new JButton("Quản lý Món Ăn");
        btnThucDon.setBackground(colorPrimary);
        btnThucDon.setForeground(Color.WHITE);
        btnThucDon.setFocusPainted(false);
        
        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(btnThucDon, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // 2. Danh sách Đơn hàng mới
        JPanel pnlDanhSachDon = new JPanel();
        pnlDanhSachDon.setLayout(new BoxLayout(pnlDanhSachDon, BoxLayout.Y_AXIS));
        pnlDanhSachDon.setBackground(new Color(245, 245, 245));
        pnlDanhSachDon.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblMoi = new JLabel("Đơn hàng chờ xác nhận:");
        lblMoi.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pnlDanhSachDon.add(lblMoi);
        pnlDanhSachDon.add(Box.createRigidArea(new Dimension(0, 10)));

        // TODO: Đổ dữ liệu từ bảng DonHang (Trạng thái = CHỜ XÁC NHẬN)
        pnlDanhSachDon.add(taoCardDonQuan("DH00124", "Nguyễn Văn Minh", "Phở Bò Đặc Biệt x2", "170,000 đ"));
        pnlDanhSachDon.add(Box.createRigidArea(new Dimension(0, 15)));
        pnlDanhSachDon.add(taoCardDonQuan("DH00125", "Trần Thị Lan", "Bún Bò Huế x1", "75,000 đ"));

        JScrollPane scrollPane = new JScrollPane(pnlDanhSachDon);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel taoCardDonQuan(String maDon, String tenKhach, String monAn, String gia) {
        JPanel pnlCard = new JPanel(new BorderLayout());
        pnlCard.setBackground(Color.WHITE);
        pnlCard.setMaximumSize(new Dimension(700, 100));
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel pnlInfo = new JPanel(new GridLayout(2, 1));
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.add(new JLabel("<html><b>Đơn: " + maDon + "</b> | Khách: " + tenKhach + "</html>"));
        pnlInfo.add(new JLabel("Món: " + monAn + " - Tổng: " + gia));

        JPanel pnlAction = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlAction.setBackground(Color.WHITE);
        
        JButton btnHuy = new JButton("Từ chối");
        btnHuy.setBackground(new Color(255, 235, 238));
        btnHuy.setForeground(Color.RED);
        
        JButton btnNhan = new JButton("✓ Nhận Đơn");
        btnNhan.setBackground(new Color(232, 245, 233));
        btnNhan.setForeground(new Color(46, 125, 50));
        
        pnlAction.add(btnHuy);
        pnlAction.add(btnNhan);

        pnlCard.add(pnlInfo, BorderLayout.CENTER);
        pnlCard.add(pnlAction, BorderLayout.EAST);
        return pnlCard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmQuanLyQuan().setVisible(true));
    }
}