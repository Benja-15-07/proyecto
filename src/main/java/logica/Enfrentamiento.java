package logica;

import java.time.LocalDate;

public class Enfrentamiento {
    private Participante participante1;
    private Participante participante2;
    private LocalDate fecha;
    private Resultado    resultado;
    private Participante ganador;
    private int ronda;

    public Enfrentamiento(Participante participante1, Participante participante2) {
        this.participante1 = participante1;
        this.participante2 = participante2;
        this.fecha = null;
        this.resultado = null;
        this.ronda = 0;
    }

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

    public boolean estadoFinalizado(){
        return resultado != null;
    }

    public Participante getParticipante1() {
        return participante1;
    }

    public Participante getParticipante2() {
        return participante2;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public Participante getGanador() {
        return ganador;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }
}
