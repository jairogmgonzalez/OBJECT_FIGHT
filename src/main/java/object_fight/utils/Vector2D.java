package object_fight.utils;

/**
 *
 * @author jairo
 */
/**
 * Clase que representa un vector en 2 dimensiones. Se utiliza para manejar
 * posiciones, velocidades y direcciones en el juego.
 */
public class Vector2D {

    // Componentes del vector
    private float x;
    private float y;

    /**
     * Constructor por defecto. Crea un vector (0,0)
     */
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor por parámetros (coordenadas)
     *
     * @param x Componente x del vector
     * @param y Componente y del vector
     */
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor por parámetros (copia)
     *
     * @param copy Vector a copiar
     */
    public Vector2D(Vector2D copy) {
        this.x = copy.x;
        this.y = copy.y;
    }

    /**
     * Suma otro vector a este.
     *
     * @param other Vector a sumar
     * @return Este vector tras la suma
     */
    public Vector2D add(Vector2D other) {
        this.x += other.x;
        this.y += other.y;

        return this;
    }

    /**
     * Resta otro vector a este.
     *
     * @param other Vector a restar
     * @return Este vector tras la resta
     */
    public Vector2D subtract(Vector2D other) {
        this.x -= other.x;
        this.y -= other.y;

        return this;
    }

    /**
     * Multiplica el vector por un escalar.
     *
     * @param scalar Valor por el que multiplicar
     * @return Este vector tras la multiplicación
     */
    public Vector2D multiply(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    /**
     * Calcula la magnitud (longitud) del vector.
     *
     * @return La magnitud del vector
     */
    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * Normaliza el vector (lo convierte en un vector unitario).
     *
     * @return Este vector normalizado
     */
    public Vector2D normalize() {
        float mag = magnitude();
        if (mag != 0) {
            this.x /= mag;
            this.y /= mag;
        }
        return this;
    }

    // Getters y setters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Devuelve una representación en String del vector.
     */
    @Override
    public String toString() {
        return "Vector2D[" + x + ", " + y + "]";
    }
}
