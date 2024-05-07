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

// ... código anterior

        for (int i = 0; i < numEstados; i++) {
            System.out.println("Ingrese el nombre del estado " + (i + 1) + ":");
            String nombreEstado = scanner.nextLine();
            System.out.println("¿Es este estado final? (si/no)");
            boolean esFinal = scanner.nextLine().equalsIgnoreCase("si");
            automata.agregarEstado(new States(nombreEstado, esFinal));
        }

        System.out.println("Ingrese el nombre del estado inicial:");
        String nombreEstadoInicial = scanner.nextLine();
        States estadoInicial = automata.getEstados().stream()
                .filter(estado -> estado.getNombre().equals(nombreEstadoInicial))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado inicial no encontrado"));

        automata.setEstadoInicial(estadoInicial);

// ... código posterior

        for (int i = 0; i < numEstados; i++) {
            for (States estadoFinal : automata.getEstados()) {
                System.out.println("Desde el estado " + automata.getEstados().get(i).getNombre() +
                        ", ¿con qué símbolo(s) se va al estado " + estadoFinal.getNombre() + "? (separados por comas, o dejar en blanco si no hay transiciones)");
                String simbolosTransicion = scanner.nextLine();
                if (!simbolosTransicion.isEmpty()) {
                    String[] simbolosEstadoFinal = simbolosTransicion.split(",");
                    for (String simbolo : simbolosEstadoFinal) {
                        automata.agregarTransicion(new Transitions(automata.getEstados().get(i), estadoFinal, simbolo.trim()));
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