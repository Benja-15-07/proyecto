package logica;

import java.util.ArrayList;

/** FormatoLiga genera enfrentamientos para cada posible combinación entre participantes (sin repeticiones)*/
public class FormatoLiga implements FormatoStrategy {
    @Override
    public ArrayList<Enfrentamiento> generarEnfrentamientos(ArrayList<Participante> participantes) {
        ArrayList<Enfrentamiento> enfrentamientos = new ArrayList<>();
        for(int i = 0; i < participantes.size() - 1; i++){
            for(int j = i+1; j < participantes.size(); j++){
                enfrentamientos.add(new Enfrentamiento(participantes.get(i), participantes.get(j)));
            }
        }
        return enfrentamientos;
    }

    @Override
    public Bracket generarBracket(ArrayList<Enfrentamiento> enfrentamientos) {
        return null;
    }

    @Override
    public String getNombreFormato() {
        return "Liga";
    }
}
