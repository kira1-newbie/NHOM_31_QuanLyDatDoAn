package GUI;

import BusinessLogic.NguoiDungBLL;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmAdmin extends JFrame {

    private NguoiDung currentUser;
    private NguoiDungBLL nguoiDungBLL = new NguoiDungBLL();

    // Các thành phần UI cho Quản lý User
    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private JButton btnDoiTrangThai;


    // --- Dark Theme Palette ---
    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    public FrmAdmin(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("Admin Dashboard - Quản Lý Hệ Thống");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BG);

        // --- Header ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("ADMIN DASHBOARD");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        // Admin Menu Buttons
        JPanel pnlAdminMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlAdminMenu.setBackground(COLOR_BG);

        JButton btnThucDon = createMenuButton("Thực Đơn Quán");
        JButton btnDonHang = createMenuButton("Đơn Hàng Quán");
        JButton btnThongKe = createMenuButton("Thống Kê Doanh Thu");
        JButton btnShipper = createMenuButton("Cổng Shipper");

        btnThucDon.addActionListener(e -> moChucNangQuan(4, "Thực Đơn Quán"));
        btnDonHang.addActionListener(e -> moChucNangQuan(4, "Đơn Hàng Quán"));
        btnThongKe.addActionListener(e -> moChucNangQuan(4, "Thống Kê"));
        btnShipper.addActionListener(e -> moChucNangShipper());

        pnlAdminMenu.add(btnThucDon);
        pnlAdminMenu.add(btnDonHang);
        pnlAdminMenu.add(btnThongKe);
        pnlAdminMenu.add(btnShipper);

        pnlHeader.add(pnlAdminMenu, BorderLayout.SOUTH);
        add(pnlHeader, BorderLayout.NORTH);

        // --- Content (Quản lý User) ---
        JPanel pnlContent = new JPanel(new BorderLayout());
        pnlContent.setBackground(COLOR_BG);
        pnlContent.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50)),
                "Danh Sách Người Dùng",
                javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_TEXT));

        String[] columns = {"Mã User", "Họ Tên", "Email", "Số Điện Thoại", "Vai Trò (Role)", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableUsers = new JTable(tableModel);
        tableUsers.setRowHeight(30);
        tableUsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableUsers.setBackground(COLOR_CARD);
        tableUsers.setForeground(COLOR_TEXT);
        tableUsers.setGridColor(new Color(50, 50, 50));
        tableUsers.setSelectionBackground(new Color(50, 80, 50));
        tableUsers.setSelectionForeground(Color.WHITE);

        javax.swing.table.DefaultTableCellRenderer headerRenderer =
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus, int row, int column) {
                        Component c = super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);
                        c.setBackground(new Color(40, 40, 40));
                        c.setForeground(COLOR_PRIMARY);
                        c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        return c;
                    }
                };
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableUsers.getColumnModel().getColumnCount(); i++) {
            tableUsers.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(tableUsers);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        pnlContent.add(scrollPane, BorderLayout.CENTER);

        // --- Bottom (Nút chức năng QL User) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        pnlBottom.setBackground(COLOR_BG);

        btnDoiTrangThai = createActionButton("KHÓA / MỞ KHÓA TÀI KHOẢN");
        btnDoiTrangThai.addActionListener(e -> xuLyDoiTrangThai());

        pnlBottom.add(btnDoiTrangThai);
        pnlContent.add(pnlBottom, BorderLayout.SOUTH);

        add(pnlContent, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(new Color(0, 0, 0));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 40));

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

    private JButton createActionButton(String text) {
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
        btn.setPreferredSize(new Dimension(250, 40));

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

    private void loadData() {
        tableModel.setRowCount(0);
        List<NguoiDung> danhSach = nguoiDungBLL.layDanhSachTaiKhoan();

        for (NguoiDung nd : danhSach) {
            String tenRole = "Không xác định";
            switch (nd.getRoleId()) {
                case 1: tenRole = "ADMIN"; break;
                case 2: tenRole = "CUSTOMER"; break;
                case 3: tenRole = "SHIPPER"; break;
                case 4: tenRole = "RESTAURANT"; break;
            }

            tableModel.addRow(new Object[]{
                    nd.getNguoiDungId(),
                    nd.getHoTen(),
                    nd.getEmail(),
                    nd.getSoDienThoai(),
                    tenRole,
                    nd.getTrangThai()
            });
        }
    }

    private void xuLyDoiTrangThai() {
        int row = tableUsers.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng trên bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = tableModel.getValueAt(row, 0).toString();
        String trangThaiHienTai = tableModel.getValueAt(row, 5).toString();
        String role = tableModel.getValueAt(row, 4).toString();

        if (role.equals("ADMIN")) {
            JOptionPane.showMessageDialog(this, "Bạn không thể khóa tài khoản ADMIN!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn muốn đổi trạng thái của tài khoản " + userId + "?\nTrạng thái hiện tại: " + trangThaiHienTai,
                "Xác nhận", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (nguoiDungBLL.daoNguocTrangThai(userId, trangThaiHienTai)) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật trạng thái tài khoản thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra, vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moChucNangQuan(int roleId, String action) {
        FrmChonTaiKhoan dialog = new FrmChonTaiKhoan(this, roleId, "Chọn Quán Ăn");
        dialog.setVisible(true);
        NguoiDung selectedRes = dialog.getSelectedUser();
        if (selectedRes != null) {
            JFrame childForm = null;
            if (action.equals("Thực Đơn Quán")) {
                childForm = new FrmQuanLyQuan(selectedRes);
            } else if (action.equals("Đơn Hàng Quán")) {
                childForm = new FrmQuanLyDonHang(selectedRes);
            } else if (action.equals("Thống Kê")) {
                childForm = new FrmThongKe(selectedRes);
            }

            if (childForm != null) {
                this.setVisible(false);
                childForm.setVisible(true);
                childForm.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        FrmAdmin.this.setVisible(true);
                    }
                });
            }
        }
    }

    private void moChucNangShipper() {
        FrmChonTaiKhoan dialog = new FrmChonTaiKhoan(this, 3, "Chọn Shipper");
        dialog.setVisible(true);
        NguoiDung selectedShipper = dialog.getSelectedUser();
        if (selectedShipper != null) {
            this.setVisible(false);
            FrmShipper frm = new FrmShipper(selectedShipper);
            frm.setVisible(true);
            frm.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    FrmAdmin.this.setVisible(true);
                }
            });
        }
    }
}