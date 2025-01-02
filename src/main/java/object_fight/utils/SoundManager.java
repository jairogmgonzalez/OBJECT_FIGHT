package object_fight.utils;

/**
 *
 * @author jairo
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que se encarga de gestionar los efectos de sonido y la música de fondo
 * en el juego. Implementa el patrón Singleton para asegurar que solo exista una
 * única instancia de SoundManager.
 *
 */
public class SoundManager {

    // Instancia única de SoundManager
    private static SoundManager instance;

    // Mapa para almacenar los efectos de sonido con sus nombres asociados
    private Map<String, Clip> soundEffects = new HashMap<>();

    // Clip para la música del menú, que se reproduce en bucle
    private Clip menuMusic;

    /**
     * Constructor privado para evitar instanciar.
     */
    private SoundManager() {
    }

    /**
     * Método estático para obtener la única instancia de SoundManager
     *
     * @return la única instancia de SoundManager
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // Método para cargar un efecto de sonido en el mapa
    // name: el nombre con el que se identificará el sonido
    // filePath: ruta del archivo de sonido dentro de la carpeta "Sounds" (sin extensión)
    /**
     * Método para almacenar un efecto de sonido en el mapa.
     *
     * @param name nombre que se le va a dar al sonido
     * @param filePath ruta donde se encuentra el archivo del sonido
     */
    public void loadSound(String name, String filePath) {
        try {
            URL soundURL = getClass().getResource("/sounds/" + filePath + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundEffects.put(name, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para reproducir un efecto de sonido del mapa.
     *
     * @param name nombre del sonido a reproducir
     */
    public void playSound(String name) {
        // Se obtiene el Clip del mapa de efectos de sonido
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            // Si el Clip ya se está reproduciendo, se detiene antes de reiniciarlo
            if (clip.isRunning()) {
                clip.stop();
            }

            // Se reinicia el Clip desde el principio y comienza su reproducción
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Método para reproducir la música del menú en bucle continuo
    // filePath: ruta del archivo de sonido dentro de la carpeta "Sounds" (sin extensión)
    /**
     * Método para reproducir la música de fondo del menú en bucle continuo.
     *
     * @param filePath ruta donde se encuentra el archivo de la música de fondo
     */
    public void playMenuMusic(String filePath) {
        try {
            URL musicURL = getClass().getResource("/sounds/" + filePath + ".wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicURL);

            if (menuMusic != null && menuMusic.isRunning()) {
                menuMusic.stop();
            }

            menuMusic = AudioSystem.getClip();
            menuMusic.open(audioInputStream);
            menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
            menuMusic.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para detener la música del menú si está en reproducción.
     */
    public void stopMenuMusic() {
        if (menuMusic != null && menuMusic.isRunning()) {
            menuMusic.stop();
        }
    }

    /**
     * Método para cerrar todos los recursos de audio y liberar la memoria
     * ocupada por los Clips.
     */
    public void close() {
        // Se cierra todos los Clips de efectos de sonido
        for (Clip clip : soundEffects.values()) {
            clip.close();
        }

        // Se cierra el Clip de la música del menú si existe
        if (menuMusic != null) {
            menuMusic.close();
        }
    }
}

