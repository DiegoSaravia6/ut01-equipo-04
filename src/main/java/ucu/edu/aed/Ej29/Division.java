package ucu.edu.aed.Ej29;

public enum Division {
    BRONCE,
    PLATA,
    ORO,
    PLATINO,
    DIAMANTE;

    public int prioridad() {
        switch (this) {
            case BRONCE:
                return 1;
            case PLATA:
                return 2;
            case ORO:
                return 3;
            case PLATINO:
                return 4;
            case DIAMANTE:
                return 5;
            default:
                return 0;
        }
    }
}
