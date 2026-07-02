package logica;

/** CriterioMayorPuntaje deteremina al ganador de forma binaria */
public class CriterioBinario implements CriterioStrategy{
    @Override
    public Participante evaluarResultado(Enfrentamiento enfrentamiento) {
        if(enfrentamiento.getResultado().getPuntuacion1() == 1){
            return enfrentamiento.getParticipante1();
        } else return enfrentamiento.getParticipante2();
    }
}