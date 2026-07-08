package logica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Torneo {
    private String nombre;
    private String disciplina;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private FormatoStrategy formato;
    private CriterioStrategy criterio;

    private List<Enfrentamiento> enfrentamientos;
    private int rondaActual;

    private List<Participante> participantes;
    private Clasificacion clasificacion;
    private Calendario calendario;
    private Bracket bracket;

    private ArrayList<Observer> observers;

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
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

    public void generarEnfrentamientos(){
        enfrentamientos = formato.generarEnfrentamientos(participantes);
        this.rondaActual = 1;

        for(Enfrentamiento enf : enfrentamientos){
            enf.setRonda(rondaActual);
        }

        fechaFin = formato.calcularFechaFin(fechaInicio, participantes.size());

        calendario = new Calendario(enfrentamientos, fechaInicio);
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

    public int getRondaActual() {
        return rondaActual;
    }
}
