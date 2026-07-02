package logica;

public class Enfrentamiento {
    private Participante participante1;
    private Participante participante2;
    private Resultado    resultado;
    private Participante ganador;

    public Enfrentamiento(Participante participante1, Participante participante2) {
        this.participante1 = participante1;
        this.participante2 = participante2;
        this.resultado = null;
    }

    public Participante getParticipante1() {
        return participante1;
    }

    public Participante getParticipante2() {
        return participante2;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public Participante getGanador() {
        return ganador;
    }
}
