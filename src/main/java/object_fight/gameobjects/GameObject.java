package object_fight.gameobjects;

/**
 * @author jairo
 */

import object_fight.collisions.Collider;
import object_fight.utils.Vector2D;

import java.awt.*;

import static object_fight.utils.Constants.DEBUG_LINE_COLOR;
import static object_fight.utils.Constants.DEBUG_VELOCITY_LINE_MULTIPLIER;

/**
 * Clase abstracta que representa un objeto base del juego. Proporciona la
 * funcionalidad común para todos los objetos del juego como posición,
 * velocidad, colisiones y renderizado.
 *
 */
public abstract class GameObject {

    // Posición del objeto en el espacio 2D
    protected Vector2D position;
    // Velocidad y dirección del objeto
    protected Vector2D velocity;
    // Dimensiones del objeto
    protected int width;
    protected int height;
    // Estado de vida del objeto
    protected boolean alive;
    // Componente de colisión del objeto
    protected Collider collider;

    /**
     * Constructor que inicializa un objeto del juego.
     *
     * @param position Posición inicial del objeto
     * @param width Ancho del objeto
     * @param height Alto del objeto
     */
    public GameObject(Vector2D position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.velocity = new Vector2D(0, 0);
        this.alive = true;
    }

    /**
     * Actualiza el estado del objeto en cada frame. Actualiza la posición
     * basada en la velocidad y actualiza el colisionador.
     */
    public void update() {
        if (!alive) {
            return;
        }
        position.add(velocity);
        collider.updatePosition(position);
    }

    /**
     * Método abstracto para renderizar el objeto. Cada subclase debe
     * implementar su propia lógica de renderizado.
     *
     * @param g Contexto gráfico para dibujar
     */
    public abstract void render(Graphics g);

    /**
     * Comprueba si este objeto colisiona con otro.
     *
     * @param other Otro objeto del juego
     * @return true si hay colisión, false en caso contrario
     */
    public boolean collidesWith(GameObject other) {
        if (!this.alive || !other.alive) {
            return false;
        }
        return this.collider.intersects(other.getCollider());
    }

    /**
     * Método abstracto para manejar las colisiones. Cada subclase debe
     * implementar su propia lógica de colisiones.
     *
     * @param other Objeto con el que se ha colisionado
     */
    public abstract void handleCollision(GameObject other);

    /**
     * Dibuja información de debug del objeto. Muestra el vector de velocidad y
     * el colisionador.
     *
     * @param g Contexto gráfico para dibujar
     */
    public void drawDebug(Graphics g) {
        if (alive) {
            g.setColor(DEBUG_LINE_COLOR);
            g.drawLine(
                    (int) position.getX(),
                    (int) position.getY(),
                    (int) (position.getX() + velocity.getX() * DEBUG_VELOCITY_LINE_MULTIPLIER),
                    (int) (position.getY() + velocity.getY() * DEBUG_VELOCITY_LINE_MULTIPLIER)
            );
            collider.paintDebug(g);
        }
    }

    // Getters y setters para las propiedades del objeto
    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public float getX() {
        return position.getX();
    }

    public void setX(float x) {
        position.setX(x);
    }

    public float getY() {
        return position.getY();
    }

    public void setY(float y) {
        position.setY(y);
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public float getVelocityX() {
        return velocity.getX();
    }

    public void setVelocityX(float x) {
        velocity.setX(x);
    }

    public float getVelocityY() {
        return velocity.getY();
    }

    public void setVelocityY(float y) {
        velocity.setY(y);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Collider getCollider() {
        return this.collider;
    }

    public void setCollider(Collider collider) {
        this.collider = collider;
    }
}

