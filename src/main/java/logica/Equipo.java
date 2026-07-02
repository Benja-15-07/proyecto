package logica;

import java.util.ArrayList;

public class Equipo extends Participante {
    private ArrayList<Individuo> integrantes;

    public Equipo(String nombre, String contacto) {
        super(nombre, contacto);
        integrantes = new ArrayList<>();
    }

    public void addIntegrante(Individuo individuo){
        integrantes.add(individuo);
    }
}
