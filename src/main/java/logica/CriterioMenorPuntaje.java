package logica;

/** CriterioMenorPuntaje deteremina al participante con menor puntaje como ganador */
public class CriterioMenorPuntaje implements CriterioStrategy{
    @Override
    public Participante evaluarResultado(Enfrentamiento enfrentamiento){
        if(enfrentamiento.getResultado().getPuntuacion1() < enfrentamiento.getResultado().getPuntuacion2()){
            return enfrentamiento.getParticipante1(); // Puntaje más bajo es 1, gana el participante 1
        }
        else if(enfrentamiento.getResultado().getPuntuacion1() > enfrentamiento.getResultado().getPuntuacion2()){
            return enfrentamiento.getParticipante2(); // Puntaje más bajo es 2, gana el participante 2
        }
        else return null; // Empate
    }

    @Override
    public String getNombreCriterio() {
        return "Menor puntaje";
    }
}