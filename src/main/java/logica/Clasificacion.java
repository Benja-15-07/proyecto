package logica;

import java.util.ArrayList;
import java.util.Comparator;

public class Clasificacion {
    private ArrayList<Participante> clasificacion;

    public Clasificacion(){
        this.clasificacion = new ArrayList<>();
    }

    public void actualizar(ArrayList<Participante> participantes){
        clasificacion.clear();
        clasificacion.addAll(participantes);
        clasificacion.sort(Comparator.comparingDouble(
                (Participante p) -> p.getEstadistica().getPorcentajeVictorias()).reversed());
    }

    public ArrayList<Participante> getClasificacion() {
        return  new ArrayList<>(clasificacion);
    }
}
