package logica;

import java.util.ArrayList;

/** FormatoEliminatoria empareja de dos en dos a los participantes.
 * Es necesario manjear la lógica de las múltiples rondas de forma externa
 * esto incluye a la distinción entre eliminatoria simple y doble.*/
public class FormatoEliminatoria implements FormatoStrategy{
    @Override
    public ArrayList<Enfrentamiento> generarEnfrentamientos(ArrayList<Participante> participantes) {
        ArrayList<Enfrentamiento> enfrentamientos = new ArrayList<>();
        for(int i = 0; i < participantes.size()/2; i++) {
            enfrentamientos.add(new Enfrentamiento(participantes.get(i*2), participantes.get((i*2)+1)));
        }
        return enfrentamientos;
    }

    @Override
    public Bracket generarBracket(ArrayList<Enfrentamiento> enfrentamientos) {
        return new Bracket(enfrentamientos);
    }

    @Override
    public String getNombreFormato() {
        return "Eliminatoria";
    }
}
