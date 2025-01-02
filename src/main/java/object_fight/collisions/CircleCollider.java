package object_fight.collisions;


/**
 *
 * @author jairo
 */

import object_fight.utils.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import static object_fight.utils.Constants.COLLIDER_DEBUG_COLOR;

/**
 * Clase que representa un collider circular. Maneja las colisiones usando una
 * combinación de Rectangle para detección rápida y Ellipse2D para precisión.
 */
public class CircleCollider implements Collider {

    // Posición del collider
    private Vector2D position;

    // Radio del círculo
    private double radius;

    /**
     * Constructor del collider circular.
     *
     * @param position Vector2D que representa el centro del círculo
     * @param radius Radio del círculo en píxeles
     */
    public CircleCollider(Vector2D position, double radius) {
        this.position = position;
        this.radius = radius;
    }

    /**
     * Actualiza la posición del centro del círculo. Se llama cuando la bola se mueva.
     *
     * @param position Nueva posición del centro del círculo
     */
    @Override
    public void updatePosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Verifica si este collider intersecta con otro.
     * Primero verifica si el collider rectangular de la bola está
     * minimamente cerca del otro collider, y después
     * verifica el collider circular con Ellipse2D.
     *
     * @param other El otro collider a verificar
     * @return true si hay intersección, false en caso contrario
     */
    @Override
    public boolean intersects(Collider other) {
        if (!this.getBoundingBox().intersects(other.getBoundingBox())) {
            return false;
        }

        return this.getEllipse().intersects(other.getBoundingBox());
    }

    /**
     * Obtiene el rectángulo delimitador (bounding box) que contiene al círculo.
     * Este rectángulo se usa para detección rápida de colisiones.
     *
     * @return Rectangle que representa el área que contiene al círculo
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                (int) (position.getX() - radius),
                (int) (position.getY() - radius),
                (int) (radius * 2),
                (int) (radius * 2)
        );
    }

    /**
     * Obtiene la representación precisa del círculo como Ellipse2D.
     *
     * @return Ellipse2D que representa el círculo exacto
     */
    private Ellipse2D getEllipse() {
        return new Ellipse2D.Double(
                position.getX() - radius,
                position.getY() - radius,
                radius * 2,
                radius * 2
        );
    }

    /**
     * Dibuja una representación visual del círculo para propósitos de debug.
     * Dibuja el círculo usando el color de debug especificado.
     *
     * @param g Contexto gráfico donde se dibujará
     */
    @Override
    public void paintDebug(Graphics g) {
        g.setColor(COLLIDER_DEBUG_COLOR);
        g.drawOval(
                (int) (position.getX() - radius),
                (int) (position.getY() - radius),
                (int) (radius * 2),
                (int) (radius * 2)
        );
    }

    /**
     * Actualiza el radio del collider.
     *
     * @param newRadius Nuevo radio
     */
    public void updateSize(int newRadius) {
        this.radius = newRadius;
    }
}
