package object_fight.game;

/**
 *
 * @author jairo
 */
/**
 * Interfaz que define un conjunto de métodos para escuchar y reaccionar a
 * eventos clave del juego.
 *
 */
public interface GameListener {

    /**
     * Llamado cuando el juego ha terminado.
     * Este evento se dispara cuando el jugador pierde todas sus vidas o cumple una condición de derrota.
     */
    void onGameOver();

    /**
     * Llamado cuando el jugador gana el juego.
     * Este evento se dispara cuando el jugador cumple todas las condiciones de victoria,
     * como completar todos los niveles.
     */
    void onGameWon();

    /**
     * Llamado cuando el jugador completa un nivel.
     * Este evento indica el avance del jugador al siguiente nivel o fase.
     *
     * @param level Nivel que el jugador ha completado.
     */
    void onLevelCompleted(int level);

    /**
     * Llamado cuando el jugador pierde una vida.
     * Este evento se dispara cada vez que el jugador pierde una vida, lo que
     * puede suceder si la bola cae fuera de la pantalla o se cumple otra condición de pérdida.
     *
     * @param remainingLives El número de vidas restantes para el jugador.
     */
    void onLifeLost(int remainingLives);
}

