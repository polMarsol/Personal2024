import java.util.ArrayList;
import java.util.List;

public class Automata {
    private List<States> estados;
    private List<Transitions> transiciones;
    private States estadoInicial;

    public Automata() {
        this.estados = new ArrayList<>();
        this.transiciones = new ArrayList<>();
    }

    public void agregarEstado(States estado) {
        this.estados.add(estado);
        if (this.estadoInicial == null) {
            this.estadoInicial = estado;
        }
    }

    public void agregarTransicion(Transitions transicion) {
        this.transiciones.add(transicion);
    }

    public States getEstadoInicial() {
        return estadoInicial;
    }

    public List<States> getEstados() {
        return estados;
    }

    public List<Transitions> getTransiciones() {
        return transiciones;
    }
    public void setEstadoInicial(States estadoInicial) {
        this.estadoInicial = estadoInicial;
    }
}