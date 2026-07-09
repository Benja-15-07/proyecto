package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** FormatoEliminatoria empareja de dos en dos a los participantes.
 * Es necesario manjear la lógica de las múltiples rondas de forma externa
 * esto incluye a la distinción entre eliminatoria simple y doble.*/
public class FormatoEliminatoria implements FormatoStrategy{
    @Override
    public List<Enfrentamiento> generarEnfrentamientos(List<Participante> participantes) {
        int n = participantes.size();
        if(n < 2 || (n & (n - 1)) != 0){
            throw new IllegalArgumentException("El formato eliminatoria requiere una cantidad de participantes que sea potencia de 2.");
        }

        List<Enfrentamiento> enfrentamientos = new ArrayList<>();
        for(int i = 0; i < participantes.size()/2; i++) {
            enfrentamientos.add(new Enfrentamiento(participantes.get(i*2), participantes.get((i*2)+1)));
        }
        return enfrentamientos;
    }

    @Override
    public Bracket generarBracket(List<Enfrentamiento> enfrentamientos) {
        return new Bracket(enfrentamientos);
    }

    @Override
    public String getNombreFormato() {
        return "Eliminatoria";
    }
}
