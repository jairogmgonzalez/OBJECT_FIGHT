package object_fight;


import object_fight.game.GameManager;

import javax.swing.*;
/**
 *
 * @author jairo
 */

/**
 * Clase principal que inicia el juego.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameManager();
        });
    }
}