package logica;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** FormatoLiga genera enfrentamientos para cada posible combinación entre participantes (sin repeticiones)*/
public class FormatoLiga implements FormatoStrategy {
    @Override
    public List<Enfrentamiento> generarEnfrentamientos(List<Participante> participantes) {
        int n = participantes.size();
        if(n < 2){
            throw new IllegalArgumentException("El formato liga reguiere al menos 2 participantes.");
        }

        List<Enfrentamiento> enfrentamientos = new ArrayList<>();
        for(int i = 0; i < participantes.size() - 1; i++){
            for(int j = i+1; j < participantes.size(); j++){
                enfrentamientos.add(new Enfrentamiento(participantes.get(i), participantes.get(j)));
            }
        }
        return enfrentamientos;
    }

    @Override
    public Bracket generarBracket(List<Enfrentamiento> enfrentamientos) {
        return null;
    }

    @Override
    public String getNombreFormato() {
        return "Liga";
    }
}
