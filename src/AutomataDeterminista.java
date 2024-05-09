import java.util.*;

public class AutomataDeterminista {
    private List<States> estados = new ArrayList<>();
    private List<Transitions> transiciones = new ArrayList<>();

    public void agregarEstado(States estado) {
        estados.add(estado);
    }

    public void agregarTransicion(Transitions transicion) {
        transiciones.add(transicion);
    }

    public AutomataDeterminista determinize(Automata afnd, String[] alfabeto) {
        AutomataDeterminista afd = new AutomataDeterminista();
        Queue<Set<States>> cola = new LinkedList<>();
        Map<Set<States>, States> mapaConjuntoEstado = new HashMap<>();

        Set<States> estadoInicial = new HashSet<>();
        estadoInicial.add(afnd.getEstadoInicial());
        cola.add(estadoInicial);

        States estadoPozo = null;

        while (!cola.isEmpty()) {
            Set<States> conjuntoActual = cola.remove();
            boolean esFinal = conjuntoActual.stream().anyMatch(States::esFinal);
            States estadoAfd = mapaConjuntoEstado.get(conjuntoActual);
            if (estadoAfd == null) {
                estadoAfd = new States(conjuntoActual.toString(), esFinal);
                afd.agregarEstado(estadoAfd);
                mapaConjuntoEstado.put(conjuntoActual, estadoAfd);
            }

            for (String simbolo : alfabeto) {
                Set<States> nuevoConjunto = new HashSet<>();
                for (States estado : conjuntoActual) {
                    for (Transitions transicion : afnd.getTransiciones()) {
                        if (transicion.getEstadoInicial().equals(estado) && (transicion.getSimbolo().equals(simbolo) || transicion.getSimbolo().equals("lambda"))) {
                            if (transicion.getSimbolo().equals(simbolo) && existeTransicionLambda(afd, estadoAfd, transicion.getEstadoFinal())) {
                                continue;
                            }
                            nuevoConjunto.add(transicion.getEstadoFinal());
                            if (transicion.getSimbolo().equals("lambda")) {
                                if (!existeTransicionLambda(afd, estadoAfd, transicion.getEstadoFinal())) {
                                    afd.agregarTransicion(new Transitions(estadoAfd, transicion.getEstadoFinal(), "lambda"));
                                }
                            }
                        }
                    }
                }

                Set<States> cierreLambda = new HashSet<>();
                for (States estado : nuevoConjunto) {
                    cierreLambda.addAll(cierreLambda(estado, afnd.getTransiciones()));
                }
                nuevoConjunto.addAll(cierreLambda);

                if (nuevoConjunto.isEmpty()) {
                    if (estadoPozo == null) {
                        estadoPozo = new States("[pozo]", false);
                        afd.agregarEstado(estadoPozo);
                    }
                    afd.agregarTransicion(new Transitions(estadoAfd, estadoPozo, simbolo));
                } else {
                    if (!mapaConjuntoEstado.containsKey(nuevoConjunto)) {
                        cola.add(nuevoConjunto);
                    }
                    States estadoFinal = mapaConjuntoEstado.get(nuevoConjunto);
                    if (estadoFinal == null) {
                        esFinal = nuevoConjunto.stream().anyMatch(States::esFinal);
                        estadoFinal = new States(nuevoConjunto.toString(), esFinal);
                        afd.agregarEstado(estadoFinal);
                        mapaConjuntoEstado.put(nuevoConjunto, estadoFinal);
                    }
                    afd.agregarTransicion(new Transitions(estadoAfd, estadoFinal, simbolo));
                }
            }
        }

        if (estadoPozo != null) {
            for (String simbolo : alfabeto) {
                afd.agregarTransicion(new Transitions(estadoPozo, estadoPozo, simbolo));
            }
        }

        return afd;
    }
    private States[] getEstados() {
        return estados.toArray(new States[0]);
    }

    private boolean existeTransicionLambda(AutomataDeterminista afd, States estadoInicial, States estadoFinal) {
        for (Transitions transicion : afd.getTransiciones()) {
            if (transicion.getEstadoInicial().equals(estadoInicial) && transicion.getEstadoFinal().equals(estadoFinal) && transicion.getSimbolo().equals("lambda")) {
                return true;
            }
        }
        return false;
    }

    private Set<States> cierreLambda(States estado, List<Transitions> transiciones) {
        Set<States> cierre = new HashSet<>();
        Queue<States> cola = new LinkedList<>();
        cola.add(estado);

        while (!cola.isEmpty()) {
            States estadoActual = cola.remove();
            cierre.add(estadoActual);

            for (Transitions transicion : transiciones) {
                if (transicion.getEstadoInicial().equals(estadoActual) && transicion.getSimbolo().equals("lambda")) {
                    if (!cierre.contains(transicion.getEstadoFinal())) {
                        cola.add(transicion.getEstadoFinal());
                    }
                }
            }
        }

        return cierre;
    }

    public void print() {
        System.out.println("Estados:");
        for (States estado : estados) {
            System.out.println("Nombre: " + estado.getNombre() + ", Es final: " + estado.esFinal());
        }

        System.out.println("Transiciones:");
        Set<States> estadosConLambda = new HashSet<>();
        for (Transitions transicion : transiciones) {
            if (transicion.getSimbolo().equals("lambda")) {
                estadosConLambda.add(transicion.getEstadoInicial());
            }
        }
        for (Transitions transicion : transiciones) {
            if (!estadosConLambda.contains(transicion.getEstadoInicial()) || transicion.getSimbolo().equals("lambda")) {
                String simbolo = transicion.getSimbolo().equals("lambda") ? "lambda" : transicion.getSimbolo();
                System.out.println("Desde el estado " + transicion.getEstadoInicial().getNombre() +
                        " hasta el estado " + transicion.getEstadoFinal().getNombre() +
                        " con el símbolo " + simbolo);
            }
        }
    }
    public List<Transitions> getTransiciones() {
        return transiciones;
    }
}