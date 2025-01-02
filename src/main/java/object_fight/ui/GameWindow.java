package object_fight.ui;

import object_fight.game.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

import static object_fight.utils.Constants.GAME_TITLE;

/**
 * Ventana principal del juego que gestiona la navegación entre diferentes pantallas.
 * Implementa un sistema de paneles intercambiables usando CardLayout y maneja
 * la transición entre los diferentes estados del juego (menú, juego, victoria, etc.).
 *
 * @author jairo
 */
public class GameWindow extends JFrame {

    // Constantes para identificar los paneles
    private static final String PANEL_MENU = "menu";
    private static final String PANEL_GAME = "game";
    private static final String PANEL_VICTORY = "victory";
    private static final String PANEL_GAMEOVER = "gameover";

    // Componentes de la interfaz
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // Paneles del juego
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private GameOverPanel gameOverPanel;
    private VictoryPanel victoryPanel;

    // Referencia al juego
    private Game game;

    /**
     * Constructor de la ventana principal.
     * Inicializa y configura todos los componentes de la ventana.
     *
     * @param game Referencia al juego principal
     */
    public GameWindow(Game game) {
        this.game = game;
        setupWindow();
        initializePanels(game);
    }

    /**
     * Configura las propiedades básicas de la ventana.
     * Establece el título, comportamiento de cierre y layout inicial.
     */
    private void setupWindow() {
        setTitle(GAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Configuración del layout
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        add(mainContainer);
    }

    /**
     * Inicializa y configura todos los paneles del juego.
     * Crea las instancias de cada panel y las añade al contenedor principal.
     *
     * @param game Referencia al juego para los paneles que la necesiten
     */
    private void initializePanels(Game game) {
        // Creación de paneles
        gamePanel = new GamePanel(game);
        menuPanel = new MenuPanel();
        gameOverPanel = new GameOverPanel();
        victoryPanel = new VictoryPanel();

        // Añadir paneles al contenedor principal
        mainContainer.add(menuPanel, PANEL_MENU);
        mainContainer.add(gamePanel, PANEL_GAME);
        mainContainer.add(gameOverPanel, PANEL_GAMEOVER);
        mainContainer.add(victoryPanel, PANEL_VICTORY);

        // Configuración final de la ventana
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Muestra un panel específico y ajusta la ventana según corresponda.
     * Gestiona la transición entre paneles y las propiedades de la ventana
     * dependiendo del panel a mostrar.
     *
     * @param panelName Identificador del panel a mostrar
     * @throws IllegalArgumentException si el identificador del panel no es válido
     */
    public void showPanel(String panelName) {
        if (!Arrays.asList(PANEL_MENU, PANEL_GAME, PANEL_VICTORY, PANEL_GAMEOVER)
                .contains(panelName)) {
            throw new IllegalArgumentException("Panel inválido: " + panelName);
        }

        boolean isGamePanel = panelName.equals(PANEL_GAME);
        configureWindowForPanel(isGamePanel);
        cardLayout.show(mainContainer, panelName);

        if (isGamePanel) {
            gamePanel.requestGameFocus();
        }
    }

    /**
     * Configura la ventana según el tipo de panel a mostrar.
     * Ajusta el tamaño y decoración de la ventana.
     *
     * @param isGamePanel true si se va a mostrar el panel de juego
     */
    private void configureWindowForPanel(boolean isGamePanel) {
        if (isGamePanel != isUndecorated()) {
            setVisible(false);
            dispose();
            setUndecorated(isGamePanel);

            if (isGamePanel) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(JFrame.NORMAL);
                setSize(800, 600);
                setLocationRelativeTo(null);
                setResizable(false);
            }

            setVisible(true);
        }
    }

    /**
     * Actualiza el estado del juego en los paneles correspondientes.
     *
     * @param game Referencia actualizada al juego
     */
    public void updateGameState(Game game) {
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
     * Actualiza la referencia al juego en la ventana y sus paneles.
     *
     * @param game Nueva referencia al juego
     */
    public void updateGameReference(Game game) {
        this.game = game;
        gamePanel.updateGameReference(game);
    }

    // Getters para acceder a los paneles
    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public GameOverPanel getGameOverPanel() {
        return gameOverPanel;
    }

    public VictoryPanel getVictoryPanel() {
        return victoryPanel;
    }
}
