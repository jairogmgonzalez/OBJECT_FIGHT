package object_fight.game;

/**
 * @author jairo
 */

import object_fight.ui.*;
import object_fight.utils.SoundManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

/**
 * Clase que gestiona el flujo general del juego y la interacción entre la
 * lógica del juego y la interfaz gráfica. Implementa `GameListener` para
 * responder a eventos importantes como fin del juego, victoria, cambio de
 * nivel, cambio de puntuación y pérdida de vidas.
 *
 * Controla los estados del juego y las transiciones entre diferentes pantallas.
 */

public class GameManager implements GameListener, Runnable {

    // Atributos principales
    private Game game; // La lógica principal del juego
    private GameWindow gameWindow; // Ventana principal del juego

    // Referencias a los paneles de la interfaz
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private GameOverPanel gameOverPanel;
    private VictoryPanel victoryPanel;

    // Hilo para el ciclo de actualización del juego
    private Thread gameThread; // Hilo que ejecuta el ciclo de juego
    private boolean running = false; // Estado de ejecución del juego
    private boolean isPaused = false; // Estado de pausa del juego

    // Estado del juego
    private final int TARGET_FPS = 60; // FPS
    private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;

    /**
     * Constructor de `GameManager`. Inicializa los componentes y la interfaz
     * gráfica, configura los listeners y los sonidos e inicia la app.
     */
    public GameManager() {
        initializeComponents();
        initializePanels();
        setupListeners();
        setupSounds();
        startApplication();
    }

    /**
     * Inicializa los componentes principales del juego y la ventana.
     */
    private void initializeComponents() {
        game = new Game();
        game.setGameListener(this);
        gameWindow = new GameWindow(game);
    }

    /**
     * Inicializa los paneles de la interfaz gráfica y obtiene referencias a
     * cada uno.
     */
    private void initializePanels() {
        menuPanel = gameWindow.getMenuPanel();
        gamePanel = gameWindow.getGamePanel();
        gameOverPanel = gameWindow.getGameOverPanel();
        victoryPanel = gameWindow.getVictoryPanel();
    }

    /**
     * Configura los listeners para los eventos en los paneles de la interfaz.
     * Controla acciones como iniciar una nueva partida, salir del juego y
     * manejar teclas.
     */
    private void setupListeners() {
        menuPanel.addIniciarPartidaAction(e -> startGame());
        victoryPanel.addIniciarPartidaAction(e -> startNewGame());
        gameOverPanel.addIniciarPartidaAction(e -> startNewGame());

        menuPanel.addSalirAction(e -> exitGame());
        victoryPanel.addSalirAction(e -> exitGame());
        gameOverPanel.addSalirAction(e -> exitGame());

        gamePanel.addKeyPressedListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }

    /**
     * Configura los sonidos del juego cargándolos en `SoundManager`. Carga los
     * sonidos de colisión, destrucción de ladrillo, fin de juego, pérdida de
     * vida y victoria.
     */
    private void setupSounds() {
        SoundManager soundManager = SoundManager.getInstance(); // Obtén la instancia única
        soundManager.loadSound("ballCollision", "BallCollision");
        soundManager.loadSound("breakBrick", "BreakBrick");
        soundManager.loadSound("gameOver", "GameOver");
        soundManager.loadSound("lifeLost", "LiveLoss");
        soundManager.loadSound("victory", "Victory");
    }

    /**
     * Inicia la aplicación mostrando el menú principal y reproduciendo la
     * música de fondo del menú.
     */
    private void startApplication() {
        gameWindow.showPanel("menu");
        gameWindow.setVisible(true);
        SoundManager.getInstance().playMenuMusic("MenuMusic");
    }

    /**
     * Inicia una nueva partida y el hilo de actualización del juego, deteniendo
     * la música del menú.
     */
    public void startGame() {
        isPaused = false;
        game.start();
        running = true;

        // Se inicia el hilo del juego si no está en ejecución
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }

        gameWindow.showPanel("game");
        gamePanel.requestGameFocus();
        SoundManager.getInstance().stopMenuMusic();
    }

    /**
     * Inicia una nueva partida desde el menú, reinicializando el estado de
     * juego y los paneles de interfaz.
     */
    private void startNewGame() {
        game = new Game();
        game.setGameListener(this);

        gameWindow.updateGameReference(game);
        gamePanel.updateGameReference(game);

        isPaused = false;
        game.start();
        running = true;

        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }

        gameWindow.showPanel("game");
        gamePanel.requestGameFocus();
    }

    /**
     * Sale del juego, cierra el SoundManagrr y deteniendo la aplicación.
     */
    public void exitGame() {
        SoundManager.getInstance().close();
        System.exit(0);
    }

    /**
     * Reanuda el juego si está pausado, restaurando el panel de juego.
     */
    public void resumeGame() {
        isPaused = false;
        game.resume();
        gameWindow.showPanel("game");
        gamePanel.requestGameFocus();
    }

    /**
     * Regresa al menú principal desde cualquier otro panel.
     */
    public void returnToMenu() {
        running = false;
        gameWindow.showPanel("menu");
    }

    /**
     * Ejecuta el ciclo de actualización del juego a la velocidad de fotogramas
     * objetivo. Verifica y respeta el estado de pausa, y controla el tiempo de
     * espera para mantener el framerate.
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastTime;
            lastTime = now;

            if (!isPaused) {
                // Se actualiza la lógica del juego y el estado
                game.update();
                updateGameState(game);
                gamePanel.repaint();
            }

            // Se controla los FPS
            long sleepTime = (lastTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Actualiza el estado del juego en el panel de juego, sincronizando los
     * elementos de juego con la interfaz.
     *
     * @param game La instancia actual del juego.
     */
    private void updateGameState(Game game) {
        gamePanel.updateGameObjects(
                game.getBall(),
                game.getPaddle(),
                game.getBricks(),
                game.getLives(),
                game.getScore(),
                game.getCurrentLevel()
        );
    }

    /**
     * Maneja las teclas presionadas, permitiendo pausar/reanudar, controlar la
     * paleta y poner la bola en juego.
     *
     * @param e El evento de teclado.
     */
    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (isPaused) {
                resumeGame();
                gamePanel.setPaused(false);
            } else {
                isPaused = true;
                game.pause();
                gamePanel.setPaused(true);
            }
            return;
        }

        if (isPaused) {
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                game.getPaddle().moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                game.getPaddle().moveRight();
                break;
            case KeyEvent.VK_SPACE:
                if (game.getBall().isStuckToPaddle()) {
                    game.getBall().launchBall();
                }
                break;
        }
    }

    /**
     * Maneja las teclas liberadas, deteniendo el movimiento de la paleta si se
     * soltaron las teclas de dirección.
     *
     * @param e El evento de teclado.
     */
    private void handleKeyRelease(KeyEvent e) {
        if (isPaused) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.getPaddle().stop();
        }
    }

    // --- IMPLEMENTACIÓN DE LOS MÉTODOS DEL GAMELISTENER ---
    /**
     * Se activa cuando el juego termina, mostrando el panel de "Game Over" y
     * reproduciendo el sonido correspondiente.
     */
    @Override
    public void onGameOver() {
        running = false;
        isPaused = true;
        gameOverPanel.updateScore(game.getScore());
        gameWindow.showPanel("gameover");
        SoundManager.getInstance().playSound("gameOver");
    }

    /**
     * Se activa cuando el jugador gana el juego, mostrando el panel de victoria
     * y reproduciendo el sonido correspondiente.
     */
    @Override
    public void onGameWon() {
        running = false;
        isPaused = true;
        victoryPanel.updateScore(game.getScore());
        gameWindow.showPanel("victory");
        SoundManager.getInstance().playSound("victory");
    }

    /**
     * Se activa cuando el jugador completa un nivel, pausando el juego y
     * preparando la transición al siguiente nivel.
     *
     * @param level El nivel completado.
     */
    @Override
    public void onLevelCompleted(int level) {
        isPaused = true;
        game.pause();
        Timer transitionTimer = new Timer(100, e -> {
            ((Timer) e.getSource()).stop();
            isPaused = false;
            game.resume();
            gameWindow.showPanel("game");
            gamePanel.requestGameFocus();
        });
        transitionTimer.setRepeats(false);
        transitionTimer.start();
    }

    /**
     * Se activa cuando el jugador pierde una vida, reproduciendo el sonido de
     * pérdida de vida. Si no quedan vidas, llama a `onGameOver`.
     *
     * @param remainingLives Número de vidas restantes.
     */
    @Override
    public void onLifeLost(int remainingLives) {
        SoundManager.getInstance().playSound("lifeLost");
        if (remainingLives <= 0) {
            onGameOver();
        }
    }
}

