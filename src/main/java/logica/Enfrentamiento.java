package logica;

import java.time.LocalDate;

/**
 * Representa un enfrentamiento entre 2 participantes.
 */
public class Enfrentamiento {
    private Participante participante1;
    private Participante participante2;
    private LocalDate fecha;
    private Resultado    resultado;
    private Participante ganador;
    private int ronda;

    /**
     * Crea un enfrentamiento entre 2 participantes.
     *
     * @param participante1 primer participante
     * @param participante2 segundo participante
     */
    public Enfrentamiento(Participante participante1, Participante participante2) {
        this.participante1 = participante1;
        this.participante2 = participante2;
        this.fecha = null;
        this.resultado = null;
        this.ronda = 0;
    }

    /**
     * Registra el resultado del enfrentamiento, determina el ganador según el criterio
     * utilizado y actualiza las estadísticas de los participantes.
     *
     * @param criterio criterio utilizado para determinar el ganador
     * @param puntuacion1 puntuación del primer participante
     * @param puntuacion2 puntuación del segundo participante
     */
    public void registrarResultado(CriterioStrategy criterio, int puntuacion1, int puntuacion2){
        if(resultado != null){
            throw new IllegalStateException("El enfrentamiento ya tiene un resultado registrado.");
        }

        this.resultado = new Resultado(puntuacion1, puntuacion2);

        participante1.getEstadistica().agregarMarcador(puntuacion1, puntuacion2);
        participante2.getEstadistica().agregarMarcador(puntuacion2, puntuacion1);

        this.ganador = criterio.evaluarResultado(this);

        if(ganador == participante1){
            participante1.getEstadistica().agregarVictoria();
            participante2.getEstadistica().agregarDerrota();
        } else if (ganador == participante2) {
            participante2.getEstadistica().agregarVictoria();
            participante1.getEstadistica().agregarDerrota();
        }
        else {
            participante1.getEstadistica().agregarEmpate();
            participante2.getEstadistica().agregarEmpate();
        }
    }

    /**
     * @return true si el enfrentamiento ha finalizado, false si sigue en curso
     */
    public boolean estadoFinalizado(){
        return resultado != null;
    }

    /**
     * @return primer participante
     */
    public Participante getParticipante1() {
        return participante1;
    }

    /**
     * @return segundo participante
     */
    public Participante getParticipante2() {
        return participante2;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * @return resultado del enfrentamiento o null sí sigue en curso
     */
    public Resultado getResultado() {
        return resultado;
    }

    /**
     * @return ganador del enfrentamiento o null sí sigue en curso
     */
    public Participante getGanador() {
        return ganador;
    }

    public int getRonda() {
        return ronda;
    }

    /**
     * Asigna la ronda del enfrentamiento.
     *
     * @param ronda número de ronda
     */
    public void setRonda(int ronda) {
        this.ronda = ronda;
    }
}
