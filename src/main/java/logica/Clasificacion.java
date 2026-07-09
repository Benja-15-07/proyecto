package logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Representa la clasificacion de participantes de un torneo.
 */
public class Clasificacion implements Observer {
    private List<Participante> clasificacion;

    /**
     * Crea una clasificación vacía.
     */
    public Clasificacion(){
        this.clasificacion = new ArrayList<>();
    }

    /**
     * Actualiza la clasificación a partir de una lista de participantes y
     * la ordena según la puntuación obtenida por cada uno.
     *
     * @param participantes lista de participantes del torneo
     */
    @Override
    public void actualizar(List<Participante> participantes){
        clasificacion.clear();
        clasificacion.addAll(participantes);
        clasificacion.sort(Comparator.comparingDouble(
                (Participante p) -> p.getEstadistica().getPuntos()).reversed());
    }

    /**
     * Devuelve una copia de la clasificación.
     *
     * @return lista de participantes ordenados por puntuación.
     */
    public List<Participante> getClasificacion() {
        return  new ArrayList<>(clasificacion);
    }
}
