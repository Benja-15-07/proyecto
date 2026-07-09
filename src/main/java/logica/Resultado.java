package logica;

/**
 * Representa el resultado de un enfrentamiento.
 */
public class Resultado {
    private int puntuacion1;
    private int puntuacion2;

    /**
     * Crea un resultado con las puntuaciones de ambos participantes.
     *
     * @param puntuacion1 puntuación del primer participante
     * @param puntuacion2 puntuación del segundo participante
     */
    public Resultado(int puntuacion1, int puntuacion2) {
        this.puntuacion1 = puntuacion1;
        this.puntuacion2 = puntuacion2;
    }

    /**
     * @return puntuación del primer participante
     */
    public int getPuntuacion1() {
        return puntuacion1;
    }

    /**
     * @return puntuación del segundo participante
     */
    public int getPuntuacion2() {
        return puntuacion2;
    }
}
