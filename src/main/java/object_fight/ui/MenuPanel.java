package object_fight.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author jairo
 */
public class MenuPanel extends javax.swing.JPanel {

    private Image backgroundImage;

    /**
     * Constructor para inicializar el panel, cargar la imagen de fondo y configurar
     * las imágenes de los botones.
     */
    public MenuPanel() {
        initComponents();
        backgroundImage = new ImageIcon(getClass().getResource("/backgrounds/game/GameBackground.jpg")).getImage();
        initializeButtonImages();
    }

    /**
     * Sobrescribe el método `paintComponent` para dibujar la imagen de fondo escalada
     * al tamaño del panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Carga las imágenes de los botones, las escala al tamaño deseado y aplica configuraciones
     * visuales a los botones.
     */
    private void initializeButtonImages() {
        try {
            // Carga las imágenes originales de los botones
            BufferedImage imgNewGame = ImageIO.read(getClass().getResource("/backgrounds/buttons/NewGame.png"));
            BufferedImage imgExit = ImageIO.read(getClass().getResource("/backgrounds/buttons/Exit.png"));

            // Configuración de tamaño para el botón "New Game"
            int newGameHeight = 80;  // Altura para el botón de "New Game"
            double newGameRatio = (double) imgNewGame.getWidth() / imgNewGame.getHeight();
            int newGameWidth = (int) (newGameHeight * newGameRatio);

            // Configuración de tamaño para el botón "Exit"
            int exitHeight = 80;  // Altura para el botón "Exit"
            double exitRatio = (double) imgExit.getWidth() / imgExit.getHeight();
            int exitWidth = (int) (exitHeight * exitRatio);

            // Asigna el tamaño preferido a los botones
            jButtonNuevaPartida.setPreferredSize(new Dimension(newGameWidth, newGameHeight));
            jButtonSalir.setPreferredSize(new Dimension(exitWidth, exitHeight));

            // Escala las imágenes al tamaño deseado
            Image newGameScaled = imgNewGame.getScaledInstance(newGameWidth, newGameHeight, Image.SCALE_SMOOTH);
            Image exitScaled = imgExit.getScaledInstance(exitWidth, exitHeight, Image.SCALE_SMOOTH);

            // Aplica las imágenes escaladas como iconos de los botones
            jButtonNuevaPartida.setIcon(new ImageIcon(newGameScaled));
            jButtonSalir.setIcon(new ImageIcon(exitScaled));

            // Configuración visual común para ambos botones
            for (JButton button : new JButton[]{jButtonNuevaPartida, jButtonSalir}) {
                button.setContentAreaFilled(false);
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                button.setOpaque(false);
                button.setHorizontalAlignment(JButton.CENTER);
                button.setVerticalAlignment(JButton.CENTER);
            }

            revalidate();
            repaint();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajusta una imagen al tamaño especificado, manteniendo su proporción.
     *
     * @param imagePath La ruta de la imagen a ajustar.
     * @param buttonWidth El ancho deseado del botón.
     * @param buttonHeight La altura deseada del botón.
     * @return Un `ImageIcon` con la imagen ajustada.
     */
    private ImageIcon adjustImage(String imagePath, int buttonWidth, int buttonHeight) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(imagePath));
            if (img == null) {
                System.out.println("No se pudo cargar la imagen: " + imagePath);
                return null;
            }

            // Mantener la proporción original de la imagen
            double imageRatio = (double) img.getWidth() / img.getHeight();
            int newWidth = buttonWidth;
            int newHeight = buttonHeight;

            if (imageRatio > 1) {
                // Imagen más ancha que alta
                newHeight = (int) (buttonWidth / imageRatio);
            } else {
                // Imagen más alta que ancha
                newWidth = (int) (buttonHeight * imageRatio);
            }

            Image adjustedImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            return new ImageIcon(adjustedImage);

        } catch (IOException e) {
            System.err.println("Error al cargar la imagen: " + imagePath);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Agrega un `ActionListener` al botón de "Nueva Partida" (New Game).
     *
     * @param listener El listener que se ejecutará al hacer clic en el botón.
     */
    public void addIniciarPartidaAction(ActionListener listener) {
        jButtonNuevaPartida.addActionListener(listener);
    }

    /**
     * Agrega un `ActionListener` al botón de "Salir" (Exit).
     *
     * @param listener El listener que se ejecutará al hacer clic en el botón.
     */
    public void addSalirAction(ActionListener listener) {
        jButtonSalir.addActionListener(listener);
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelTitulo2 = new javax.swing.JLabel();
        jPanelOpciones = new javax.swing.JPanel();
        jPanelIniciarPartida = new javax.swing.JPanel();
        jButtonNuevaPartida = new javax.swing.JButton();
        jPanelSalir = new javax.swing.JPanel();
        jButtonSalir = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(800, 600));
        setLayout(new java.awt.BorderLayout());

        jPanelTitulo.setOpaque(false);
        jPanelTitulo.setPreferredSize(new java.awt.Dimension(200, 200));
        jPanelTitulo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 60));

        jLabelTitulo.setFont(new java.awt.Font("Berlin Sans FB Demi", 3, 66)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTitulo.setText("OBJECT");
        jPanelTitulo.add(jLabelTitulo);

        jLabelTitulo2.setFont(new java.awt.Font("Berlin Sans FB Demi", 3, 66)); // NOI18N
        jLabelTitulo2.setForeground(new java.awt.Color(0, 255, 255));
        jLabelTitulo2.setText("FIGHT");
        jPanelTitulo.add(jLabelTitulo2);

        add(jPanelTitulo, java.awt.BorderLayout.PAGE_START);

        jPanelOpciones.setOpaque(false);
        jPanelOpciones.setLayout(new java.awt.GridLayout(2, 0));

        jPanelIniciarPartida.setOpaque(false);
        jPanelIniciarPartida.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jButtonNuevaPartida.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButtonNuevaPartida.setMaximumSize(new java.awt.Dimension(400, 150));
        jButtonNuevaPartida.setMinimumSize(new java.awt.Dimension(400, 150));
        jButtonNuevaPartida.setPreferredSize(new java.awt.Dimension(200, 80));
        jPanelIniciarPartida.add(jButtonNuevaPartida);

        jPanelOpciones.add(jPanelIniciarPartida);

        jPanelSalir.setOpaque(false);
        jPanelSalir.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jButtonSalir.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jButtonSalir.setMaximumSize(new java.awt.Dimension(400, 150));
        jButtonSalir.setMinimumSize(new java.awt.Dimension(400, 150));
        jButtonSalir.setPreferredSize(new java.awt.Dimension(200, 80));
        jPanelSalir.add(jButtonSalir);

        jPanelOpciones.add(jPanelSalir);

        add(jPanelOpciones, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonNuevaPartida;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTitulo2;
    private javax.swing.JPanel jPanelIniciarPartida;
    private javax.swing.JPanel jPanelOpciones;
    private javax.swing.JPanel jPanelSalir;
    private javax.swing.JPanel jPanelTitulo;
    // End of variables declaration//GEN-END:variables
}

