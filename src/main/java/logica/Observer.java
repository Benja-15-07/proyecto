package logica;

import java.util.List;

/**
 * Interfaz de patrón observer.
 * Define como se deben actualizar los objetos
 * al agregar un participante, registrar un enfrentamiento, al generar
 * los enfrentamientos en un torneo o al generar una nueva ronda.
 */
public interface Observer {

    /**
     * Actualiza el observador con la lista actual de participantes.
     *
     * @param participantes participantes del torneo
     */
    public void actualizar(List<Participante> participantes);
}
