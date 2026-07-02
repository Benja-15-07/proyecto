package logica;

import java.util.ArrayList;

public class Clasificacion {
    private ArrayList<Participante> clasificacion;

    public Clasificacion(){
        this.clasificacion = new ArrayList<>();
    }

    public ArrayList<Participante> getClasificacion() {
        return  new ArrayList<>(clasificacion);
    }
}
