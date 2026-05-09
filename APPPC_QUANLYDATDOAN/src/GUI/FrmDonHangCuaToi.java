package GUI;

import BusinessLogic.DonHangBLL;
import SCHEMA.DonHang;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmDonHangCuaToi extends JFrame {

    private NguoiDung currentUser;
    private DonHangBLL donHangBLL = new DonHangBLL();

    // Các thành phần UI
    private JTable tableDonHang;
    private DefaultTableModel tableModel;

    // --- Dark Theme Palette ---
    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    private final DecimalFormat dfMoney = new DecimalFormat("#,### VNĐ");
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmDonHangCuaToi(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    /**
     * 1. Khởi tạo giao diện (Header, Bảng dữ liệu)
     */
    private void initUI() {
        setTitle("Lịch Sử Đơn Hàng");
        setSize(1000, 600); // Tăng size cho giống giỏ hàng
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // --- Phần Header ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("Đơn Hàng Của Tôi");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- Phần Bảng dữ liệu (JTable) ---
        String[] columns = { "Mã Đơn", "Thời Gian Đặt", "Tổng Tiền", "Trạng Thái", "Địa Chỉ Giao" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Khóa không cho người dùng sửa trực tiếp trên bảng
            }
        };

        tableDonHang = new JTable(tableModel);
        tableDonHang.setRowHeight(30);
        tableDonHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDonHang.setBackground(COLOR_CARD);
        tableDonHang.setForeground(COLOR_TEXT);
        tableDonHang.setGridColor(new Color(50, 50, 50));
        tableDonHang.setSelectionBackground(new Color(50, 80, 50));

        // Renderer cho Header (Chữ đen, canh giữa, nền xám nhạt)
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(220, 220, 220)); // Xám nhạt
                c.setForeground(Color.BLACK);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                return c;
            }
        };
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Renderer cho nội dung (Canh giữa)
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tableDonHang.getColumnModel().getColumnCount(); i++) {
            tableDonHang.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            if (i != 4) { // Ngoại trừ cột địa chỉ (canh trái)
                tableDonHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Căn chỉnh độ rộng các cột cho đẹp mắt
        tableDonHang.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã Đơn
        tableDonHang.getColumnModel().getColumn(1).setPreferredWidth(150); // Thời Gian
        tableDonHang.getColumnModel().getColumn(2).setPreferredWidth(120); // Tổng Tiền
        tableDonHang.getColumnModel().getColumn(3).setPreferredWidth(150); // Trạng Thái
        tableDonHang.getColumnModel().getColumn(4).setPreferredWidth(250); // Địa Chỉ

        JScrollPane scrollPane = new JScrollPane(tableDonHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getViewport().setBackground(COLOR_BG);
        add(scrollPane, BorderLayout.CENTER);

        // --- Phần Bottom ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setBackground(COLOR_BG);
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));

        JButton btnHuyDon = new JButton("HỦY ĐƠN HÀNG") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnHuyDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuyDon.setBackground(new Color(220, 53, 69)); // Đỏ
        btnHuyDon.setForeground(Color.WHITE);
        btnHuyDon.setContentAreaFilled(false);
        btnHuyDon.setFocusPainted(false);
        btnHuyDon.setOpaque(true);
        btnHuyDon.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 1));
        btnHuyDon.setPreferredSize(new Dimension(150, 40));
        btnHuyDon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHuyDon.addActionListener(e -> xuLyHuyDon());

        JButton btnDong = new JButton("ĐÓNG") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
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

        pnlBottom.add(btnHuyDon);
        pnlBottom.add(btnDong);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void xuLyHuyDon() {
        int selectedRow = tableDonHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần hủy!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String donHangId = tableModel.getValueAt(selectedRow, 0).toString();
        String trangThai = tableModel.getValueAt(selectedRow, 3).toString();

        if (trangThai.equalsIgnoreCase("ĐÃ HỦY")) {
            JOptionPane.showMessageDialog(this, "Đơn hàng này đã bị hủy trước đó!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (trangThai.equalsIgnoreCase("HOÀN THÀNH") || trangThai.equalsIgnoreCase("ĐANG GIAO")) {
            JOptionPane.showMessageDialog(this, "Không thể hủy đơn hàng đang giao hoặc đã giao!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn hủy đơn hàng " + donHangId + "?",
                "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = donHangBLL.capNhatTrangThai(donHangId, "ĐÃ HỦY");
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã hủy đơn hàng thành công.");
                loadData(); // Tải lại bảng
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi hủy đơn hàng. Vui lòng thử lại.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 2. Lấy dữ liệu từ CSDL nạp vào bảng
     */
    private void loadData() {
        tableModel.setRowCount(0); // Làm sạch bảng

        // Gọi BLL lấy danh sách đơn hàng của User đang đăng nhập
        List<DonHang> danhSach = donHangBLL.layLichSuDonHang(currentUser.getNguoiDungId());

        for (DonHang dh : danhSach) {
            Object[] row = {
                    dh.getDonHangId(),
                    dfDate.format(dh.getThoiGianDat()),
                    dfMoney.format(dh.getTongTien()),
                    dh.getTrangThai(),
                    dh.getDiaChiGiao()
            };
            tableModel.addRow(row);
        }
    }
}