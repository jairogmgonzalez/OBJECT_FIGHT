package object_fight.gameobjects.bricks;

/**
 * @author jairo
 */

import object_fight.gameobjects.Ball;
import object_fight.gameobjects.GameObject;
import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.*;

/**
 * Clase que representa un ladrillo resistente que necesita múltiples golpes
 * para ser destruido.
 *
 */
public class ResistantBrick extends Brick {

    // Contador de golpes restantes para destruir el ladrillo
    private int hitsRemaining;

    /**
     * Constructor del ladrillo resistente. Inicializa el ladrillo con la
     * cantidad máxima de golpes.
     *
     * @param position Posición inicial del ladrillo
     * @param width Ancho del ladrillo
     * @param height Alto del ladrillo
     */
    public ResistantBrick(Vector2D position, int width, int height) {
        super(position, width, height);
        this.hitsRemaining = RESISTANT_BRICK_INITIAL_HITS;
    }

    /**
     * Maneja las colisiones y reduce la resistencia del ladrillo. Cada vez que
     * la bola golpea el ladrillo, reduce el contador de golpes y destruye el
     * ladrillo cuando llega a cero.
     *
     * @param other Objeto con el que colisiona
     */
    @Override
    public void handleCollision(GameObject other) {
        if (other instanceof Ball && isAlive()) {
            hitsRemaining--;
            if (hitsRemaining <= 0) {
                setAlive(false);
            }
        }
    }

    /**
     * Renderiza el ladrillo con efectos visuales que cambian según el daño.
     *
     * @param g Contexto gráfico para dibujar
     */
    @Override
    public void render(Graphics g) {
        if (!isAlive()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        int x = (int) position.getX();
        int y = (int) position.getY();

        // Determinar el color base según la fase de daño
        Color[] crystalColors = {
                RESISTANT_BRICK_COLOR_PHASE1,
                RESISTANT_BRICK_COLOR_PHASE2,
                RESISTANT_BRICK_COLOR_PHASE3
        };
        int phase = Math.max(0, RESISTANT_BRICK_INITIAL_HITS - hitsRemaining);

        // Dibujar el fondo del ladrillo
        g2d.setColor(crystalColors[phase]);
        g2d.fillRect(x, y, width, height);

        // Aplicar degradado para efecto de profundidad
        GradientPaint gradient = new GradientPaint(
                x, y, RESISTANT_BRICK_GRADIENT_COLOR,
                x + width, y + height,
                crystalColors[phase].darker()
        );
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, height);

        // Dibujar patrón de líneas diagonales
        g2d.setColor(RESISTANT_BRICK_LINES_COLOR);
        for (int i = 0; i < width + height; i += RESISTANT_BRICK_LINE_SPACING) {
            int startX = x + i < x + width ? x + i : x + width;
            int startY = x + i < x + width ? y : y + (i - width);
            int endX = x + i - height > x ? x + i - height : x;
            int endY = x + i - height > x ? y + height : y + i;
            g2d.drawLine(startX, startY, endX, endY);
        }

        // Dibujar grietas según el daño recibido
        g2d.setColor(RESISTANT_BRICK_CRACKS_COLOR);
        if (hitsRemaining == 2) {
            // Una grieta central
            g2d.drawLine(x + width / 4, y + height / 4, x + width / 2, y + height / 2);
        } else if (hitsRemaining == 1) {
            // Múltiples grietas
            g2d.drawLine(x + width / 4, y + height / 4, x + width / 2, y + height / 2);
            g2d.drawLine(x + 3 * width / 4, y + height / 4, x + width / 2, y + height / 2);
            g2d.drawLine(x + width / 4, y + 3 * height / 4, x + width / 2, y + height / 2);
        }

        // Borde del ladrillo
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
    }

    /**
     * Obtiene el número de golpes restantes para destruir el ladrillo.
     *
     * @return Número de golpes restantes
     */
    public int getHitsRemaining() {
        return hitsRemaining;
    }
}
