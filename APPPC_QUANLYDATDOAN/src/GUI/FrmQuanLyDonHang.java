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

public class FrmQuanLyDonHang extends JFrame {

    private NguoiDung currentUser;
    private DonHangBLL donHangBLL = new DonHangBLL();

    private JTable tableDonHang;
    private DefaultTableModel tableModel;
    private JButton btnXacNhan, btnHuyDon;

    // --- Dark Theme Palette ---
    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    private final DecimalFormat dfMoney = new DecimalFormat("#,### VNĐ");
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmQuanLyDonHang(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Quản Lý Đơn Hàng Dành Cho Quán Ăn");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(COLOR_BG);

        // --- Header ---
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(COLOR_BG);

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel lblTitle = new JLabel("Danh Sách Đơn Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);


        pnlTop.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlNavBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlNavBar.setBackground(COLOR_BG);

        JButton btnTabThucDon = createNavButton("Thực Đơn Quán", false);
        JButton btnTabDonHang = createNavButton("Đơn Hàng Quán", true);
        JButton btnTabThongKe = createNavButton("Thống Kê", false);

        btnTabThucDon.addActionListener(e -> {
            new FrmQuanLyQuan(currentUser).setVisible(true);
            this.dispose();
        });
        btnTabThongKe.addActionListener(e -> {
            new FrmThongKe(currentUser).setVisible(true);
            this.dispose();
        });

        pnlNavBar.add(btnTabThucDon);
        pnlNavBar.add(btnTabDonHang);
        pnlNavBar.add(btnTabThongKe);

        pnlTop.add(pnlNavBar, BorderLayout.SOUTH);
        add(pnlTop, BorderLayout.NORTH);

        // --- Table ---
        String[] columns = {"Mã Đơn", "Thời Gian", "Tổng Tiền", "Mã Khách", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableDonHang = new JTable(tableModel);
        tableDonHang.setRowHeight(30);
        tableDonHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableDonHang.setBackground(COLOR_CARD);
        tableDonHang.setForeground(COLOR_TEXT);
        tableDonHang.setGridColor(new Color(50, 50, 50));
        tableDonHang.setSelectionBackground(new Color(50, 80, 50));
        tableDonHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(220, 220, 220));
                c.setForeground(Color.BLACK);
                c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                return c;
            }
        };
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableDonHang.getColumnModel().getColumnCount(); i++) {
            tableDonHang.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableDonHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getViewport().setBackground(COLOR_BG);
        JPanel pnlTableContainer = new JPanel(new BorderLayout());
        pnlTableContainer.setBackground(COLOR_BG);
        pnlTableContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnlTableContainer.add(scrollPane, BorderLayout.CENTER);
        add(pnlTableContainer, BorderLayout.CENTER);

        // --- Bottom (Các nút thao tác) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlBottom.setBackground(COLOR_BG);
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));

        btnXacNhan = new JButton("XÁC NHẬN ĐƠN (CHUẨN BỊ MÓN)");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btnXacNhan, new Color(16, 185, 129), new Color(16, 185, 129), new Dimension(280, 44));

        btnHuyDon = new JButton("TỪ CHỐI ĐƠN HÀNG");
        btnHuyDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btnHuyDon, new Color(220, 38, 38), new Color(220, 38, 38), new Dimension(220, 44));

        btnXacNhan.addActionListener(e -> capNhatTrangThai("ĐANG CHUẨN BỊ"));
        btnHuyDon.addActionListener(e -> capNhatTrangThai("ĐÃ HỦY"));

        pnlBottom.add(btnXacNhan);
        pnlBottom.add(btnHuyDon);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        // Lấy đúng QuanAnId của chủ quán từ CSDL (tránh tự ghép mã dễ sai)
        String quanAnId = new ProcessData.NguoiDungDAO().layQuanAnId(currentUser.getNguoiDungId());
        if (quanAnId == null || quanAnId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy quán ăn của tài khoản này.\nVui lòng kiểm tra dữ liệu bảng QuanAn (ChuQuanId).",
                    "Thiếu dữ liệu quán ăn",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<DonHang> danhSach = donHangBLL.layDanhSachDonHangQuanAn(quanAnId);

        for (DonHang dh : danhSach) {
            tableModel.addRow(new Object[]{
                    dh.getDonHangId(),
                    dfDate.format(dh.getThoiGianDat()),
                    dfMoney.format(dh.getTongTien()),
                    dh.getKhachHangId(),
                    dh.getTrangThai()
            });
        }
    }

    private void capNhatTrangThai(String trangThaiMoi) {
        int row = tableDonHang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String donHangId = tableModel.getValueAt(row, 0).toString();
        String trangThaiHienTai = tableModel.getValueAt(row, 4).toString();

        if (!trangThaiHienTai.equals("CHỜ XÁC NHẬN")) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể thao tác với đơn hàng đang CHỜ XÁC NHẬN!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Chuyển đơn hàng " + donHangId + " sang trạng thái: " + trangThaiMoi + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (donHangBLL.capNhatTrangThai(donHangId, trangThaiMoi)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (isActive) {
            styleButton(btn, COLOR_PRIMARY, COLOR_PRIMARY, new Dimension(150, 38));
        } else {
            styleButton(btn, COLOR_CARD, COLOR_PRIMARY, new Dimension(150, 38));
        }
        return btn;
    }

    private static void styleButton(JButton btn, Color bg, Color border, Dimension size) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(border, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (size != null) btn.setPreferredSize(size);
    }
}