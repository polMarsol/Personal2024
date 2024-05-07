import java.util.*;

public class AutomataDeterminista {
    private List<States> estados;
    private List<Transitions> transiciones;

    public AutomataDeterminista() {
        this.estados = new ArrayList<>();
        this.transiciones = new ArrayList<>();
    }

    public void agregarEstado(States estado) {
        this.estados.add(estado);
    }

    public void agregarTransicion(Transitions transicion) {
        this.transiciones.add(transicion);
    }

    public List<States> getEstados() {
        return estados;
    }

    public List<Transitions> getTransiciones() {
        return transiciones;
    }

    public AutomataDeterminista determinize(Automata afnd, String[] alfabeto) {
        AutomataDeterminista afd = new AutomataDeterminista();
        Queue<Set<States>> cola = new LinkedList<>();
        Map<Set<States>, States> mapeo = new HashMap<>();

        Set<States> estadoInicial = new HashSet<>();
        estadoInicial.add(afnd.getEstadoInicial());
        cola.add(estadoInicial);

        while (!cola.isEmpty()) {
            Set<States> conjuntoActual = cola.remove();
            boolean esFinal = conjuntoActual.stream().anyMatch(States::esFinal);
            States estadoAfd = new States(conjuntoActual.toString(), esFinal);
            afd.agregarEstado(estadoAfd);
            mapeo.put(conjuntoActual, estadoAfd);

            for (String simbolo : alfabeto) {
                Set<States> nuevoConjunto = new HashSet<>();
                for (States estado : conjuntoActual) {
                    for (Transitions transicion : afnd.getTransiciones()) {
                        if (transicion.getEstadoInicial().equals(estado) && transicion.getSimbolo().equals(simbolo)) {
                            nuevoConjunto.add(transicion.getEstadoFinal());
                        }
                    }
                }
                if (!mapeo.containsKey(nuevoConjunto)) {
                    cola.add(nuevoConjunto);
                }
                States estadoFinal = mapeo.get(nuevoConjunto);
                if (estadoFinal == null) {
                    esFinal = nuevoConjunto.stream().anyMatch(States::esFinal);
                    estadoFinal = new States(nuevoConjunto.toString(), esFinal);
                    afd.agregarEstado(estadoFinal);
                    mapeo.put(nuevoConjunto, estadoFinal);
                }
                afd.agregarTransicion(new Transitions(estadoAfd, estadoFinal, simbolo));
            }
        }
        return afd;
    }
    public void print() {
        System.out.println("Estados:");
        for (States estado : estados) {
            System.out.println("Nombre: " + estado.getNombre() + ", Es final: " + estado.esFinal());
        }

        System.out.println("Transiciones:");
        for (Transitions transicion : transiciones) {
            System.out.println("Desde el estado " + transicion.getEstadoInicial().getNombre() +
                    " hasta el estado " + transicion.getEstadoFinal().getNombre() +
                    " con el símbolo " + transicion.getSimbolo());
        }
    }
}