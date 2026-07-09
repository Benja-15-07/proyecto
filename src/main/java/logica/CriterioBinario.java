package logica;

/** CriterioMayorPuntaje deteremina al ganador de forma binaria */
public class CriterioBinario implements CriterioStrategy{
    @Override
    public Participante evaluarResultado(Enfrentamiento enfrentamiento) {
        int puntuacion1 = enfrentamiento.getResultado().getPuntuacion1();
        int puntuacion2 = enfrentamiento.getResultado().getPuntuacion2();

        if(puntuacion1 == 1 && puntuacion2 == 0){
            return enfrentamiento.getParticipante1();
        } else if (puntuacion1 == 0 && puntuacion2 == 1) {
            return enfrentamiento.getParticipante2();
        }
        else {
            throw new IllegalArgumentException("Resultatado binario inválido.");
        }
    }

    @Override
    public String getNombreCriterio() {
        return "Binario";
    }
}