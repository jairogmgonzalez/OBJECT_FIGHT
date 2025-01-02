package object_fight.utils;

/**
 * @author jairo
 */

import java.awt.*;

/**
 * Clase de constantes que define los valores fundamentales para el juego
 * ObjectFight. Contiene todas las configuraciones estáticas como dimensiones,
 * velocidades, colores y otros parámetros del juego.
 */
public class Constants {

    // --- Configuración de Pantalla ---
    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    public static final int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();
    public static final int FPS = 60;

    // --- Título del Juego ---
    public static final String GAME_TITLE = "ObjectFight";

    // --- Configuración del Juego ---
    public static final int INITIAL_LIVES = 5;
    public static final int GAME_INITIAL_LIVES = 3;
    public static final int GAME_POINTS_PER_BRICK = 10;
    public static final int GAME_PADDLE_REDUCTION_PER_LEVEL = 10;
    public static final int GAME_BALL_REDUCTION_PER_LEVEL = 2;
    public static final float GAME_TURBO_SPEED_MULTIPLIER = 80;
    public static final int MAX_LEVEL = 5;

    // --- Configuración del Paddle ---
    public static final int INITIAL_PADDLE_WIDTH = 250;
    public static final int PADDLE_HEIGHT = 40;
    public static final int PADDLE_SPEED = 40;
    public static final int PADDLE_ORIGINAL_WIDTH = 250;
    public static final int PADDLE_MIN_WIDTH = 100;
    public static final int PADDLE_RESIZE_SPEED = 2;
    public static double SPEED_MULTIPLIER = 1.0;
    public static final Color PADDLE_COLOR = new Color(255, 20, 147);

    // --- Configuración de la bola (Ball) ---
    public static final int BALL_RADIUS = 40;
    public static final int INITIAL_BALL_SPEED = 18;
    public static final float CONSTANT_BALL_SPEED = 10.0f;
    public static final float MAX_BOUNCE_ANGLE = 75.0f;
    public static final float MIN_VERTICAL_SPEED = CONSTANT_BALL_SPEED * 0.3f;
    public static final int BALL_INITIAL_Y_OFFSET = 100;
    public static final Color BALL_COLOR = new Color(255, 165, 0);
    public static final float BASE_BALL_SPEED = 15.0f;  // Aumentado de la velocidad base
    public static final float SPEED_INCREMENT = 0.2f;   // La bola se acelera con cada golpe
    public static final float MAX_BALL_SPEED = 25.0f;   // Velocidad máxima permitida

    // --- Configuración de los ladrillos (Brick) ---
    public static final int BRICK_WIDTH = 100;
    public static final int BRICK_HEIGHT = 60;
    public static final int INITIAL_BRICK_ROWS = 5;
    public static final int INITIAL_BRICK_COLUMNS = 10;
    public static final int BRICK_SPACING = 40;
    public static final int BRICK_TOP_MARGIN = 80;
    public static final int POINTS_PER_BRICK = 100;
    public static final Color BRICK_COLOR = Color.WHITE;
    public static final Color BRICK_BASE_COLOR = new Color(100, 100, 150);
    public static final Color BRICK_BORDER_COLOR = new Color(150, 150, 200);

    // --- Configuración de los ladrillos irrompibles (Unbreakable Bricks) ---
    public static final Color UNBREAKABLE_BRICK_COLOR = Color.GRAY;
    public static final Color UNBREAKABLE_BRICK_BASE_COLOR = new Color(50, 50, 70);
    public static final Color UNBREAKABLE_BRICK_HIGHLIGHT_COLOR = new Color(90, 90, 110);
    public static final Color UNBREAKABLE_BRICK_TEXTURE_COLOR = new Color(120, 120, 140, 100);
    public static final int UNBREAKABLE_BRICK_BORDER_WIDTH = 3;
    public static final int UNBREAKABLE_BRICK_LINE_SPACING = 10;

    // --- Configuración de los ladrillos resistentes (Resistant Bricks) ---
    public static final int RESISTANT_BRICK_INITIAL_HITS = 3;
    public static final int RESISTANT_BRICK_LINE_SPACING = 5;
    public static final Color RESISTANT_BRICK_COLOR_PHASE1 = new Color(135, 206, 250, 150);
    public static final Color RESISTANT_BRICK_COLOR_PHASE2 = new Color(100, 180, 230, 150);
    public static final Color RESISTANT_BRICK_COLOR_PHASE3 = new Color(70, 150, 200, 150);
    public static final Color RESISTANT_BRICK_GRADIENT_COLOR = new Color(255, 255, 255, 80);
    public static final Color RESISTANT_BRICK_LINES_COLOR = new Color(255, 255, 255, 60);
    public static final Color RESISTANT_BRICK_CRACKS_COLOR = new Color(255, 255, 255, 120);

    // --- Configuración de los ladrillos móviles (Movable Bricks) ---
    public static final int MOVABLE_BRICK_INITIAL_DIRECTION = 1;
    public static final int MOVABLE_BRICK_DEFAULT_SPEED = 1;

    // --- Configuración de Colores de Debug ---
    public static final Color DEBUG_LINE_COLOR = Color.RED;
    public static final int DEBUG_VELOCITY_LINE_MULTIPLIER = 10;
    public static final Color COLLIDER_DEBUG_COLOR = Color.RED;

    // --- Constantes de Patrones de Niveles ---
    public static final int PATTERN_BRICK_SPACING = 10;
    public static final int PATTERN_MOVABLE_BRICK_SPACING = 502;
    public static final int PATTERN_MOVEMENT_RANGE = 200;
    public static final int PATTERN_MOVEMENT_SPEED = 8;

    // --- Constantes de UI ---
    public static final int UI_SCORE_X = 20;
    public static final int UI_SCORE_Y = 30;
    public static final int UI_LIVES_OFFSET = 210;
    public static final Font UI_FALLBACK_FONT = new Font("Monospaced", Font.BOLD, 24);

    /**
     * Constructor privado para evitar la instanciación de esta clase de
     * utilidad.
     *
     * @throws AssertionError siempre, ya que esta clase no debe ser instanciada
     */
    private Constants() {
        throw new AssertionError("Esta clase no debe ser instanciada");
    }
}

