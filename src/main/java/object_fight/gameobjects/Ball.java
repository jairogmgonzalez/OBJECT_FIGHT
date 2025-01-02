package object_fight.gameobjects;

/**
 * @author jairo
 */

import object_fight.collisions.CircleCollider;
import object_fight.gameobjects.bricks.Brick;
import object_fight.utils.SoundManager;
import object_fight.utils.Vector2D;
import object_fight.utils.Constants.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static object_fight.utils.Constants.*;

/**
 * Clase que representa la bola del juego. Implementa la lógica de movimiento,
 * colisiones y renderizado de la bola.
 *
 */
public class Ball extends GameObject {

    // Atributo que indica si la bola está pegada al paddle
    private boolean isStuckToPaddle = true;

    /**
     * Constructor de la bola.
     *
     * @param position Posición inicial de la bola
     * @param sizeReduction Reducción del tamaño respecto al radio base
     */
    public Ball(Vector2D position, int sizeReduction) {
        super(position, (BALL_RADIUS * 2) - sizeReduction, (BALL_RADIUS * 2) - sizeReduction);

        // Se establece la velocidad de la bola
        this.velocity = new Vector2D(CONSTANT_BALL_SPEED, -CONSTANT_BALL_SPEED);

        // Se establece su collider como CircleCollider
        this.collider = new CircleCollider(
                new Vector2D(position.getX() + BALL_RADIUS, position.getY() + BALL_RADIUS),
                BALL_RADIUS - sizeReduction
        );
    }

    /**
     * Actualiza la posición de la bola y su collider.
     */
    public void update(Paddle paddle) {
        if (isStuckToPaddle) {
            // Si la bola está pegada, sigue la posición del paddle en el eje X
            position.setX(paddle.getPosition().getX() + paddle.getWidth() / 2 - BALL_RADIUS);
            position.setY(paddle.getPosition().getY() - BALL_RADIUS * 2);
        } else {
            // Movimiento normal de la bola cuando está en juego
            super.update();
        }

        // Se actualiza el collider independientemente de si está pegada o no
        collider.updatePosition(new Vector2D(position.getX() + BALL_RADIUS, position.getY() + BALL_RADIUS));

        // Se verifica las colisiones con los límites de la ventana de juego
        checkBounds();
    }

    /**
     * Renderiza la bola como una luna con cráteres usando gradientes.
     *
     * @param g Contexto gráfico para dibujar
     */
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int x = (int) position.getX();
        int y = (int) position.getY();
        int radius = width / 2;

        // Dibuja el cuerpo base de la luna
        Color moonBaseColor = new Color(220, 220, 220);
        g2d.setColor(moonBaseColor);
        g2d.fillOval(x, y, width, height);

        // Aplica gradiente para dar efecto de profundidad
        RadialGradientPaint gradient = new RadialGradientPaint(
                new Point2D.Double(x + radius, y + radius), radius,
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 255, 255, 180), new Color(200, 200, 200)}
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x, y, width, height);

        // Dibuja los cráteres
        g2d.setColor(new Color(150, 150, 150));

        // Se define el tamaño del cráter
        int craterSize = width / 5;

        // Cráteres grandes
        g2d.fillOval(x + width / 4, y + height / 4, craterSize, craterSize);
        g2d.fillOval(x + width / 2, y + height / 3, craterSize - 5, craterSize - 5);
        g2d.fillOval(x + width / 3, y + height / 2, craterSize - 8, craterSize - 8);

        // Cráteres pequeños
        int smallCraterSize = width / 8;
        g2d.fillOval(x + width / 3, y + height / 4, smallCraterSize, smallCraterSize);
        g2d.fillOval(x + width / 2 + 10, y + height / 3 + 10, smallCraterSize, smallCraterSize);
        g2d.fillOval(x + width / 4 + 15, y + height / 2 + 5, smallCraterSize, smallCraterSize);
        g2d.fillOval(x + width / 2 - 10, y + height / 4 + 15, smallCraterSize, smallCraterSize);

        // Borde de la luna
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, width, height);
    }

    /**
     * Maneja las colisiones con otros objetos del juego
     *
     * @param other Objeto con el que colisiona
     */
    @Override
    public void handleCollision(GameObject other) {
        if (other instanceof Paddle) {
            handlePaddleCollision((Paddle) other);
            SoundManager.getInstance().playSound("ballCollision"); // Se reproduce el sonido de colisión de la bola
        } else if (other instanceof Brick) {
            handleBrickCollision((Brick) other);
        }
    }

    /**
     * Maneja la colisión de la bola con la paleta (paddle). Ajusta la velocidad
     * y dirección de la bola en función de la posición del impacto en la
     * paleta, incrementando la velocidad con cada golpe para hacer el juego más
     * difícil.
     *
     * @param paddle El paddle con el que colisiona la bola.
     */
    private void handlePaddleCollision(Paddle paddle) {
        // Se obtiene el collider del paddle
        Rectangle paddleBounds = paddle.getCollider().getBoundingBox();

        // Se obtiene el centro de la bola
        double ballCenterX = position.getX() + BALL_RADIUS;

        // Se incrementa la velocidad de la bola hasta el límite máximo
        float currentSpeed = Math.min(
                getCurrentSpeed() + SPEED_INCREMENT,
                MAX_BALL_SPEED
        );

        // Se verifica si el golpe es central o lateral en la paleta
        boolean isCentralHit = ballCenterX >= paddleBounds.x
                && ballCenterX <= paddleBounds.x + paddleBounds.width;

        if (isCentralHit) {
            handleCentralHit(paddle, currentSpeed);
        } else {
            handleSideHit(currentSpeed);
        }
    }

    /**
     * Maneja un golpe central en la paleta. Calcula el ángulo de rebote y
     * ajusta la velocidad de la bola, añadiendo una pequeña variación aleatoria
     * para dar un comportamiento más dinámico.
     *
     * @param paddle La paleta con la que colisiona la bola.
     * @param currentSpeed La velocidad actual de la bola, ya incrementada.
     */
    private void handleCentralHit(Paddle paddle, float currentSpeed) {
        // Se obtiene el centro de la bola y del paddle
        double ballCenterX = position.getX() + BALL_RADIUS;
        double paddleCenterX = paddle.getPosition().getX() + paddle.getWidth() / 2;

        // Se calcula el punto de impacto relativo (donde golpeó en la paleta)
        double relativeHit = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);

        // Se añade una ligera variación de velocidad y ángulo para un rebote dinámico
        float speedWithVariation = currentSpeed + (float) (Math.random() * 3 - 1.5); // 1.5 de variación
        double randomAngle = Math.random() * 20 - 10; // 10 grados de variación

        // Se ajusta el ángulo de rebote y establece la velocidad de la bola
        double angle = (relativeHit * 50) + randomAngle; // Rebote ajustado de 50 grados
        velocity.setX((float) (speedWithVariation * Math.sin(Math.toRadians(angle))));
        velocity.setY((float) (-speedWithVariation)); // Siempre irá hacia arriba
    }

    /**
     * Maneja un golpe lateral en la paleta. Invierte la dirección horizontal de
     * la bola y ajustar la velocidad vertical para garantizar que no sea
     * demasiado lenta tras un rebote lateral.
     *
     * @param currentSpeed La velocidad actual de la bola.
     */
    private void handleSideHit(float currentSpeed) {
        // Se invierte la dirección horizontal con una variación del 20%
        velocity.setX((float) (-velocity.getX() * (1 + (Math.random() * 0.4f - 0.2f))));
        velocity.setY(-velocity.getY());

        // Se asegura una velocidad mínima vertical para mantener el dinamismo
        float minSpeed = currentSpeed * 0.6f; // 60% de la velocidad actual
        if (Math.abs(velocity.getY()) < minSpeed) {
            velocity.setY(velocity.getY() < 0 ? -minSpeed : minSpeed);
        }
    }

    /**
     * Maneja la colisión de la bola con un ladrillo (brick). Determina la
     * dirección desde la que la bola golpea el ladrillo y ajusta su velocidad y
     * posición en consecuencia.
     *
     * @param brick El ladrillo con el que colisiona la bola.
     */
    private void handleBrickCollision(Brick brick) {
        // Se obtiene el collider del brick
        Rectangle brickBounds = brick.getCollider().getBoundingBox();

        // Se determina la posición anterior de la bola para estimar la dirección de colisión
        double previousX = position.getX() - velocity.getX();
        double previousY = position.getY() - velocity.getY();

        // Se calcula el centro de la bola (posiciones anterior y actual)
        double prevCenterX = previousX + BALL_RADIUS;
        double prevCenterY = previousY + BALL_RADIUS;

        // Se determina la dirección de impacto y ajusta la velocidad en consecuencia
        if (prevCenterY + BALL_RADIUS <= brickBounds.y) {
            // Impacto desde arriba
            velocity.setY(-Math.abs(velocity.getY())); // Asegura que va hacia arriba
            position.setY(brickBounds.y - BALL_RADIUS * 2);
        } else if (prevCenterY - BALL_RADIUS >= brickBounds.y + brickBounds.height) {
            // Impacto desde abajo
            velocity.setY(Math.abs(velocity.getY())); // Asegura que va hacia abajo
            position.setY(brickBounds.y + brickBounds.height);
        } else if (prevCenterX + BALL_RADIUS <= brickBounds.x) {
            // Impacto desde la izquierda
            velocity.setX(-Math.abs(velocity.getX())); // Asegura que va hacia la izquierda
            position.setX(brickBounds.x - BALL_RADIUS * 2);
        } else if (prevCenterX - BALL_RADIUS >= brickBounds.x + brickBounds.width) {
            // Impacto desde la derecha
            velocity.setX(Math.abs(velocity.getX())); // Asegura que va hacia la derecha
            position.setX(brickBounds.x + brickBounds.width);
        } else {
            // Si la bola está dentro del ladrillo, corrige la posición al borde más cercano
            double distToLeft = Math.abs(position.getX() + BALL_RADIUS - brickBounds.x);
            double distToRight = Math.abs(position.getX() + BALL_RADIUS - (brickBounds.x + brickBounds.width));
            double distToTop = Math.abs(position.getY() + BALL_RADIUS - brickBounds.y);
            double distToBottom = Math.abs(position.getY() + BALL_RADIUS - (brickBounds.y + brickBounds.height));

            // Encuentra la distancia mínima para determinar la corrección
            double minDist = Math.min(Math.min(distToLeft, distToRight), Math.min(distToTop, distToBottom));

            // Se ajusta la posición y dirección de la bola basándose en la corrección más cercana
            if (minDist == distToLeft) {
                velocity.setX(-Math.abs(velocity.getX()));
                position.setX(brickBounds.x - BALL_RADIUS * 2);
            } else if (minDist == distToRight) {
                velocity.setX(Math.abs(velocity.getX()));
                position.setX(brickBounds.x + brickBounds.width);
            } else if (minDist == distToTop) {
                velocity.setY(-Math.abs(velocity.getY()));
                position.setY(brickBounds.y - BALL_RADIUS * 2);
            } else {
                velocity.setY(Math.abs(velocity.getY()));
                position.setY(brickBounds.y + brickBounds.height);
            }
        }
    }

    /**
     * Verifica y maneja las colisiones con los límites de la pantalla.
     */
    private void checkBounds() {
        // Límites laterales
        if (position.getX() <= 0 || position.getX() + (BALL_RADIUS * 2) >= SCREEN_WIDTH) {
            velocity.setX(-velocity.getX());
            position.setX(Math.max(0, Math.min(position.getX(), SCREEN_WIDTH - (BALL_RADIUS * 2))));
            SoundManager.getInstance().playSound("ballCollision"); // Se reproduce el sonido de colisión de la bola
        }

        // Límite superior
        if (position.getY() <= 0) {
            velocity.setY(-velocity.getY());
            position.setY(0);
            SoundManager.getInstance().playSound("ballCollision"); // Se reproduce el sonido de colisión de la bola
        }

        // Límite inferior
        if (position.getY() >= SCREEN_HEIGHT) {
            alive = false; // Se "mata" la pelota
        }
    }

    /**
     * Resetea la posiición de la bola a su posición inicial (pegada al paddle).
     *
     * @param paddle
     */
    public void resetBall(Paddle paddle) {
        // Se posiciona la bola encima del paddle
        position.setX(paddle.getPosition().getX() + paddle.getWidth() / 2 - BALL_RADIUS);
        position.setY(paddle.getPosition().getY() - BALL_RADIUS * 2); // Se ajusta la altura para que esté pegada

        // Se establece como pegada al paddle
        isStuckToPaddle = true;

        // Se establece la velocidad de la bola como 0 para que no se mueva hasta que se dispare
        velocity.setX(0);
        velocity.setY(0);
    }

    /**
     * Lanza la bola desde el paddle, asignándole una velocidad inicial y una
     * dirección aleatoria.
     *
     */
    public void launchBall() {
        if (isStuckToPaddle) {
            // Se cambia el estado para permitir que la bola se mueva
            isStuckToPaddle = false;

            // Se define la velocidad inicial aumentando su velocidad base para iniciar el movimiento
            float initialSpeed = BASE_BALL_SPEED * 1.2f;

            // Se genera un ángulo aleatorio para variar la dirección inicial
            double angle = Math.toRadians(-60 + Math.random() * 120); // Rango de -60° a 60°

            // Se establece la velocidad inicial en función del ángulo y la velocidad definida
            velocity.setX((float) (initialSpeed * Math.cos(angle))); // Componente X de la velocidad
            velocity.setY((float) (-initialSpeed * Math.sin(angle))); // Siempre irá hacia arriba
        }
    }

    /**
     * Resetea la posición de la bola a su posición inicial.
     */
    public void resetBallPosition() {
        setPosition(new Vector2D(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2));
        setAlive(true);
    }

    /**
     * Resetea la velocidad de la bola a sus valores iniciales.
     */
    public void resetBallVelocity() {
        setVelocity(new Vector2D(INITIAL_BALL_SPEED, -INITIAL_BALL_SPEED));
    }

    /**
     * Calcula y devuelve la velocidad actual de la bola.
     *
     * @return La magnitud de la velocidad actual de la bola.
     */
    private float getCurrentSpeed() {
        return velocity.magnitude();
    }

    // Getters y setters
    public boolean isStuckToPaddle() {
        return isStuckToPaddle;
    }

    public void setStuckToPaddle(boolean isStuck) {
        isStuckToPaddle = isStuck;
    }

}
