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
    private int partidosPorDia;

    private List<Enfrentamiento> enfrentamientos;
    private int rondaActual;

    private List<Participante> participantes;
    private Clasificacion clasificacion;
    private Calendario calendario;
    private Bracket bracket;

    private List<Observer> observers;

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio) {
        this(nombre, disciplina, fechaInicio, formato, criterio, 1);
    }

    public Torneo(String nombre, String disciplina, LocalDate fechaInicio, FormatoStrategy formato, CriterioStrategy criterio, int partidosPorDia) {
        this.nombre = nombre;
        this.disciplina = disciplina;
        this.fechaInicio = fechaInicio;
        this.fechaFin = null;
        this.formato = formato;
        this.criterio = criterio;
        this.partidosPorDia = Math.max(1, partidosPorDia);
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

        calendario = new Calendario(enfrentamientos, fechaInicio, partidosPorDia);
        fechaFin = calcularFechaFinTorneo();
        bracket = formato.generarBracket(enfrentamientos);

        actualizar();
    }

    public void generarSiguienteRonda(List<Participante> ganadores){
        if(!(formato instanceof FormatoEliminatoria) || ganadores.size() <= 1) {
            return;
        }

        rondaActual++;

        List<Enfrentamiento> nuevaRonda = formato.generarEnfrentamientos(ganadores);

        for(Enfrentamiento enf : nuevaRonda){
            enf.setRonda(rondaActual);
        }

        enfrentamientos.addAll(nuevaRonda);

        calendario = new Calendario(enfrentamientos, fechaInicio, partidosPorDia);
        fechaFin = calcularFechaFinTorneo();
        bracket = formato.generarBracket(enfrentamientos);

        actualizar();
    }

    private LocalDate calcularFechaFinTorneo() {
        if (calendario == null) {
            return null;
        }

        if (!(formato instanceof FormatoEliminatoria)) {
            return calendario.getFechaFin();
        }

        int participantesTotales = participantes.size();
        if (participantesTotales < 2) {
            return calendario.getFechaFin();
        }

        int diasTotales = 0;
        int participantesRonda = participantesTotales;

        while (participantesRonda >= 2) {
            int partidosRonda = participantesRonda / 2;
            int diasRonda = (int) Math.ceil((double) partidosRonda / partidosPorDia);
            diasTotales += Math.max(1, diasRonda);
            participantesRonda /= 2;
        }

        return fechaInicio.plusDays(diasTotales - 1L);
    }

    public void actualizar(){
        for(Observer s : observers){
            s.actualizar(participantes);
        }
    }

    public List<Participante> getParticipantes() {
        return new ArrayList<>(participantes);
    }

    public List<Enfrentamiento> getEnfrentamientos() {
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

    public int getRondaActual() {
        return rondaActual;
    }
}
