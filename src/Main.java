import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el alfabeto a utilizar (separado por comas):");
        String alfabeto = scanner.nextLine();
        String[] simbolos = alfabeto.split(",");

        System.out.println("Ingrese el número de estados:");
        int numEstados = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        Automata automata = new Automata();
        for (int i = 0; i < numEstados; i++) {
            System.out.println("Ingrese el nombre del estado " + (i + 1) + ":");
            String nombreEstado = scanner.nextLine();
            System.out.println("¿Es este estado final? (si/no)");
            boolean esFinal = scanner.nextLine().equalsIgnoreCase("si");
            automata.agregarEstado(new States(nombreEstado, esFinal));
        }

        System.out.println("Ingrese el nombre del estado inicial:");
        String nombreEstadoInicial = scanner.nextLine();
        States estadoInicial = null;

        for (States estado : automata.getEstados()) {
            if (estado.getNombre().equals(nombreEstadoInicial)) {
                estadoInicial = estado;
                break;
            }
        }

        if (estadoInicial == null) {
            throw new IllegalArgumentException("Estado inicial no encontrado");
        }

        automata.setEstadoInicial(estadoInicial);

// ... código posterior

        for (int i = 0; i < numEstados; i++) {
            for (String simbolo : simbolos) {
                System.out.println("Desde el estado " + automata.getEstados().get(i).getNombre() +
                        ", ¿a qué estados se va con el símbolo " + simbolo + "? (separados por comas, o dejar en blanco si no hay transiciones)");
                String nombresEstadosFinales = scanner.nextLine();
                if (!nombresEstadosFinales.isEmpty()) {
                    String[] nombresEstadoFinal = nombresEstadosFinales.split(",");
                    for (String nombreEstadoFinal : nombresEstadoFinal) {
                        States estadoFinal = null;
                        for (States estado : automata.getEstados()) {
                            if (estado.getNombre().equals(nombreEstadoFinal.trim())) {
                                estadoFinal = estado;
                                break;
                            }
                        }
                        if (estadoFinal == null) {
                            throw new IllegalArgumentException("Estado no encontrado");
                        }
                        automata.agregarTransicion(new Transitions(automata.getEstados().get(i), estadoFinal, simbolo));
                    }
                }
                System.out.println("¿Existe una transición lambda desde el estado " + automata.getEstados().get(i).getNombre() + "? (si/no)");
                String esLambda = scanner.nextLine();
                if (esLambda.equalsIgnoreCase("si")) {
                    System.out.println("¿A qué estados se va con la transición lambda desde el estado " + automata.getEstados().get(i).getNombre() + "? (separados por comas)");
                    String nombresEstadosFinalesLambda = scanner.nextLine();
                    String[] nombresEstadoFinalLambda = nombresEstadosFinalesLambda.split(",");
                    for (String nombreEstadoFinalLambda : nombresEstadoFinalLambda) {
                        States estadoFinalLambda = null;
                        for (States estado : automata.getEstados()) {
                            if (estado.getNombre().equals(nombreEstadoFinalLambda.trim())) {
                                estadoFinalLambda = estado;
                                break;
                            }
                        }
                        if (estadoFinalLambda == null) {
                            throw new IllegalArgumentException("Estado no encontrado");
                        }
                        automata.agregarTransicion(new Transitions(automata.getEstados().get(i), estadoFinalLambda, "lambda"));
                    }
                }
            }
        }

        scanner.close();
        AutomataDeterminista afd = new AutomataDeterminista();
        afd = afd.determinize(automata, simbolos);
        afd.print();
    }

}