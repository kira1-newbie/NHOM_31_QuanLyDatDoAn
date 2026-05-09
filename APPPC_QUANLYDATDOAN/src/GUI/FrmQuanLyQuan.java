package GUI;

import BusinessLogic.SanPhamBLL;
import SCHEMA.NguoiDung;
import SCHEMA.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmQuanLyQuan extends JFrame {

    private NguoiDung currentUser;
    private SanPhamBLL sanPhamBLL = new SanPhamBLL();

    // UI Components
    private JTextField txtMaSP, txtTenSP, txtGia;
    private JComboBox<String> cbTrangThai;
    private JTable tableMenu;
    private DefaultTableModel tableModel;
    private JButton btnThem, btnSua, btnXoa;

    // --- Dark Theme Palette ---
    private final Color COLOR_BG      = new Color(24, 24, 24);
    private final Color COLOR_CARD    = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT    = Color.WHITE;

    public FrmQuanLyQuan(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Quản Lý Thực Đơn Quán Ăn");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BG);

        // =============================================
        // HEADER + NAV BAR
        // =============================================
        JPanel pnlTop = new JPanel(new BorderLayout());
        pnlTop.setBackground(COLOR_BG);

        JLabel lblTitle = new JLabel("QUẢN LÝ THỰC ĐƠN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(15, 0, 10, 0));
        pnlTop.add(lblTitle, BorderLayout.NORTH);

        JPanel pnlNavBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        pnlNavBar.setBackground(COLOR_BG);

        JButton btnTabThucDon = createNavButton("Thực Đơn Quán", true);
        JButton btnTabDonHang = createNavButton("Đơn Hàng Quán", false);
        JButton btnTabThongKe = createNavButton("Thống Kê", false);

        btnTabDonHang.addActionListener(e -> {
            new FrmQuanLyDonHang(currentUser).setVisible(true);
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

        // =============================================
        // PANEL TRÁI: Form nhập + Nút chức năng
        // =============================================
        JPanel pnlLeft = new JPanel(new BorderLayout(0, 16));
        pnlLeft.setBackground(COLOR_BG);
        pnlLeft.setPreferredSize(new Dimension(320, 0));
        pnlLeft.setBorder(new EmptyBorder(10, 10, 10, 0));

        // -- Form nhập liệu: dùng GridBagLayout để kiểm soát chiều cao từng hàng --
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(COLOR_CARD);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50)),
                "Thông tin món ăn",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), COLOR_TEXT));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10); // padding nhỏ gọn
        gbc.anchor = GridBagConstraints.WEST;

        // Chiều cao cố định cho mỗi input
        Dimension inputSize = new Dimension(160, 28);
        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Hàng 0: Mã SP
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        JLabel lblMaSP = new JLabel("Mã Sản Phẩm:");
        lblMaSP.setFont(labelFont);
        lblMaSP.setForeground(COLOR_TEXT);
        pnlInput.add(lblMaSP, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtMaSP = new JTextField();
        txtMaSP.setFont(inputFont);
        txtMaSP.setPreferredSize(inputSize);
        txtMaSP.setBackground(new Color(50, 50, 50));
        txtMaSP.setForeground(COLOR_TEXT);
        txtMaSP.setCaretColor(COLOR_TEXT);
        txtMaSP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(3, 6, 3, 6)));
        pnlInput.add(txtMaSP, gbc);

        // Hàng 1: Tên SP
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        JLabel lblTenSP = new JLabel("Tên Món Ăn:");
        lblTenSP.setFont(labelFont);
        lblTenSP.setForeground(COLOR_TEXT);
        pnlInput.add(lblTenSP, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtTenSP = new JTextField();
        txtTenSP.setFont(inputFont);
        txtTenSP.setPreferredSize(inputSize);
        txtTenSP.setBackground(new Color(50, 50, 50));
        txtTenSP.setForeground(COLOR_TEXT);
        txtTenSP.setCaretColor(COLOR_TEXT);
        txtTenSP.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(3, 6, 3, 6)));
        pnlInput.add(txtTenSP, gbc);

        // Hàng 2: Giá
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        JLabel lblGia = new JLabel("Giá Bán (VNĐ):");
        lblGia.setFont(labelFont);
        lblGia.setForeground(COLOR_TEXT);
        pnlInput.add(lblGia, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        txtGia = new JTextField();
        txtGia.setFont(inputFont);
        txtGia.setPreferredSize(inputSize);
        txtGia.setBackground(new Color(50, 50, 50));
        txtGia.setForeground(COLOR_TEXT);
        txtGia.setCaretColor(COLOR_TEXT);
        txtGia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                new EmptyBorder(3, 6, 3, 6)));
        pnlInput.add(txtGia, gbc);

        // Hàng 3: Trạng thái
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        JLabel lblTrangThai = new JLabel("Trạng Thái:");
        lblTrangThai.setFont(labelFont);
        lblTrangThai.setForeground(COLOR_TEXT);
        pnlInput.add(lblTrangThai, gbc);

        gbc.gridx = 1; gbc.weightx = 1;
        cbTrangThai = new JComboBox<>(new String[]{"CÒN HÀNG", "HẾT HÀNG"});
        cbTrangThai.setFont(inputFont);
        cbTrangThai.setBackground(new Color(50, 50, 50));
        cbTrangThai.setForeground(Color.BLACK);
        cbTrangThai.setPreferredSize(inputSize);
        pnlInput.add(cbTrangThai, gbc);

        pnlLeft.add(pnlInput, BorderLayout.NORTH);

        // -- Panel Nút: canh giữa, nút to hơn --
        JPanel pnlButtons = new JPanel(new GridLayout(3, 1, 12, 12));
        pnlButtons.setBackground(COLOR_CARD);
        pnlButtons.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50)),
                new EmptyBorder(16, 20, 16, 20)));

        btnThem   = createButton("Thêm");
        btnSua    = createButton("Sửa");
        btnXoa    = createButton("Xóa");

        // Chiều cao nút lớn hơn (~30% so với cũ)
        Dimension btnSize = new Dimension(240, 44);
        btnThem.setPreferredSize(btnSize);
        btnSua.setPreferredSize(btnSize);
        btnXoa.setPreferredSize(btnSize);

        pnlButtons.add(btnThem);
        pnlButtons.add(btnSua);
        pnlButtons.add(btnXoa);

        // Bọc pnlButtons vào wrapper để canh giữa theo chiều dọc
        JPanel pnlBtnWrapper = new JPanel(new GridBagLayout());
        pnlBtnWrapper.setBackground(COLOR_BG);
        pnlBtnWrapper.add(pnlButtons);
        pnlLeft.add(pnlBtnWrapper, BorderLayout.CENTER);

        add(pnlLeft, BorderLayout.WEST);

        // =============================================
        // PANEL PHẢI: Bảng dữ liệu
        // =============================================
        String[] columns = {"Mã SP", "Tên Món", "Giá Bán", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableMenu = new JTable(tableModel);
        tableMenu.setRowHeight(30);
        tableMenu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableMenu.setBackground(COLOR_CARD);
        tableMenu.setForeground(COLOR_TEXT);
        tableMenu.setGridColor(new Color(50, 50, 50));
        tableMenu.setSelectionBackground(new Color(50, 80, 50));

        javax.swing.table.DefaultTableCellRenderer headerRenderer =
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        c.setBackground(new Color(220, 220, 220));
                        c.setForeground(Color.BLACK);
                        c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        return c;
                    }
                };
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableMenu.getColumnModel().getColumnCount(); i++) {
            tableMenu.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        tableMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tableMenu.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    txtGia.setText(tableModel.getValueAt(row, 2).toString());
                    cbTrangThai.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtMaSP.setEnabled(false);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableMenu);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50)),
                "Danh sách món ăn",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT));
        scrollPane.getViewport().setBackground(COLOR_BG);
        add(scrollPane, BorderLayout.CENTER);

        addEvents();
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(0, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.CENTER);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 0, 0));
            }
        });
        return btn;
    }

    private JButton createNavButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleButton(btn, isActive ? COLOR_PRIMARY : COLOR_CARD, COLOR_PRIMARY, new Dimension(150, 38));
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

    private void loadData() {
        tableModel.setRowCount(0);
        String quanAnId = new ProcessData.NguoiDungDAO().layQuanAnId(currentUser.getNguoiDungId());
        List<SanPham> list = sanPhamBLL.layDanhSachSanPhamTheoQuanTatCa(quanAnId);

        for (SanPham sp : list) {
            tableModel.addRow(new Object[]{
                    sp.getSanPhamId(), sp.getTenSanPham(),
                    sp.getGia(), sp.getTrangThai()
            });
        }
    }

    private void lamMoiForm() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtMaSP.setEnabled(true);
        cbTrangThai.setSelectedIndex(0);
        tableMenu.clearSelection();
    }

    private void addEvents() {
        // Thêm
        btnThem.addActionListener(e -> {
            try {
                SanPham sp = new SanPham();
                sp.setSanPhamId(txtMaSP.getText().trim());
                String quanAnId = new ProcessData.NguoiDungDAO().layQuanAnId(currentUser.getNguoiDungId());
                if (quanAnId == null) {
                    JOptionPane.showMessageDialog(this,
                            "Không tìm thấy quán ăn của bạn trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                sp.setQuanAnId(quanAnId);
                sp.setDanhMucId(1);
                sp.setTenSanPham(txtTenSP.getText().trim());
                sp.setGia(Double.parseDouble(txtGia.getText().trim()));
                sp.setTrangThai(cbTrangThai.getSelectedItem().toString());

                if (sanPhamBLL.themSanPham(sp)) {
                    JOptionPane.showMessageDialog(this, "Thêm món thành công!");
                    loadData();
                    lamMoiForm();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Mã sản phẩm đã tồn tại hoặc lỗi dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập giá là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Sửa
        btnSua.addActionListener(e -> {
            if (tableMenu.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một món ăn trên bảng để sửa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try {
                SanPham sp = new SanPham();
                sp.setSanPhamId(txtMaSP.getText().trim());
                sp.setTenSanPham(txtTenSP.getText().trim());
                sp.setGia(Double.parseDouble(txtGia.getText().trim()));
                sp.setTrangThai(cbTrangThai.getSelectedItem().toString());

                if (sanPhamBLL.capNhatSanPham(sp)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật món ăn thành công!");
                    loadData();
                    lamMoiForm(); // Clear các ô nhập sau khi sửa
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Lỗi khi cập nhật. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Giá bán phải là một số hợp lệ!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Xóa
        btnXoa.addActionListener(e -> {
            if (tableMenu.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một món ăn trên bảng để xóa!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String maSP  = txtMaSP.getText().trim();
            String tenSP = txtTenSP.getText().trim();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa món '" + tenSP + "' không?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (sanPhamBLL.xoaSanPham(maSP)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa món ăn thành công!");
                    loadData();
                    lamMoiForm();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa món ăn này!\nNguyên nhân: Món ăn có thể đang nằm trong lịch sử đơn hàng.",
                            "Lỗi ràng buộc dữ liệu", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}