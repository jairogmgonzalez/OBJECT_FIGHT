package object_fight.gameobjects.bricks;

/**
 * @author jairo
 */

import object_fight.collisions.RectangleCollider;
import object_fight.gameobjects.Ball;
import object_fight.gameobjects.GameObject;
import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.BRICK_BASE_COLOR;
import static object_fight.utils.Constants.BRICK_BORDER_COLOR;

/**
 * Clase que representa un ladrillo en el juego. Los ladrillos son objetos
 * estáticos que pueden ser destruidos cuando la bola colisiona con ellos. Cada
 * ladrillo tiene un aspecto metálico con un color base y un borde.
 */
public class Brick extends GameObject {

    /**
     * Constructor del ladrillo.
     *
     * @param position Posición inicial del ladrillo
     * @param width Ancho del ladrillo
     * @param height Alto del ladrillo
     */
    public Brick(Vector2D position, int width, int height) {
        super(position, width, height);

        // Se establece su collider como RectangleCollider
        this.collider = new RectangleCollider(position, width, height);
    }

    /**
     * Actualiza el estado del ladrillo. Si el ladrillo no está vivo (ha sido
     * destruido), no realiza ninguna actualización.
     */
    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
    }

    /**
     * Renderiza el ladrillo con un efecto metálico.
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

        // Dibujar el cuerpo principal del ladrillo
        g2d.setColor(BRICK_BASE_COLOR);
        g2d.fillRect(x, y, width, height);

        // Dibujar el borde metálico
        g2d.setColor(BRICK_BORDER_COLOR);
        g2d.drawRect(x, y, width, height);
    }

    /**
     * Maneja la colisión con otros objetos del juego (la bola). Si el ladrillo
     * es golpeado por una bola, se destruye.
     *
     * @param other Objeto con el que colisiona
     */
    @Override
    public void handleCollision(GameObject other) {
        if (other instanceof Ball && isAlive()) {
            setAlive(false);
        }
    }
}
