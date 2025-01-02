package object_fight.collisions;

/**
 *
 * @author jairo
 */

import object_fight.utils.Vector2D;

import java.awt.*;

/**
 * Interfaz que define el comportamiento de un collider. Proporciona métodos
 * para la detección de colisiones y manejo de posiciones.
 */
public interface Collider {

    /**
     * Actualiza la posición del collider basándose en la nueva posición
     * proporcionada.
     *
     * @param position La nueva posición del objeto asociado al collider.
     */
    void updatePosition(Vector2D position);

    /**
     * Verifica si el collider actual intersecta con otro collider.
     *
     * @param other El collider con el que se verifica la intersección.
     * @return true si los colliders intersectan, false en caso contrario.
     */
    boolean intersects(Collider other);

    /**
     * Devuelve la caja delimitadora del collider en forma de un objeto Rectangle.
     *
     * @return La caja delimitadora del colisionador.
     */
    Rectangle getBoundingBox();

    /**
     * Dibuja la representación de depuración del collider.
     *
     * @param g El contexto gráfico en el que se dibujará.
     */
    void paintDebug(Graphics g);
}
