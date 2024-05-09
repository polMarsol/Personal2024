Queue<Set<States>> cola = new LinkedList<>();
    List<Set<States>> listaConjuntos = new ArrayList<>();
    List<States> listaEstados = new ArrayList<>();

Se crean una cola y dos listas. La cola se usará para almacenar los conjuntos de estados que aún no se han procesado. Las listas se usan para almacenar los conjuntos de estados que ya se han procesado y sus correspondientes estados en el autómata determinista.
