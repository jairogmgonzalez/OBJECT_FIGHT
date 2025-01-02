package object_fight.gameobjects;

/**
 * @author jairo
 */

import object_fight.collisions.RectangleCollider;
import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.*;

/**
 * Clase que representa el paddle controlada por el jugador. Implementa la
 * lógica de movimiento, redimensionamiento y renderizado de la paleta.
 *
 */
public class Paddle extends GameObject {

    private boolean resizingEnabled = false;
    private int sizeChangeDirection = -1;

    /**
     * Constructor de la paleta.
     *
     * @param position Posición inicial de la paleta
     * @param sizeReduction Reducción del tamaño respecto al tamaño base
     */
    public Paddle(Vector2D position, int sizeReduction) {
        super(position,
                INITIAL_PADDLE_WIDTH - sizeReduction,
                PADDLE_HEIGHT - sizeReduction);

        // Se establece su collider como RectangleCollider
        this.collider = new RectangleCollider(
                position,
                INITIAL_PADDLE_WIDTH - sizeReduction,
                PADDLE_HEIGHT - sizeReduction
        );
    }

    /**
     * Actualiza la posición, tamaño y collider de la paleta.
     */
    @Override
    public void update() {
        position.add(velocity);
        checkBounds();
        collider.updatePosition(position);
        updateSize();
    }

    /**
     * Renderiza la paleta con efectos visuales.
     *
     * @param g Contexto gráfico para dibujar
     */
    @Override
    public void render(Graphics g) {
        int x = (int) position.getX();
        int y = (int) position.getY();

        g.setColor(new Color(220, 20, 60));
        g.fillRect(x, y, width, height);

        g.setColor(new Color(200, 200, 220));
        g.fillRect(x, y + height / 4, width, height / 2);

        g.setColor(new Color(0, 255, 255));
        int segmentWidth = width / 6;
        for (int i = 0; i < 3; i++) {
            int segmentX = x + segmentWidth + (i * 2 * segmentWidth);
            int segmentY = y + height / 3;
            g.fillRect(segmentX, segmentY, segmentWidth / 2, height / 3);
        }

        g.setColor(new Color(255, 100, 100));
        g.drawRect(x, y, width - 1, height - 1);

        g.setColor(new Color(255, 255, 255, 100));
        g.drawLine(x, y, x + width, y);
    }

    /**
     * Maneja colisiones con otros objetos.
     */
    @Override
    public void handleCollision(GameObject other) {
    }

    /**
     * Verifica y corrige la posición de la paleta para que no se salga de la
     * pantalla.
     */
    private void checkBounds() {
        if (position.getX() < 0) {
            position.setX(0);
        } else if (position.getX() + width > SCREEN_WIDTH) {
            position.setX(SCREEN_WIDTH - width);
        }
    }

    /**
     * Activa o desactiva el redimensionamiento automático.
     */
    public void enableResizing(boolean enable) {
        resizingEnabled = enable;
    }

    /**
     * Ajusta el tamaño de la paleta si el redimensionamiento está activado. La
     * paleta cambia de tamaño de forma progresiva en una dirección (ampliación
     * o reducción). Si el tamaño alcanza los límites mínimo o máximo
     * permitidos, se invierte la dirección.
     */
    public void updateSize() {
        // Si el redimensionamiento está desactivado, no hace nada
        if (!resizingEnabled) {
            return;
        }

        // Se calcula el nuevo ancho de la paleta basado en la dirección y velocidad de cambio de tamaño
        int newWidth = getWidth() + (sizeChangeDirection * PADDLE_RESIZE_SPEED);

        // Se verifica si el nuevo tamaño está fuera de los límites permitidos
        if (newWidth <= PADDLE_MIN_WIDTH || newWidth >= PADDLE_ORIGINAL_WIDTH) {
            // Se invierte la dirección de cambio de tamaño
            sizeChangeDirection *= -1;

            // Se ajusta el ancho para que esté dentro de los límites
            newWidth = Math.max(PADDLE_MIN_WIDTH, Math.min(newWidth, PADDLE_ORIGINAL_WIDTH));
        }

        // Se aplica el nuevo tamaño a la paleta
        setWidth(newWidth);
    }

    /**
     * Reduce el tamaño del paddle.
     *
     * @param reduction
     */
    public void reduceSize(int reduction) {
        width = Math.max(INITIAL_PADDLE_WIDTH, width - reduction);
        ((RectangleCollider) collider).updateSize(width, height);
    }

    /**
     * Resetea el tamaño original del paddle.
     */
    public void resetSize() {
        this.width = INITIAL_PADDLE_WIDTH;
    }

    /**
     * Mueve el paddle hacia la izquierda.
     */
    public void moveLeft() {
        velocity.setX((float) (-PADDLE_SPEED * SPEED_MULTIPLIER));
    }

    /**
     * Mueve el paddle hacia la derecha.
     */
    public void moveRight() {
        velocity.setX((float) (PADDLE_SPEED * SPEED_MULTIPLIER));
    }

    /**
     * Detiene el movimiento del paddle.
     */
    public void stop() {
        velocity.setX(0);
    }

    /**
     * Establece el ancho de la paleta y actualiza su colisionador.
     *
     * @param width ancho del paddle
     */
    @Override
    public void setWidth(int width) {
        this.width = width;
        updateCollider();
    }

    /**
     * Actualiza el tamaño del collider.
     */
    private void updateCollider() {
        if (collider instanceof RectangleCollider) {
            ((RectangleCollider) collider).updateSize(width, height);
        }
    }

}
