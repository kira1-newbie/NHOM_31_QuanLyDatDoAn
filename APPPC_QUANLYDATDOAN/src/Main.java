import GUI.*;
import javax.swing.*;

public class Main
{
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Lỗi thiết lập giao diện: " + ex.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            new frmDANGNHAP().setVisible(true);
        });
    }
}
