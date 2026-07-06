package logica;

/** CriterioMayorPuntaje deteremina al participante con mayor puntaje como ganador */
public class CriterioMayorPuntaje implements CriterioStrategy{
    @Override
    public Participante evaluarResultado(Enfrentamiento enfrentamiento){
        if(enfrentamiento.getResultado().getPuntuacion1() > enfrentamiento.getResultado().getPuntuacion2()){
            return enfrentamiento.getParticipante1(); // Puntaje más alto es 1, gana el participante 1
        }
        else if(enfrentamiento.getResultado().getPuntuacion1() < enfrentamiento.getResultado().getPuntuacion2()){
            return enfrentamiento.getParticipante2(); // Puntaje más alto es 2, gana el participante 2
        }
        else return null; // Empate
    }

    @Override
    public String getNombreCriterio() {
        return "Mayor puntaje";
    }
}