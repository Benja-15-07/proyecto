package logica;

public class Estadistica {
    private int victorias;
    private int derrotas;
    private int empates;
    private int puntosFavor;
    private int puntosContra;

    public Estadistica() {
        this.victorias = 0;
        this.derrotas = 0;
        this.empates = 0;
        this.puntosFavor = 0;
        this.puntosContra = 0;

    }

    public void agregarVictoria(){
        victorias++;
    }

    public void agregarDerrota(){
        derrotas++;
    }

    public void agregarEmpate(){
        empates++;
    }

    public void agregarMarcador(int favor, int contra){
        puntosFavor += favor;
        puntosContra += contra;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public int getPuntosFavor() {
        return puntosFavor;
    }

    public int getPuntosContra() {
        return puntosContra;
    }

    public int getDiferencia() {
        return puntosFavor - puntosContra;
    }

    public int getPartidasJugadas(){
        return victorias + derrotas + empates;
    }

    public double getPorcentajeVictorias(){
        int total = victorias + derrotas + empates;
        if(total == 0) {
            return 0;
        }
        return (double) victorias / total;
    }

    public int getPuntos(){
        return 3 * victorias + empates;
    }
}
