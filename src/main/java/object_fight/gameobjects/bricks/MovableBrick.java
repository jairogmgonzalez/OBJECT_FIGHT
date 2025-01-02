package object_fight.gameobjects.bricks;

/**
 * @author jairo
 */

import object_fight.utils.Vector2D;

import static object_fight.utils.Constants.MOVABLE_BRICK_DEFAULT_SPEED;

/**
 * Clase que epresenta un ladrillo que se mueve horizontalmente. Este tipo de ladrillo se
 * desplaza de un lado a otro dentro de un rango específico.
 *
 */
public class MovableBrick extends Brick {

    // Dirección del movimiento: 1 para derecha, -1 para izquierda
    private int moveDirection;

    // Distancia máxima que puede moverse desde su posición inicial
    private int maxRange;

    // Posición inicial en X del ladrillo
    private int initialX;

    // Velocidad actual de movimiento
    private int moveSpeed;

    /**
     * Constructor del ladrillo móvil.
     *
     * @param position Posición inicial del ladrillo
     * @param width Ancho del ladrillo
     * @param height Alto del ladrillo
     * @param maxRange Rango máximo de movimiento desde la posición inicial
     * @param initialDirection Dirección inicial del movimiento (1 para derecha,
     * -1 para izquierda)
     */
    public MovableBrick(Vector2D position, int width, int height, int maxRange, int initialDirection) {
        super(position, width, height);
        this.initialX = (int) position.getX();
        this.maxRange = maxRange;
        this.moveDirection = initialDirection;
        this.moveSpeed = MOVABLE_BRICK_DEFAULT_SPEED;
    }

    /**
     * Actualiza la posición del ladrillo. El ladrillo se mueve horizontalmente
     * dentro de su rango establecido, cambiando de dirección cuando alcanza los
     * límites.
     */
    @Override
    public void update() {
        super.update();

        // Se actualiza posición horizontal
        position.setX(position.getX() + moveDirection * moveSpeed);

        // Se verifica si alcanzó el límite del rango y se cambiar la dirección
        if (Math.abs(position.getX() - initialX) >= maxRange) {
            moveDirection *= -1;
        }

        // Se actualiza la posición del collider
        collider.updatePosition(position);
    }

    /**
     * Establece la velocidad de movimiento del ladrillo.
     *
     * @param speed Nueva velocidad de movimiento
     */
    public void setMoveSpeed(int speed) {
        this.moveSpeed = speed;
    }

}
