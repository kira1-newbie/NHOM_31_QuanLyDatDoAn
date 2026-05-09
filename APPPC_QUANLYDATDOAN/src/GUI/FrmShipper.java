package GUI;

import BusinessLogic.ShipperBLL;
import SCHEMA.DonHang;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmShipper extends JFrame {

    private NguoiDung currentUser;
    private ShipperBLL shipperBLL = new ShipperBLL();

    private static final String CARD_CAN_NHAN = "CAN_NHAN";
    private static final String CARD_DANG_GIAO = "DANG_GIAO";

    private String activeCard = CARD_CAN_NHAN;
    private JPanel pnlCards;
    private CardLayout cardLayout;

    private JButton btnTabCanNhan;
    private JButton btnTabDangGiao;

    private JTable tableDonCanNhan;
    private DefaultTableModel modelDonCanNhan;
    private JButton btnNhanDon;

    private JTable tableDonDangGiao;
    private DefaultTableModel modelDonDangGiao;
    private JButton btnDaGiao;



    // --- Dark Theme Palette (đồng nhất với các form khác) ---
    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    private final DecimalFormat dfMoney = new DecimalFormat("#,### VNĐ");
    private final SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public FrmShipper(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    /**
     * 1. Khởi tạo giao diện
     */
    private void initUI() {
        setTitle("Cổng Dành Cho Đối Tác Giao Hàng (Shipper)");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("Danh Sách Đơn Hàng Cần Giao");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);



        add(pnlHeader, BorderLayout.NORTH);
        getContentPane().setBackground(COLOR_BG);

        // --- Thanh chuyển tab (đồng bộ style với các nút khác) ---
        JPanel pnlTabs = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlTabs.setBackground(COLOR_BG);
        pnlTabs.setBorder(new EmptyBorder(0, 20, 0, 20));

        btnTabCanNhan = createTabButton("Đơn cần nhận", true);
        btnTabDangGiao = createTabButton("Đơn đang giao", false);

        btnTabCanNhan.addActionListener(e -> chuyenTab(CARD_CAN_NHAN));
        btnTabDangGiao.addActionListener(e -> chuyenTab(CARD_DANG_GIAO));

        pnlTabs.add(btnTabCanNhan);
        pnlTabs.add(btnTabDangGiao);

        // --- Tab 1: Đơn cần nhận (ĐANG CHUẨN BỊ) ---
        JPanel tabCanNhan = new JPanel(new BorderLayout());
        tabCanNhan.setBackground(COLOR_BG);
        modelDonCanNhan = createTableModel();
        tableDonCanNhan = createStyledTable(modelDonCanNhan);
        tabCanNhan.add(wrapTable(tableDonCanNhan), BorderLayout.CENTER);

        // --- Tab 2: Đơn đang giao của tôi (ĐANG GIAO) ---
        JPanel tabDangGiao = new JPanel(new BorderLayout());
        tabDangGiao.setBackground(COLOR_BG);
        modelDonDangGiao = createTableModel();
        tableDonDangGiao = createStyledTable(modelDonDangGiao);
        tabDangGiao.add(wrapTable(tableDonDangGiao), BorderLayout.CENTER);

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.setBackground(COLOR_BG);
        pnlCards.add(tabCanNhan, CARD_CAN_NHAN);
        pnlCards.add(tabDangGiao, CARD_DANG_GIAO);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBackground(COLOR_BG);
        pnlCenter.add(pnlTabs, BorderLayout.NORTH);
        pnlCenter.add(pnlCards, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.CENTER);

        // --- Bottom ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBorder(new EmptyBorder(10, 20, 20, 20));
        pnlBottom.setBackground(COLOR_BG);

        btnNhanDon = new JButton("NHẬN GIAO ĐƠN NÀY");
        btnNhanDon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btnNhanDon, new Color(16, 185, 129), new Color(16, 185, 129), new Dimension(220, 44));

        btnDaGiao = new JButton("ĐÃ GIAO");
        btnDaGiao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btnDaGiao, COLOR_PRIMARY, COLOR_PRIMARY, new Dimension(140, 44));
        btnDaGiao.setEnabled(false);

        btnNhanDon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyNhanDon();
            }
        });

        btnDaGiao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyDaGiao();
            }
        });

        tableDonDangGiao.getSelectionModel().addListSelectionListener(e -> capNhatTrangThaiNutDaGiao());

        pnlBottom.add(btnNhanDon);
        pnlBottom.add(btnDaGiao);
        add(pnlBottom, BorderLayout.SOUTH);

        // Default tab
        chuyenTab(CARD_CAN_NHAN);
    }

    /**
     * 2. Lấy dữ liệu danh sách đơn hàng nạp vào bảng
     */
    private void loadData() {
        modelDonCanNhan.setRowCount(0);
        List<DonHang> dsCanNhan = shipperBLL.layDanhSachDonCho();
        for (DonHang dh : dsCanNhan) addRow(modelDonCanNhan, dh);

        modelDonDangGiao.setRowCount(0);
        List<DonHang> dsDangGiao = shipperBLL.layDanhSachDonDangGiao(currentUser.getNguoiDungId());
        for (DonHang dh : dsDangGiao) addRow(modelDonDangGiao, dh);

        capNhatTrangThaiNutDaGiao();
    }

    /**
     * 3. Xử lý logic khi Shipper bấm nhận đơn
     */
    private void xuLyNhanDon() {
        int selectedRow = tableDonCanNhan.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn hàng trong bảng để nhận giao!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy Mã Đơn Hàng từ cột đầu tiên (index 0) của dòng đang được chọn
        String donHangId = modelDonCanNhan.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn chắc chắn muốn nhận giao đơn hàng " + donHangId + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Gọi BLL xử lý
            boolean thanhCong = shipperBLL.nhanDonGiao(donHangId, currentUser.getNguoiDungId());

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Nhận đơn thành công! Trạng thái đơn đã chuyển sang ĐANG GIAO.");
                loadData(); // Tải lại bảng để làm mất đi đơn hàng vừa nhận
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi nhận đơn. Có thể đơn hàng đã bị người khác nhận.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xuLyDaGiao() {
        int selectedRow = tableDonDangGiao.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một đơn trong tab 'Đơn đang giao'!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String donHangId = modelDonDangGiao.getValueAt(selectedRow, 0).toString();
        String trangThai = modelDonDangGiao.getValueAt(selectedRow, 4).toString();

        if (!"ĐANG GIAO".equals(trangThai)) {
            JOptionPane.showMessageDialog(this, "Chỉ có thể bấm 'Đã giao' khi đơn đang ở trạng thái ĐANG GIAO!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            capNhatTrangThaiNutDaGiao();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận đơn " + donHangId + " đã giao xong (chuyển sang HOÀN THÀNH)?",
                "Xác nhận đã giao",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = shipperBLL.xacNhanDaGiao(donHangId, currentUser.getNguoiDungId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công! Đơn đã chuyển sang HOÀN THÀNH.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại! Có thể đơn không thuộc shipper này hoặc trạng thái không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void capNhatTrangThaiNutDaGiao() {
        boolean inTabDangGiao = CARD_DANG_GIAO.equals(activeCard);
        if (!inTabDangGiao) {
            btnDaGiao.setEnabled(false);
            return;
        }
        int row = tableDonDangGiao.getSelectedRow();
        if (row < 0) {
            btnDaGiao.setEnabled(false);
            return;
        }
        String trangThai = modelDonDangGiao.getValueAt(row, 4).toString();
        btnDaGiao.setEnabled("ĐANG GIAO".equals(trangThai));
    }

    private void chuyenTab(String card) {
        if (card == null) return;
        activeCard = card;
        if (cardLayout != null && pnlCards != null) {
            cardLayout.show(pnlCards, card);
        }

        boolean isCanNhan = CARD_CAN_NHAN.equals(card);
        styleActiveTabButton(btnTabCanNhan, isCanNhan);
        styleActiveTabButton(btnTabDangGiao, !isCanNhan);

        capNhatTrangThaiNutDaGiao();
    }

    private JButton createTabButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleActiveTabButton(btn, isActive);
        return btn;
    }

    private void styleActiveTabButton(JButton btn, boolean isActive) {
        if (btn == null) return;
        if (isActive) {
            styleButton(btn, COLOR_PRIMARY, COLOR_PRIMARY, new Dimension(170, 38));
        } else {
            styleButton(btn, COLOR_CARD, COLOR_PRIMARY, new Dimension(170, 38));
        }
    }

    private DefaultTableModel createTableModel() {
        String[] columns = {"Mã Đơn", "Thời Gian Đặt", "Tổng Tiền", "Địa Chỉ Giao", "Trạng Thái"};
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setBackground(COLOR_CARD);
        table.setForeground(COLOR_TEXT);
        table.setGridColor(new Color(50, 50, 50));
        table.setSelectionBackground(new Color(50, 80, 50));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            if (i != 3) { // cột địa chỉ canh trái
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        return table;
    }

    private JPanel wrapTable(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        scrollPane.getViewport().setBackground(COLOR_BG);
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(COLOR_BG);
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        pnl.add(scrollPane, BorderLayout.CENTER);
        return pnl;
    }

    private void addRow(DefaultTableModel model, DonHang dh) {
        Object[] row = {
                dh.getDonHangId(),
                dfDate.format(dh.getThoiGianDat()),
                dfMoney.format(dh.getTongTien()),
                dh.getDiaChiGiao(),
                dh.getTrangThai()
        };
        model.addRow(row);
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