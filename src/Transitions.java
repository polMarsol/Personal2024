public class Transitions {
    private States estadoInicial;
    private States estadoFinal;
    private String simbolo;

    public Transitions(States estadoInicial, States estadoFinal, String simbolo) {
        this.estadoInicial = estadoInicial;
        this.estadoFinal = estadoFinal;
        this.simbolo = simbolo;
    }
    public States getEstadoInicial() {
        return estadoInicial;
    }

    public States getEstadoFinal() {
        return estadoFinal;
    }

    public String getSimbolo() {
        return simbolo;
    }
}