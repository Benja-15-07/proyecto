import visual.*;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Si no está disponible, Swing utiliza su apariencia predeterminada.
            }
            Ventana ventana = new Ventana();
            new ControladorTorneo(ventana);
        });
    }
}
