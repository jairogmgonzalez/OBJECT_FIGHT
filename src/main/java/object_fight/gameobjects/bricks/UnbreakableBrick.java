package object_fight.gameobjects.bricks;

/**
 * @author jairo
 */

import object_fight.gameobjects.GameObject;
import object_fight.utils.SoundManager;
import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.*;

/**
 * Clase que epresenta un ladrillo irrompible en el juego.
 *
 */
public class UnbreakableBrick extends Brick {

    /**
     * Constructor del ladrillo irrompible.
     *
     * @param position Posición inicial del ladrillo
     * @param width Ancho del ladrillo
     * @param height Alto del ladrillo
     */
    public UnbreakableBrick(Vector2D position, int width, int height) {
        super(position, width, height);
    }

    /**
     * Sobrescribe el manejo de colisiones para que el ladrillo no pueda ser
     * destruido.
     *
     * @param other Objeto con el que colisiona
     */
    @Override
    public void handleCollision(GameObject other) {
        // Los ladrillos irrompibles no reaccionan a las colisiones
        SoundManager.getInstance().playSound("ballCollision"); // Simplemente se reproduce el sonido de colisión de la bola
    }

    /**
     * Renderiza el ladrillo irrompible con un diseño especial.
     *
     * @param g Contexto gráfico para dibujar
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int x = (int) position.getX();
        int y = (int) position.getY();

        // Dibujar el fondo del ladrillo
        g2d.setColor(UNBREAKABLE_BRICK_BASE_COLOR);
        g2d.fillRect(x, y, width, height);

        // Dibujar el borde grueso
        g2d.setColor(UNBREAKABLE_BRICK_HIGHLIGHT_COLOR);
        g2d.setStroke(new BasicStroke(UNBREAKABLE_BRICK_BORDER_WIDTH));
        g2d.drawRect(x, y, width, height);

        // Dibujar el patrón de líneas diagonales
        g2d.setColor(UNBREAKABLE_BRICK_TEXTURE_COLOR);

        // Dibujar líneas diagonales para crear textura
        for (int i = 0; i < width + height; i += UNBREAKABLE_BRICK_LINE_SPACING) {
            int x1 = x + i;
            int y1 = y;
            int x2 = x;
            int y2 = y + i;

            // Ajustar las coordenadas para mantener las líneas dentro del ladrillo
            if (x1 > x + width) {
                x1 = x + width;
            }
            if (y2 > y + height) {
                y2 = y + height;
            }

            g2d.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * Se actualiza el estado del ladrillo irrompible.
     */
    @Override
    public void update() {
        super.update();
    }
}

