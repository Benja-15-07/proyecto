package logica;

import java.time.LocalDate;

public class Organizador {
    private String nombre;
    private String contacto;
    private Torneo torneo;

    public Organizador(String nombre, String contacto) {
        this.nombre = nombre;
        this.contacto = contacto;
    }

    public void crearTorneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio){
        torneo = new Torneo(nombre, disciplina, fechaInicio, formato, criterio);
    }

    public void addParticipante(Participante participante){
        torneo.addParticipante(participante);
    }

    public void generarEnfrentamientos(){
        torneo.generarEnfrentamientos();
    }

    public void registrarResultado(Enfrentamiento enfrentamiento, int puntuacion1, int puntuacion2){
        torneo.registrarResultado(enfrentamiento, puntuacion1, puntuacion2);
    }

    public Torneo getTorneo() {
        return torneo;
    }
}
