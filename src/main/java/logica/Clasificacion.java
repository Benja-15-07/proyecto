package logica;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Clasificacion implements Observer {
    private List<Participante> clasificacion;

    public Clasificacion(){
        this.clasificacion = new ArrayList<>();
    }

    @Override
    public void actualizar(List<Participante> participantes){
        clasificacion.clear();
        clasificacion.addAll(participantes);
        clasificacion.sort(Comparator.comparingDouble(
                (Participante p) -> p.getEstadistica().getPuntos()).reversed());
    }

    public List<Participante> getClasificacion() {
        return  new ArrayList<>(clasificacion);
    }
}
