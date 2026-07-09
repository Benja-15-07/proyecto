package logica;

/**
 * Almacena las estadísticas de un participante durante un torneo.
 */
public class Estadistica {
    private int victorias;
    private int derrotas;
    private int empates;
    private int puntosFavor;
    private int puntosContra;

    /**
     * Crea una estadística con los valores en 0.
     */
    public Estadistica() {
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
        this.puntosFavor = 0;
        this.puntosContra = 0;

    }

    /**
     * Incrementa el número de victorias.
     */
    public void agregarVictoria(){
        victorias++;
    }

    /**
     * Incrementa el número de derrotas.
     */
    public void agregarDerrota(){
        derrotas++;
    }

    /**
     * Incrementa el número de empates.
     */
    public void agregarEmpate(){
        empates++;
    }

    public void agregarMarcador(int favor, int contra){
        puntosFavor += favor;
        puntosContra += contra;
    }

    /**
     * @return número de victorias
     */
    public int getVictorias() {
        return victorias;
    }

    /**
     * @return número de derrotas
     */
    public int getDerrotas() {
        return derrotas;
    }

    /**
     * @return número de empates
     */
    public int getEmpates() {
        return empates;
    }

    public int getPuntosFavor() {
        return puntosFavor;
    }

    public int getPuntosContra() {
        return puntosContra;
    }

    public int getDiferencia() {
        return puntosFavor - puntosContra;
    }

    /**
     * Devuelve el total de partidas jugadas.
     *
     * @return partidas jugadas
     */
    public int getPartidasJugadas(){
        return victorias + derrotas + empates;
    }

    /**
     * Calcula el porcentaje de victorias del participante.
     *
     * @return porcentaje de victorias
     */
    public double getPorcentajeVictorias(){
        int total = victorias + derrotas + empates;
        if(total == 0) {
            return 0;
        }
        return (double) (victorias * 100) / total;
    }

    /**
     * Calcula la puntuación del participante.
     * Victoria 3 puntos, empate 1 punto, derrota 0 puntos.
     *
     * @return puntos acumulados
     */
    public int getPuntos(){
        return 3 * victorias + empates;
    }
}
