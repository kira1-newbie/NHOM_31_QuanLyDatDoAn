package GUI;

import BusinessLogic.NguoiDungBLL;
import SCHEMA.NguoiDung;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmChonTaiKhoan extends JDialog {
    private NguoiDung selectedUser = null;
    private int targetRoleId;
    private NguoiDungBLL nguoiDungBLL = new NguoiDungBLL();

    private JTable tableUsers;
    private DefaultTableModel tableModel;
    private JButton btnChon, btnHuy;

    private final Color COLOR_BG = new Color(24, 24, 24);
    private final Color COLOR_CARD = new Color(36, 36, 36);
    private final Color COLOR_PRIMARY = new Color(0, 195, 0);
    private final Color COLOR_TEXT = Color.WHITE;

    public FrmChonTaiKhoan(Frame parent, int roleId, String title) {
        super(parent, title, true);
        this.targetRoleId = roleId;
        initUI();
        loadData();
    }

    private void initUI() {
        setSize(600, 400);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BG);

        JLabel lblTitle = new JLabel("CHỌN TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(15, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"Mã User", "Họ Tên", "Số Điện Thoại", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableUsers = new JTable(tableModel);
        tableUsers.setRowHeight(30);
        tableUsers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableUsers.setBackground(COLOR_CARD);
        tableUsers.setForeground(COLOR_TEXT);
        tableUsers.setSelectionBackground(new Color(50, 80, 50));
        tableUsers.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableUsers);
        scrollPane.getViewport().setBackground(COLOR_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlBottom.setBackground(COLOR_BG);

        btnChon = createButton("Chọn Tài Khoản");
        btnHuy = createButton("Hủy Bỏ");

        btnChon.addActionListener(e -> xuLyChon());
        btnHuy.addActionListener(e -> dispose());

        pnlBottom.add(btnHuy);
        pnlBottom.add(btnChon);
        add(pnlBottom, BorderLayout.SOUTH);
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
        btn.setPreferredSize(new Dimension(150, 35));

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
        List<NguoiDung> danhSach = nguoiDungBLL.layDanhSachTaiKhoan();
        for (NguoiDung nd : danhSach) {
            if (nd.getRoleId() == targetRoleId) {
                tableModel.addRow(new Object[]{
                        nd.getNguoiDungId(),
                        nd.getHoTen(),
                        nd.getSoDienThoai(),
                        nd.getTrangThai()
                });
            }
        }
    }

    private void xuLyChon() {
        int row = tableUsers.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String userId = tableModel.getValueAt(row, 0).toString();
        List<NguoiDung> danhSach = nguoiDungBLL.layDanhSachTaiKhoan();
        for (NguoiDung nd : danhSach) {
            if (nd.getNguoiDungId().equals(userId)) {
                selectedUser = nd;
                break;
            }
        }
        dispose();
    }

    public NguoiDung getSelectedUser() {
        return selectedUser;
    }
}
