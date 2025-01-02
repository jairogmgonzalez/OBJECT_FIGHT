package object_fight.game;

/**
 * @author jairo
 */

import object_fight.gameobjects.Ball;
import object_fight.gameobjects.Paddle;
import object_fight.gameobjects.bricks.Brick;
import object_fight.gameobjects.bricks.MovableBrick;
import object_fight.gameobjects.bricks.ResistantBrick;
import object_fight.gameobjects.bricks.UnbreakableBrick;
import object_fight.utils.SoundManager;
import object_fight.utils.Vector2D;

import static object_fight.utils.Constants.*;

/**
 * Clase principal que gestiona toda la lógica del juego. Controla la
 * actualización de objetos, colisiones, niveles y estado general del juego.
 * Implementa un sistema de puntuación, vidas y progresión de niveles.
 *
 * Incluye listeners para actualizar el estado del juego y notificar eventos.
 */
public class Game {

    // Objetos principales del juego
    private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;

    // Progreso del juego
    private int score;
    private int lives;
    private int currentLevel;

    // Estado del juego
    private boolean isRunning;
    private boolean gameOver;
    private boolean gameWon;

    // Listener para eventos del juego
    private GameListener listener;

    /**
     * Constructor del juego. Inicializa el juego.
     */
    public Game() {
        initGame();
    }

    /**
     * Inicializa el estado del juego con los valores por defecto.
     */
    private void initGame() {
        score = 0;
        lives = GAME_INITIAL_LIVES;
        currentLevel = 1;
        isRunning = false;
        gameOver = false;
        gameWon = false;

        createGameObjects();
    }

    /**
     * Crea o reinicia los objetos principales del juego: la bola, el paddle y
     * los ladrillos.
     */
    private void createGameObjects() {
        if (paddle == null) {
            paddle = new Paddle(new Vector2D(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 50), 0);
        } else {
            paddle.setPosition(new Vector2D(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 50));
        }

        if (ball == null) {
            ball = new Ball(new Vector2D(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2), 0);
        } else {
            ball.resetBallPosition();
        }

        createBricks();
    }

    /**
     * Crea la disposición de ladrillos según el nivel actual. Cada nivel tiene
     * un patrón y dificultad diferente.
     */
    private void createBricks() {
        switch (currentLevel) {
            case 1 ->
                    createLevel1Pattern();
            case 2 ->
                    createLevel2Pattern();
            case 3 ->
                    createLevel3Pattern();
            case 4 ->
                    createLevel4Pattern();
            case 5 ->
                    createLevel5Pattern();
        }
    }

    /**
     * Actualiza el estado de todos los elementos del juego. Solo actualiza si
     * el juego está en ejecución y no ha terminado.
     */
    public void update() {
        if (!isRunning || gameOver) {
            return;
        }

        paddle.update();
        ball.update(paddle);

        if (bricks != null) {
            for (Brick brick : bricks) {
                if (brick != null) {
                    brick.update();
                }
            }
        }

        // Se comprueba las colisiones
        checkCollisions();

        // Se comprueba el estado de la partida
        checkGameState();
    }

    // --- MÉTODOS DE COMPROBACIÓN ---
    /**
     * Verifica y maneja todas las colisiones entre objetos del juego.
     */
    private void checkCollisions() {
        // Colisión bola - paddle
        if (ball.collidesWith(paddle)) {
            ball.handleCollision(paddle);
        }

        // Colisión bola - brick
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (brick != null && brick.isAlive()) {
                    if (ball.collidesWith(brick)) {
                        ball.handleCollision(brick);
                        brick.handleCollision(ball);

                        // Se incrementa el score con todos los tipos de bricks excepto el brick irrompible
                        if (!(brick instanceof UnbreakableBrick)) {
                            SoundManager.getInstance().playSound("breakBrick"); // Se reproduce el sonido de destruir un brick
                            score += GAME_POINTS_PER_BRICK;
                        }
                    }
                }
            }
        }
    }

    /**
     * Verifica el estado actual del juego y maneja condiciones de victoria o
     * derrota. Actualiza las vidas del jugador si pierde la bola, y verifica si
     * se ha completado el nivel. Maneja la transición al siguiente nivel o el
     * fin del juego en caso de victoria o derrota.
     */
    private void checkGameState() {
        // Se verifica si la bola ya no está activa
        if (!ball.isAlive()) {
            lives--; // Se reduce las vidas restantes
            ball.setStuckToPaddle(true); // Se vuelve a colocar la bola sobre el paddle

            // Se notifica la pérdida de una vida al GameListener
            if (listener != null) {
                listener.onLifeLost(lives);
            }

            // Se verifica si el jugador se ha quedado sin vidas
            if (lives <= 0) {
                gameOver(); // Se termina la partida
            } else {
                // Se reinicia la posición y velocidad de la bola para continuar el juego
                ball.resetBallPosition();
                ball.resetBallVelocity();
                ball.setAlive(true);
            }
            return; // Se sale del método ya que no es necesario seguir verificando
        }

        // Se verifica si el juego está en curso y el nivel está completo
        if (isRunning && isLevelCompleted()) {
            if (currentLevel == 5) {
                gameWon(); // Se marca el juego como ganado si es el último nivel
            } else {
                // Se avanza al siguiente nivel
                currentLevel++;
                startNextLevel();
                ball.setStuckToPaddle(true); // Se vuelve a colocar la bola sobre el paddle

                // Se notifica la finalización del nivel actual al GameListener
                if (listener != null) {
                    listener.onLevelCompleted(currentLevel);
                }
            }
        }
    }

    // --- MÉTODOS DEL ESTADO DE LA PARTIDA ---
    /**
     * Maneja el estado de fin de juego por derrota.
     */
    private void gameOver() {
        isRunning = false;
        gameOver = true;
        if (listener != null) {
            listener.onGameOver();
        }
    }

    /**
     * Maneja el estado de victoria del juego.
     */
    private void gameWon() {
        isRunning = false;
        gameWon = true;
        if (listener != null) {
            listener.onGameWon();
        }
    }

    /**
     * Prepara y configura el siguiente nivel del juego.
     */
    private void startNextLevel() {
        paddle.reduceSize((currentLevel - 1) * GAME_PADDLE_REDUCTION_PER_LEVEL);
        ball.resetBallPosition();
        ball.resetBallVelocity();
        ball.setAlive(true);

        createBricks();
        isRunning = true;
        paddle.enableResizing(currentLevel == 4);
    }

    /**
     * Verifica si el nivel actual ha sido completado.
     *
     * @return true si el nivel está completado, false en caso contrario
     */
    private boolean isLevelCompleted() {
        if (!isRunning || bricks == null) {
            return false;
        }

        int bricksRompiblesVivos = 0;
        for (Brick brick : bricks) {
            if (brick != null && brick.isAlive() && !(brick instanceof UnbreakableBrick)) {
                bricksRompiblesVivos++;
            }
        }

        return bricksRompiblesVivos == 0;
    }

    // --- MÉTODOS DE CONTROL DE EJECUCIÓN ---
    /**
     * Inicia el juego.
     */
    public void start() {
        isRunning = true;
        gameOver = false;
        gameWon = false;
    }

    /**
     * Pausa el juego.
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * Reanuda el juego.
     */
    public void resume() {
        isRunning = true;
    }

    // --- PATRONES DE BRICKS PARA CADA NIVEL ---
    /**
     * Patrón para el nivel 1.
     */
    private void createLevel1Pattern() {
        int ROWS = 5;
        int COLS = 6;
        bricks = new Brick[ROWS * COLS];

        int startX = (SCREEN_WIDTH - (COLS * BRICK_WIDTH)) / 2;
        int index = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (index >= bricks.length) {
                    break;
                }
                int x = startX + col * BRICK_WIDTH;
                int y = BRICK_TOP_MARGIN + row * BRICK_HEIGHT;
                bricks[index++] = new Brick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT);
            }
        }
    }

    /**
     * Patrón para el nivel 2.
     */
    private void createLevel2Pattern() {
        int ROWS = 6;
        int COLS = 8;
        int BRICK_SPACING = 5;
        bricks = new Brick[ROWS * COLS];
        int startX = (SCREEN_WIDTH - (COLS * (BRICK_WIDTH + BRICK_SPACING))) / 2;
        int index = 0;

        int[][] pattern = {
                {1, 1, 1, 0, 0, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 2, 1, 1, 2, 1, 1},
                {0, 1, 1, 2, 2, 1, 1, 0},
                {0, 0, 1, 1, 1, 1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0}
        };

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (pattern[row][col] > 0) {
                    int x = startX + col * (BRICK_WIDTH + BRICK_SPACING);
                    int y = BRICK_TOP_MARGIN + row * (BRICK_HEIGHT + BRICK_SPACING);

                    if (pattern[row][col] == 2) {
                        bricks[index++] = new UnbreakableBrick(new Vector2D(x, y),
                                BRICK_WIDTH, BRICK_HEIGHT);
                    } else {
                        bricks[index++] = new Brick(new Vector2D(x, y),
                                BRICK_WIDTH, BRICK_HEIGHT);
                    }
                }
            }
        }
    }

    /**
     * Patrón para el nivel 3.
     */
    private void createLevel3Pattern() {
        int brickWidth = 100;
        int brickHeight = 60;
        int verticalSpacing = 30;
        int horizontalSpacing = 10;

        int centerX = SCREEN_WIDTH / 2;
        int startY = 100;

        bricks = new Brick[19];
        int index = 0;

        int rowWidth = 6 * (brickWidth + horizontalSpacing) - horizontalSpacing;
        int startX = centerX - rowWidth / 2;
        for (int i = 0; i < 6; i++) {
            int x = startX + i * (brickWidth + horizontalSpacing);
            Vector2D position = new Vector2D(x, startY);

            if (i == 0 || i == 5) {
                bricks[index++] = new ResistantBrick(position, brickWidth, brickHeight);
            } else {
                bricks[index++] = new Brick(position, brickWidth, brickHeight);
            }
        }

        startY += brickHeight + verticalSpacing;
        rowWidth = 5 * (brickWidth + horizontalSpacing) - horizontalSpacing;
        startX = centerX - rowWidth / 2;
        for (int i = 0; i < 5; i++) {
            int x = startX + i * (brickWidth + horizontalSpacing);
            Vector2D position = new Vector2D(x, startY);

            if (i == 0 || i == 4) {
                bricks[index++] = new ResistantBrick(position, brickWidth, brickHeight);
            } else {
                bricks[index++] = new Brick(position, brickWidth, brickHeight);
            }
        }

        startY += brickHeight + verticalSpacing;
        rowWidth = 4 * (brickWidth + horizontalSpacing) - horizontalSpacing;
        startX = centerX - rowWidth / 2;
        for (int i = 0; i < 4; i++) {
            int x = startX + i * (brickWidth + horizontalSpacing);
            Vector2D position = new Vector2D(x, startY);

            if (i == 0 || i == 3) {
                bricks[index++] = new ResistantBrick(position, brickWidth, brickHeight);
            } else {
                bricks[index++] = new Brick(position, brickWidth, brickHeight);
            }
        }

        startY += brickHeight + verticalSpacing;
        rowWidth = 3 * (brickWidth + horizontalSpacing) - horizontalSpacing;
        startX = centerX - rowWidth / 2;
        for (int i = 0; i < 3; i++) {
            int x = startX + i * (brickWidth + horizontalSpacing);
            Vector2D position = new Vector2D(x, startY);

            if (i == 0 || i == 2) {
                bricks[index++] = new ResistantBrick(position, brickWidth, brickHeight);
            } else {
                bricks[index++] = new Brick(position, brickWidth, brickHeight);
            }
        }

        startY += brickHeight + verticalSpacing;
        Vector2D position = new Vector2D(centerX - brickWidth / 2, startY);
        bricks[index] = new UnbreakableBrick(position, brickWidth, brickHeight);
    }

    /**
     * Patrón para el nivel 4.
     */
    private void createLevel4Pattern() {
        int ROWS = 3;
        int COLS = 3;
        int BRICK_SPACING_HORIZONTAL = 150;
        int BRICK_SPACING_VERTICAL = 50;
        int MOVEMENT_RANGE = 100;
        int MOVEMENT_SPEED = 8;

        bricks = new Brick[ROWS * COLS];
        int startX = (SCREEN_WIDTH - (COLS * (BRICK_WIDTH + BRICK_SPACING_HORIZONTAL))) / 2;
        int startY = BRICK_TOP_MARGIN;
        int index = 0;

        for (int row = 0; row < ROWS; row++) {
            int initialDirection = (row % 2 == 0) ? 1 : -1;

            for (int col = 0; col < COLS; col++) {
                if (index >= bricks.length) {
                    break;
                }

                int x = startX + col * (BRICK_WIDTH + BRICK_SPACING_HORIZONTAL);
                int y = startY + row * (BRICK_HEIGHT + BRICK_SPACING_VERTICAL);

                bricks[index++] = new MovableBrick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT, MOVEMENT_RANGE, initialDirection);
                ((MovableBrick) bricks[index - 1]).setMoveSpeed(MOVEMENT_SPEED);
            }
        }
    }

    /**
     * Patrón para el nivel 5.
     */
    private void createLevel5Pattern() {
        int ROWS = 5;
        int COLS = 7;
        int BRICK_SPACING = 10;
        bricks = new Brick[ROWS * COLS];

        int startX = (SCREEN_WIDTH - (COLS * (BRICK_WIDTH + BRICK_SPACING))) / 2;
        int index = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (index >= bricks.length) {
                    break;
                }

                int x = startX + col * (BRICK_WIDTH + BRICK_SPACING);
                int y = BRICK_TOP_MARGIN + row * (BRICK_HEIGHT + BRICK_SPACING);

                if ((row == 0 && (col == 0 || col == COLS - 1))
                        || (row == 1 && (col == 1 || col == COLS - 2))) {
                    bricks[index++] = new UnbreakableBrick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT);
                } else if (row == 2) {
                    bricks[index++] = new MovableBrick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT, 60, 1);
                } else if (row == 3) {
                    bricks[index++] = new MovableBrick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT, 60, -1);
                } else if ((row == 1 && (col > 1 && col < COLS - 2))
                        || (row == 4 && (col > 0 && col < COLS - 1))) {
                    bricks[index++] = new ResistantBrick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT);
                } else if (row == 0 && (col > 0 && col < COLS - 1)) {
                    bricks[index++] = new Brick(new Vector2D(x, y), BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }
    }

    // Setters y getters
    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Ball getBall() {
        return this.ball;
    }

    public Paddle getPaddle() {
        return this.paddle;
    }

    public Brick[] getBricks() {
        return this.bricks;
    }

    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
}

