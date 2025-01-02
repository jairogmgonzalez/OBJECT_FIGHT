package object_fight.collisions;

/**
 *
 * @author jairo
 */

import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.COLLIDER_DEBUG_COLOR;

/**
 * Clase que rerpresenta un collider rectangular. Maneja las colisiones usando
 * un Rectangle interno para la detección de intersecciones. Proporciona
 * funcionalidad para actualizar tamaño y posición dinámicamente.
 */
public class RectangleCollider implements Collider {

    // Posición del collider
    private Vector2D position;

    // Ancho y altura del collider
    private int width; // Ancho
    private int height; // Altura

    /**
     * Constructor del collider rectangular.
     *
     * @param position Posición inicial del collider
     * @param width Ancho del rectángulo en píxeles
     * @param height Alto del rectángulo en píxeles
     */
    public RectangleCollider(Vector2D position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    /**
     * Actualiza la posición del collider. Se llama cuando el objeto
     * asociado se mueve.
     *
     * @param position Nueva posición del collider
     */
    @Override
    public void updatePosition(Vector2D position) {
        this.position = position;
    }

    /**
     * Verifica si este collider intersecta con otro. Utiliza los bounding
     * boxes de ambos colliders para la detección.
     *
     * @param other El otro collider a verificar
     * @return true si hay intersección, false en caso contrario
     */
    @Override
    public boolean intersects(Collider other) {
        return this.getBoundingBox().intersects(other.getBoundingBox());
    }

    /**
     * Obtiene la representación rectangular (bounding box) del collider.
     * Este rectángulo se usa para detección de colisiones básica.
     *
     * @return Rectangle que representa el área del collider
     */
    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(
                (int) position.getX(),
                (int) position.getY(),
                width,
                height
        );
    }

    /**
     * Dibuja una representación visual del collider para propósitos de
     * debug. Dibuja el rectángulo usando el color de debug especificado.
     *
     * @param g Contexto gráfico donde se dibujará
     */
    @Override
    public void paintDebug(Graphics g) {
        g.setColor(COLLIDER_DEBUG_COLOR);
        g.drawRect(
                (int) position.getX(),
                (int) position.getY(),
                width,
                height
        );
    }

    /**
     * Actualiza las dimensiones del collider.
     *
     * @param newWidth Nuevo ancho
     * @param newHeight Nuevo alto
     */
    public void updateSize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
}
