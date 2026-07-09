package logica;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un participante de tipo equipo.
 */
public class Equipo extends Participante {
    private List<Individuo> integrantes;

    public Equipo(String nombre, String contacto) {
        super(nombre, contacto);
        integrantes = new ArrayList<>();
    }

    public void addIntegrante(Individuo individuo){
        integrantes.add(individuo);
    }
}
