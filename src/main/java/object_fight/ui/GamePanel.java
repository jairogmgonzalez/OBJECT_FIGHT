package object_fight.ui;

/**
 * @author jairo
 */

import object_fight.game.Game;
import object_fight.gameobjects.Ball;
import object_fight.gameobjects.Paddle;
import object_fight.gameobjects.bricks.Brick;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.IOException;

import object_fight.utils.Constants.*;

import static javax.swing.text.StyleConstants.setBackground;
import static object_fight.utils.Constants.*;

/**
 * Panel principal del juego que maneja la representación visual.
 *
 */
public class GamePanel extends JPanel {

    // Referencia al juego
    private Game game;

    // Objetos del juego
    private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;

    // Estado del juego
    private int lives;
    private int score;
    private int currentLevel;
    private boolean isPaused;

    // Recursos gráficos
    private Image backgroundImage;

    /**
     * Constructor del panel de juego. Inicializa el panel y configura sus
     * componentes visuales.
     *
     * @param game Referencia al juego principal
     */
    public GamePanel(Game game) {
        this.game = game;
        initializeGameObjects();
        setupPanel();
        setupCursor();
        loadBackgroundImage();
    }

    /**
     * Inicializa las referencias a los objetos del juego.
     */
    private void initializeGameObjects() {
        this.ball = game.getBall();
        this.paddle = game.getPaddle();
        this.bricks = game.getBricks();
        this.lives = game.getLives();
        this.score = game.getScore();
        this.currentLevel = game.getCurrentLevel();
        this.isPaused = false;
    }

    /**
     * Configura las propiedades básicas del panel.
     */
    private void setupPanel() {
        setBackground(Color.BLACK);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        setFocusable(true);
        setDoubleBuffered(true);
    }

    /**
     * Configura el cursor invisible para el juego.
     */
    private void setupCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image cursorImage = toolkit.createImage("");
        Cursor invisibleCursor = toolkit.createCustomCursor(
                cursorImage,
                new Point(0, 0),
                "InvisibleCursor"
        );
        setCursor(invisibleCursor);
    }

    /**
     * Carga la imagen de fondo del juego.
     */
    private void loadBackgroundImage() {
        backgroundImage = new ImageIcon(
                getClass().getResource("/backgrounds/game/GameBackground.jpg")
        ).getImage();
    }

    /**
     * Añade un KeyListener para manejar los inputs del teclado.
     *
     * @param listener El KeyListener a añadir
     */
    public void addKeyPressedListener(KeyListener listener) {
        addKeyListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        renderGame(g);
        drawGameInfo(g);
        if (isPaused) {
            drawPauseScreen(g);
        }
    }

    /**
     * Dibuja el fondo del juego.
     */
    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * Renderiza todos los elementos activos del juego.
     */
    private void renderGame(Graphics g) {
        if (game == null) {
            return;
        }

        if (paddle != null) {
            paddle.render(g);
        }

        if (ball != null) {
            ball.render(g);
        }

        renderBricks(g);
    }

    /**
     * Renderiza los ladrillos activos del juego.
     */
    private void renderBricks(Graphics g) {
        if (bricks == null) {
            return;
        }

        for (Brick brick : bricks) {
            if (brick != null && brick.isAlive()) {
                brick.render(g);
            }
        }
    }

    /**
     * Dibuja la información del juego (puntuación, vidas, nivel).
     */
    private void drawGameInfo(Graphics g) {
        try {
            Font arcadeFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/fonts/PressStart2P.ttf")
            ).deriveFont(24f);

            g.setFont(arcadeFont);
            g.setColor(Color.WHITE);

            // Puntuación a la izquierda
            g.drawString("Score: " + score, UI_SCORE_X, UI_SCORE_Y);

            // Nivel en el centro
            String levelText = "Level: " + currentLevel;
            FontMetrics metrics = g.getFontMetrics(arcadeFont);
            int levelWidth = metrics.stringWidth(levelText);
            g.drawString(levelText, (getWidth() - levelWidth) / 2, UI_SCORE_Y);

            // Vidas a la derecha
            g.drawString("Lives: " + lives, getWidth() - UI_LIVES_OFFSET, UI_SCORE_Y);

        } catch (FontFormatException | IOException e) {
            handleFontLoadError(g);
        }
    }

    /**
     * Maneja el error de carga de fuente, usando una fuente alternativa.
     */
    private void handleFontLoadError(Graphics g) {
        g.setFont((Font) UI_FALLBACK_FONT);
        g.drawString("SCORE: " + score, UI_SCORE_X, UI_SCORE_Y);
        g.drawString("LIVES: " + lives, getWidth() - UI_LIVES_OFFSET, UI_SCORE_Y);
    }

    /**
     * Dibuja la pantalla de pausa.
     */
    private void drawPauseScreen(Graphics g) {
        try {
            Font arcadeFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream("/fonts/PressStart2P.ttf")
            ).deriveFont(24f);

            g.setFont(arcadeFont);
            g.setColor(Color.WHITE);
            String pauseText = "PAUSE";

            FontMetrics metrics = g.getFontMetrics(arcadeFont);
            int x = (getWidth() - metrics.stringWidth(pauseText)) / 2;
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            g.drawString(pauseText, x, y);
        } catch (FontFormatException | IOException e) {
            handleFontLoadError(g);
        }
    }

    /**
     * Actualiza todos los objetos del juego.
     */
    public void updateGameObjects(Ball ball, Paddle paddle, Brick[] bricks,
                                  int lives, int score, int currentLevel) {
        this.ball = ball;
        this.paddle = paddle;
        this.bricks = bricks;
        this.lives = lives;
        this.score = score;
        this.currentLevel = currentLevel;
    }

    /**
     * Actualiza la referencia al juego y sus objetos.
     */
    public void updateGameReference(Game game) {
        this.game = game;
        updateGameObjects(
                game.getBall(),
                game.getPaddle(),
                game.getBricks(),
                game.getLives(),
                game.getScore(),
                game.getCurrentLevel()
        );
    }

    /**
     * Establece el estado de pausa del juego.
     */
    public void setPaused(boolean paused) {
        this.isPaused = paused;
        repaint();
    }

    /**
     * Solicita el foco para capturar el input del teclado.
     */
    public void requestGameFocus() {
        requestFocus();
    }
}
