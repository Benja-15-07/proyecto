package logica;

import java.util.ArrayList;

public class Calendario {
    private ArrayList<Enfrentamiento> calendario;

    public Calendario() {
        this.calendario = new ArrayList<>();
    }

    public void actualizar(ArrayList<Enfrentamiento> enfrentamientos){
        calendario.clear();
        calendario.addAll(enfrentamientos);
    }

    public ArrayList<Enfrentamiento> getCalendario() {
        return new ArrayList<>(calendario);
    }
}
