package logica;

import java.time.LocalDate;

public class Organizador {
    private String nombre;
    private String contacto;

    public Organizador(String nombre, String contacto) {
        this.nombre = nombre;
        this.contacto = contacto;
    }

    public Torneo crearTorneo(String nombre, String disciplina, LocalDate fechaInicio, LocalDate fechaFin, FormatoStrategy formato, CriterioStrategy criterio){
        return new Torneo(nombre, disciplina, fechaInicio, fechaFin, formato, criterio);
    }
}
