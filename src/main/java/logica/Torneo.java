package logica;
import java.time.LocalDate;
import java.util.ArrayList;

public class Torneo {
    private String nombre;
    private String disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private FormatoStrategy formato;
    private CriterioStrategy criterio;

    private ArrayList<Participante> participantes;
    private ArrayList<Enfrentamiento> enfrentamientos;
    private Clasificacion clasificacion;
    private Calendario calendario;
    private Bracket bracket;

    private ArrayList<Observer> observers;

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, LocalDate fechaFin, FormatoStrategy formato, CriterioStrategy criterio) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.formato = formato;
        this.criterio = criterio;
        this.participantes = new ArrayList<>();
        this.enfrentamientos = new ArrayList<>();
        this.clasificacion = new Clasificacion();
        this.calendario = null;
        this.bracket = null;

        this.observers = new ArrayList<>();
        this.observers.add(this.clasificacion);
    }

    public void addParticipante(Participante participante){
        participantes.add(participante);
        actualizar();
    }

    public void registrarResultado(Enfrentamiento enfrentamiento,int puntaje1, int puntaje2){
        enfrentamiento.registrarResultado(criterio, puntaje1, puntaje2);
        actualizar();
    }

    public void generarEnfrentamiento(){
        enfrentamientos = formato.generarEnfrentamientos(participantes);
        calendario = new Calendario(enfrentamientos);
        bracket = formato.generarBracket(enfrentamientos);

        actualizar();
    }

    public void actualizar(){
        for(Observer s : observers){
            s.actualizar(participantes);
        }
    }

    public ArrayList<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    public ArrayList<Enfrentamiento> getEnfrentamientos() {
        return new ArrayList<>(enfrentamientos);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void asignarFechaEnfrentamiento(Enfrentamiento enfrentamiento, LocalDate fecha) {
        if (fecha != null && fechaInicio != null && fecha.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha del enfrentamiento no puede ser anterior al inicio del torneo.");
        }
        if (fecha != null && fechaFin != null && fecha.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha del enfrentamiento no puede ser posterior al fin del torneo.");
        }
        if (calendario == null) {
            calendario = new Calendario(enfrentamientos);
        }
        calendario.asignarFecha(enfrentamiento, fecha);
    }

    public String getNombreFormato() {
        return formato.getNombreFormato();
    }

    public String getNombreCriterio() {
        return criterio.getNombreCriterio();
    }

    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public Bracket getBracket() {
        return bracket;
    }
}
