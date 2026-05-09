package GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp PanelBoGoc giúp tạo ra các vùng chứa (container) có góc bo tròn,
 * phù hợp với thiết kế giao diện hiện đại.
 */
public class PanelBoGoc extends JPanel {
    private int banKinhGoc = 20; // Độ cong của góc
    private Color mauNen;

    public PanelBoGoc(int banKinhGoc, Color mauNen) {
        this.banKinhGoc = banKinhGoc;
        this.mauNen = mauNen;
        setOpaque(false); // Quan trọng: làm trong suốt nền mặc định để vẽ nền bo góc
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Bật chế độ khử răng cưa (Anti-aliasing) để các đường cong mịn màng hơn
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(mauNen);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), banKinhGoc, banKinhGoc);
    }
}