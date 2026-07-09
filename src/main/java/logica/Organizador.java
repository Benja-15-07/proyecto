package logica;

import java.time.LocalDate;

/**
 * Representa al oraganizador del torneo.
 * Se encarga de gestionar el torneo.
 */
public class Organizador {
    private String nombre;
    private String contacto;
    private Torneo torneo;

    /**
     * Crea un organizador.
     *
     * @param nombre nombre del organizador
     * @param contacto contacto del organizador
     */
    public Organizador(String nombre, String contacto) {
        this.nombre = nombre;
        this.contacto = contacto;
    }

    /**
     * Crea un torneo.
     *
     * @param nombre nombre del torneo
     * @param disciplina disciplina del torneo
     * @param fechaInicio fecha de inicio del torneo
     * @param formato formato del torneo
     * @param criterio criterio para evaluar los resultados
     */
    public void crearTorneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio){
        torneo = new Torneo(nombre, disciplina, fechaInicio, formato, criterio);
    }

    /**
     * Agrega un participante al torneo.
     *
     * @param participante participante que se agregará
     */
    public void addParticipante(Participante participante){
        torneo.addParticipante(participante);
    }

    /**
     * Genera los enfrentamientos del torneo.
     */
    public void generarEnfrentamientos(){
        torneo.generarEnfrentamientos();
    }

    /**
     * Registra el resultado de un enfrentamiento.
     *
     * @param enfrentamiento enfrentamiento al que se le registrará un resultado
     * @param puntuacion1 puntuación del primer participante
     * @param puntuacion2 puntuación del segundo participante
     */
    public void registrarResultado(Enfrentamiento enfrentamiento, int puntuacion1, int puntuacion2){
        torneo.registrarResultado(enfrentamiento, puntuacion1, puntuacion2);
    }

    /**
     * Devuelve el torneo gestionado por el organizador.
     *
     * @return torneo
     */
    public Torneo getTorneo() {
        return torneo;
    }
}
