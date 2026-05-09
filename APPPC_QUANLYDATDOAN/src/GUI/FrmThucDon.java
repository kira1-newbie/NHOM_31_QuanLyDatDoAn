package GUI;

import BusinessLogic.GioHangBLL;
import BusinessLogic.NguoiDungBLL;
import BusinessLogic.SanPhamBLL;
import SCHEMA.NguoiDung;
import SCHEMA.SanPham;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

public class FrmThucDon extends JFrame {

    private NguoiDung currentUser;
    private SanPhamBLL sanPhamBLL = new SanPhamBLL();
    private GioHangBLL gioHangBLL = new GioHangBLL();
    private NguoiDungBLL nguoiDungBLL = new NguoiDungBLL();

    private JPanel pnlMenuContainer;
    private JPanel pnlQuanAnContainer; // Cột trái

    // --- Dark Theme Palette ---
    private final Color COLOR_BG = new Color(24, 24, 24); // #181818
    private final Color COLOR_CARD = new Color(36, 36, 36); // #242424
    private final Color COLOR_PRIMARY = new Color(0, 195, 0); // #00C300
    private final Color COLOR_TEXT = Color.WHITE;

    private JPanel selectedQuanAnCard = null;
    private FrmGioHang frmGioHangInstance = null;

    public FrmThucDon(NguoiDung user) {
        this.currentUser = user;
        initUI();
        loadDanhSachQuan();
    }

    private void initUI() {
        setTitle("Thực Đơn - Xin chào " + currentUser.getHoTen());
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout());

        // HEADER
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG);
        pnlHeader.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)),
                new EmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel("Thực Đơn");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(COLOR_PRIMARY);
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JButton btnXemGioHang = new JButton("Xem Giỏ Hàng") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnXemGioHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXemGioHang.setBackground(new Color(0, 0, 0));
        btnXemGioHang.setForeground(Color.WHITE);
        btnXemGioHang.setContentAreaFilled(false);
        btnXemGioHang.setFocusPainted(false);
        btnXemGioHang.setOpaque(true);
        btnXemGioHang.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btnXemGioHang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemGioHang.setPreferredSize(new Dimension(160, 40));

        btnXemGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXemGioHang.setBackground(new Color(30, 30, 30));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXemGioHang.setBackground(new Color(0, 0, 0));
            }
        });

        btnXemGioHang.addActionListener(e -> {
            if (frmGioHangInstance == null || !frmGioHangInstance.isDisplayable()) {
                frmGioHangInstance = new FrmGioHang(currentUser);
            } else {
                frmGioHangInstance.loadDataToTable();
            }
            frmGioHangInstance.setVisible(true);
            frmGioHangInstance.toFront();
        });

        // Nút Xem Đơn Hàng
        JButton btnXemDonHang = new JButton("Xem Đơn Hàng") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnXemDonHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnXemDonHang.setBackground(new Color(0, 0, 0));
        btnXemDonHang.setForeground(Color.WHITE);
        btnXemDonHang.setContentAreaFilled(false);
        btnXemDonHang.setFocusPainted(false);
        btnXemDonHang.setOpaque(true);
        btnXemDonHang.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btnXemDonHang.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXemDonHang.setPreferredSize(new Dimension(160, 40));

        btnXemDonHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXemDonHang.setBackground(new Color(30, 30, 30));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXemDonHang.setBackground(new Color(0, 0, 0));
            }
        });

        btnXemDonHang.addActionListener(e -> {
            new FrmDonHangCuaToi(currentUser).setVisible(true);
        });

        // Gộp 2 nút vào chung 1 panel
        JPanel pnlHeaderButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlHeaderButtons.setOpaque(false);
        pnlHeaderButtons.add(btnXemDonHang);
        pnlHeaderButtons.add(btnXemGioHang);

        pnlHeader.add(pnlHeaderButtons, BorderLayout.EAST);
        add(pnlHeader, BorderLayout.NORTH);

        // ==========================================
        // NỘI DUNG CHÍNH CHIA 2 CỘT
        // ==========================================
        JPanel pnlMainBody = new JPanel(new BorderLayout(20, 0));
        pnlMainBody.setBackground(COLOR_BG);
        pnlMainBody.setBorder(new EmptyBorder(20, 20, 20, 20));

        // CỘT TRÁI: DANH SÁCH QUÁN ĂN
        pnlQuanAnContainer = new JPanel();
        pnlQuanAnContainer.setLayout(new BoxLayout(pnlQuanAnContainer, BoxLayout.Y_AXIS));
        pnlQuanAnContainer.setBackground(COLOR_BG);

        JScrollPane scrollQuanAn = new JScrollPane(pnlQuanAnContainer);
        scrollQuanAn.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)),
                "CÁC QUÁN ĂN", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));
        scrollQuanAn.getVerticalScrollBar().setUnitIncrement(16);
        scrollQuanAn.getViewport().setBackground(COLOR_BG);
        scrollQuanAn.setPreferredSize(new Dimension(320, 0));

        // CỘT PHẢI: DANH SÁCH MÓN ĂN
        pnlMenuContainer = new JPanel(new GridLayout(0, 4, 20, 20)); // Giảm xuống 4 cột vì cột trái chiếm chỗ
        pnlMenuContainer.setBackground(COLOR_BG);

        JPanel wrapperMenu = new JPanel(new BorderLayout());
        wrapperMenu.setBackground(COLOR_BG);
        wrapperMenu.add(pnlMenuContainer, BorderLayout.NORTH);

        JScrollPane scrollMenu = new JScrollPane(wrapperMenu);
        scrollMenu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)),
                "THỰC ĐƠN", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), COLOR_PRIMARY));
        scrollMenu.getVerticalScrollBar().setUnitIncrement(16);
        scrollMenu.getViewport().setBackground(COLOR_BG);

        pnlMainBody.add(scrollQuanAn, BorderLayout.WEST);
        pnlMainBody.add(scrollMenu, BorderLayout.CENTER);

        add(pnlMainBody, BorderLayout.CENTER);
    }

    private void loadDanhSachQuan() {
        pnlQuanAnContainer.removeAll();

        List<NguoiDung> danhSachQuan = nguoiDungBLL.layDanhSachQuanAn();

        if (danhSachQuan == null || danhSachQuan.isEmpty()) {
            JLabel lblEmpty = new JLabel("Chưa có quán ăn nào.");
            lblEmpty.setForeground(Color.LIGHT_GRAY);
            lblEmpty.setBorder(new EmptyBorder(10, 10, 10, 10));
            pnlQuanAnContainer.add(lblEmpty);
        } else {
            // Lấy danh sách QuanAnId thực tế trong CSDL (để fix lỗi lệch ID giữa bảng
            // NguoiDung và SanPham)
            List<SanPham> allSp = sanPhamBLL.layDanhSachMenu();
            java.util.List<String> realQuanAnIds = new java.util.ArrayList<>();
            if (allSp != null) {
                for (SanPham sp : allSp) {
                    if (!realQuanAnIds.contains(sp.getQuanAnId())) {
                        realQuanAnIds.add(sp.getQuanAnId());
                    }
                }
            }

            boolean isFirst = true;
            for (int i = 0; i < danhSachQuan.size(); i++) {
                NguoiDung quan = danhSachQuan.get(i);

                // Ánh xạ ID quán ăn: Nếu CSDL có sẵn QA00001, QA00002 thì gán lần lượt cho các
                // quán.
                // Nếu không, tạo ID giả lập mặc định.
                String mappedQuanAnId = (i < realQuanAnIds.size()) ? realQuanAnIds.get(i)
                        : "QA" + quan.getNguoiDungId().substring(2);

                JPanel card = createQuanAnCard(quan, mappedQuanAnId);
                pnlQuanAnContainer.add(card);
                pnlQuanAnContainer.add(Box.createVerticalStrut(10)); // Khoảng cách giữa các quán

                // Mặc định chọn quán đầu tiên
                if (isFirst) {
                    chonQuanAn(card, mappedQuanAnId);
                    isFirst = false;
                }
            }
        }

        pnlQuanAnContainer.revalidate();
        pnlQuanAnContainer.repaint();
    }

    private JPanel createQuanAnCard(NguoiDung quan, String mappedQuanAnId) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                new EmptyBorder(12, 14, 12, 14)));

        // Giới hạn kích thước: width theo scroll pane, height cố định
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        card.setPreferredSize(new Dimension(0, 55));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Tên quán căn giữa
        JLabel lblName = new JLabel(quan.getHoTen(), SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblName.setForeground(COLOR_TEXT);

        card.add(lblName, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                chonQuanAn(card, mappedQuanAnId);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (selectedQuanAnCard != card)
                    card.setBackground(new Color(50, 50, 50));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (selectedQuanAnCard != card)
                    card.setBackground(COLOR_CARD);
            }
        });

        return card;
    }

    private void chonQuanAn(JPanel card, String quanAnId) {
        // Hủy chọn thẻ cũ
        if (selectedQuanAnCard != null) {
            selectedQuanAnCard.setBackground(COLOR_CARD);
            selectedQuanAnCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
                    new EmptyBorder(10, 10, 10, 10)));
        }

        // Chọn thẻ mới
        selectedQuanAnCard = card;
        selectedQuanAnCard.setBackground(new Color(20, 60, 20)); // Màu xanh lá tối nổi bật
        selectedQuanAnCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 2),
                new EmptyBorder(9, 9, 9, 9)));

        // Tải thực đơn của quán đó
        loadMenuQuan(quanAnId);
    }

    private void loadMenuQuan(String quanAnId) {
        pnlMenuContainer.removeAll();

        List<SanPham> danhSachSP = sanPhamBLL.layDanhSachSanPhamTheoQuan(quanAnId);

        if (danhSachSP == null || danhSachSP.isEmpty()) {
            JLabel lblEmpty = new JLabel("Quán này hiện chưa có món ăn nào.");
            lblEmpty.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            lblEmpty.setForeground(Color.LIGHT_GRAY);
            lblEmpty.setHorizontalAlignment(SwingConstants.CENTER);
            pnlMenuContainer.add(lblEmpty);
        } else {
            for (SanPham sp : danhSachSP) {
                pnlMenuContainer.add(createProductCard(sp));
            }
        }

        pnlMenuContainer.revalidate();
        pnlMenuContainer.repaint();
    }

    private JPanel createProductCard(SanPham sp) {
        RoundedPanel card = new RoundedPanel(20, COLOR_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Tên món ăn
        JLabel lblName = new JLabel(
                "<html><body style='text-align: center; width: 200px;'>" + sp.getTenSanPham() + "</body></html>");
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(COLOR_TEXT);
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Giá tiền
        DecimalFormat df = new DecimalFormat("#,###");
        JLabel lblPrice = new JLabel(df.format(sp.getGia()) + " VNĐ");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(COLOR_PRIMARY);
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Số lượng
        JPanel pnlQuantity = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlQuantity.setOpaque(false);
        pnlQuantity.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSL = new JLabel("Số lượng:");
        lblSL.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSL.setForeground(Color.LIGHT_GRAY);

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner spinSoLuong = new JSpinner(spinnerModel);
        spinSoLuong.setPreferredSize(new Dimension(60, 30));
        spinSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Tùy chỉnh JSpinner cho Dark Theme
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinSoLuong.getEditor();
        editor.getTextField().setBackground(COLOR_BG);
        editor.getTextField().setForeground(Color.WHITE);
        editor.getTextField().setCaretColor(Color.WHITE);

        pnlQuantity.add(lblSL);
        pnlQuantity.add(spinSoLuong);

        // Nút Thêm vào giỏ
        JButton btnAdd = new JButton("Thêm vào giỏ") {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdd.setBackground(new Color(0, 0, 0));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setContentAreaFilled(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARY, 1));
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(160, 44));
        btnAdd.setPreferredSize(new Dimension(160, 44));

        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdd.setBackground(COLOR_PRIMARY);
                btnAdd.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdd.setBackground(new Color(0, 0, 0));
                btnAdd.setForeground(Color.WHITE);
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int soLuong = (int) spinSoLuong.getValue();

                // Lấy đúng giỏ hàng theo KhachHangId (không tự ghép mã, tránh nhầm giỏ của người khác)
                String gioHangId = gioHangBLL.layGioHangIdTheoKhachHang(currentUser.getNguoiDungId());
                String targetQuanAnId = sp.getQuanAnId();

                if (gioHangId == null) {
                    // Khách mới chưa có giỏ -> tạo giỏ theo quán đang chọn
                    gioHangId = gioHangBLL.taoGioHangMoi(currentUser.getNguoiDungId(), targetQuanAnId);
                    if (gioHangId == null) {
                        JOptionPane.showMessageDialog(card, "Không thể tạo giỏ hàng. Vui lòng thử lại.", "Lỗi hệ thống",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    // Đã có giỏ -> chỉ cho phép 1 quán / 1 giỏ
                    String cartQuanAnId = gioHangBLL.layQuanAnIdTheoGioHang(gioHangId);
                    if (cartQuanAnId != null && !cartQuanAnId.equals(targetQuanAnId)) {
                        if (gioHangBLL.gioHangCoMon(gioHangId)) {
                            JOptionPane.showMessageDialog(card,
                                    "Bạn chỉ được đặt món của 1 quán trong 1 lần.\n"
                                            + "Vui lòng xóa hết món trong giỏ hiện tại trước khi chọn quán khác.",
                                    "Không thể thêm món",
                                    JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        // Giỏ đang trống -> cho phép đổi quán của giỏ
                        gioHangBLL.capNhatQuanAnChoGio(gioHangId, targetQuanAnId);
                    }
                }

                boolean success = gioHangBLL.themVaoGio(gioHangId, sp.getSanPhamId(), soLuong);

                if (success) {
                    JOptionPane.showMessageDialog(card,
                            "Đã thêm " + soLuong + " x [" + sp.getTenSanPham() + "] vào giỏ hàng!", "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(card, "Lỗi khi thêm vào giỏ. Vui lòng thử lại.", "Lỗi hệ thống",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add to card
        card.add(Box.createVerticalStrut(15));
        card.add(lblName);
        card.add(Box.createVerticalStrut(10));
        card.add(lblPrice);
        card.add(Box.createVerticalStrut(10));
        card.add(pnlQuantity);
        card.add(Box.createVerticalStrut(15));
        card.add(btnAdd);
        card.add(Box.createVerticalStrut(15));

        return card;
    }

    class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color bgColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.cornerRadius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Shadow
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);

            // Background
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, cornerRadius, cornerRadius);
            g2.dispose();
        }
    }
}