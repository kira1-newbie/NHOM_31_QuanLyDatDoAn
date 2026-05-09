package GUI;

import BusinessLogic.DonHangBLL;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class FrmGioHang extends JFrame {

    private NguoiDung currentUser;
    private String gioHangId;

    // Tầng BLL
    private DonHangBLL donHangBLL = new DonHangBLL();
    private BusinessLogic.GioHangBLL gioHangBLL = new BusinessLogic.GioHangBLL(); // Đã kích hoạt
    // (Lưu ý: Bạn sẽ cần bổ sung hàm layDanhSachChiTietGio() vào GioHangBLL sau)
    // private GioHangBLL gioHangBLL = new GioHangBLL();

    // Các thành phần UI
    private JTable tableGioHang;
    private DefaultTableModel tableModel;
    private JLabel lblTongTien;
    private JTextField txtDiaChi;

    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;
    private final DecimalFormat df = new DecimalFormat("#,### VNĐ");

    public FrmGioHang(NguoiDung user) {
        this.currentUser = user;
        // Lấy đúng mã giỏ hàng theo khách hàng từ CSDL (tránh tự ghép mã sai)
        this.gioHangId = gioHangBLL.layGioHangIdTheoKhachHang(currentUser.getNguoiDungId());

        initUI();
        loadDataToTable(); // Nạp dữ liệu
    }

    /**
     * 1. Khởi tạo Giao diện Giỏ Hàng
     */
    private void initUI() {
        setTitle("Giỏ Hàng Của Bạn");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_BG);

        // --- HEADER ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel lblTitle = new JLabel("Chi Tiết Giỏ Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CENTER (Bảng dữ liệu) ---
        // Thiết lập cấu trúc cột cho bảng
        String[] columns = { "Mã SP", "Tên Món Ăn", "Đơn Giá", "Số Lượng", "Thành Tiền" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép gõ trực tiếp vào ô để sửa
            }
        };
        tableGioHang = new JTable(tableModel);
        tableGioHang.setRowHeight(30);
        tableGioHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableGioHang.setBackground(COLOR_CARD);
        tableGioHang.setForeground(COLOR_TEXT);
        tableGioHang.setGridColor(new Color(50, 50, 50));
        tableGioHang.setSelectionBackground(new Color(50, 80, 50));

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

        for (int i = 0; i < tableGioHang.getColumnModel().getColumnCount(); i++) {
            tableGioHang.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            if (i != 1) { // Ngoại trừ cột tên món ăn (canh trái)
                tableGioHang.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPane = new JScrollPane(tableGioHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getViewport().setBackground(COLOR_BG);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM (Thanh toán & Địa chỉ) ---
        JPanel pnlBottom = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlBottom.setBackground(COLOR_BG);
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Khung nhập địa chỉ
        JPanel pnlDiaChi = new JPanel(new BorderLayout(10, 0));
        pnlDiaChi.setBackground(COLOR_BG);
        JLabel lblDiaChi = new JLabel("Địa chỉ giao hàng:");
        lblDiaChi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDiaChi.setForeground(COLOR_TEXT);
        txtDiaChi = new JTextField();
        txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDiaChi.setBackground(COLOR_CARD);
        txtDiaChi.setForeground(COLOR_TEXT);
        txtDiaChi.setCaretColor(COLOR_TEXT);
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                new EmptyBorder(8, 10, 8, 10)));
        pnlDiaChi.add(lblDiaChi, BorderLayout.WEST);
        pnlDiaChi.add(txtDiaChi, BorderLayout.CENTER);
        pnlBottom.add(pnlDiaChi);

        // Khung hiển thị tổng tiền & Nút chốt đơn
        JPanel pnlThanhToan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlThanhToan.setBackground(COLOR_BG);
        lblTongTien = new JLabel("Tổng: 0 VNĐ");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(COLOR_PRIMARY);

        JButton btnXoaMon = new JButton("XÓA MÓN ĐÃ CHỌN") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnXoaMon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXoaMon.setBackground(new Color(220, 53, 69)); // Đỏ
        btnXoaMon.setForeground(Color.WHITE);
        btnXoaMon.setContentAreaFilled(false);
        btnXoaMon.setFocusPainted(false);
        btnXoaMon.setOpaque(true);
        btnXoaMon.setBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 1));
        btnXoaMon.setPreferredSize(new Dimension(180, 40));
        btnXoaMon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoaMon.addActionListener(e -> xuLyXoaMon());

        JButton btnDatHang = new JButton("XÁC NHẬN ĐẶT HÀNG") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnDatHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDatHang.setBackground(COLOR_PRIMARY);
        btnDatHang.setForeground(Color.WHITE);
        btnDatHang.setContentAreaFilled(false);
        btnDatHang.setFocusPainted(false);
        btnDatHang.setOpaque(true);
        btnDatHang.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btnDatHang.setPreferredSize(new Dimension(200, 40));
        btnDatHang.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sự kiện: Bấm đặt hàng
        btnDatHang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDatHang();
            }
        });

        pnlThanhToan.add(lblTongTien);
        pnlThanhToan.add(btnXoaMon);
        pnlThanhToan.add(btnDatHang);
        pnlBottom.add(pnlThanhToan);

        add(pnlBottom, BorderLayout.SOUTH);
    }

    /**
     * 2. Tải dữ liệu giả lập (Mock Data) lên bảng
     * (Sau này sẽ gọi BLL để kéo từ Database)
     */
    /**
     * 2. Tải dữ liệu thật từ Database lên bảng
     */
    public void loadDataToTable() {
        tableModel.setRowCount(0); // Xóa trắng bảng trước khi nạp
        double tongTien = 0;

        // Nếu khách chưa có giỏ (chưa từng thêm món) thì báo trống
        if (gioHangId == null || gioHangId.trim().isEmpty()) {
            lblTongTien.setText("Giỏ hàng trống");
            return;
        }

        // Gọi BLL để lấy dữ liệu thực tế
        java.util.List<SCHEMA.ChiTietGioHangDTO> danhSach = gioHangBLL.layDanhSachChiTietGio(gioHangId);

        if (danhSach.isEmpty()) {
            lblTongTien.setText("Giỏ hàng trống");
            return;
        }

        // Đổ dữ liệu vào JTable
        for (SCHEMA.ChiTietGioHangDTO dto : danhSach) {
            Object[] row = {
                    dto.getSanPhamId(),
                    dto.getTenSanPham(),
                    df.format(dto.getDonGia()),
                    dto.getSoLuong(),
                    df.format(dto.getThanhTien())
            };
            tableModel.addRow(row);

            // Cộng dồn tổng tiền
            tongTien += dto.getThanhTien();
        }

        // Hiển thị tổng tiền
        lblTongTien.setText("Tổng: " + df.format(tongTien));
    }

    /**
     * 3. Xử lý logic Đặt Hàng thông qua BLL
     */
    private void xuLyDatHang() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }

        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ giao hàng!", "Cảnh báo",
                    JOptionPane.WARNING_MESSAGE);
            txtDiaChi.requestFocus();
            return;
        }

        // Đổi nút thành Loading
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đặt đơn hàng này?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Gọi BLL (Đã được viết ở các bước trước)
            boolean thanhCong = donHangBLL.datHang(currentUser.getNguoiDungId(), gioHangId, diaChi);

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đặt hàng thành công! Vui lòng chờ quán xác nhận.");
                tableModel.setRowCount(0); // Xóa giỏ
                lblTongTien.setText("Tổng: 0 VNĐ");
                this.dispose(); // Đóng form giỏ hàng
            } else {
                JOptionPane.showMessageDialog(this, "Đặt hàng thất bại. Vui lòng kiểm tra lại hệ thống.");
            }
        }
    }

    /**
     * 4. Xử lý logic Xóa món khỏi giỏ
     */
    private void xuLyXoaMon() {
        int selectedRow = tableGioHang.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa khỏi giỏ hàng!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sanPhamId = tableModel.getValueAt(selectedRow, 0).toString();
        String tenMon = tableModel.getValueAt(selectedRow, 1).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa món '" + tenMon + "' khỏi giỏ hàng?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = gioHangBLL.xoaKhoiGio(gioHangId, sanPhamId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Đã xóa món thành công.");
                loadDataToTable(); // Tải lại dữ liệu và cập nhật tổng tiền
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa món. Vui lòng thử lại.", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}