import logica.*;
import visual.*;

import java.util.HashMap;
import java.util.Map;

public class ControladorTorneo {
    private Organizador organizador;
    private Ventana ventana;
    private Map<String, Enfrentamiento> enfrentamientosMap;

    public ControladorTorneo(Ventana ventana){
        this.ventana = ventana;
        this.organizador = new Organizador("Organizador","");
        this.enfrentamientosMap = new HashMap<>();
        eventos();
    }

    private void eventos(){
        
    }
}
