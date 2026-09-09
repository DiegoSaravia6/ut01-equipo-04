package ucu.edu.aed;

import ucu.edu.aed.Taller.Vehiculo;
import ucu.edu.aed.tda.ArbolAVL;
import ucu.edu.aed.tda.Lista;

/**
 * Desafío 3: búsqueda lineal por patente del Hito 1 vs índice AVL del Hito 2.
 * Se usa el peor caso de la lista (patente ubicada en el último nodo).
 */
public class BenchmarkBusquedaVehiculos {
    private static volatile Object sink;
    private static final int REPETICIONES = 200;
    private static final int RONDAS = 7;

    public static void main(String[] args) {
        int[] tamaños = {1_000, 5_000, 10_000, 25_000, 50_000, 100_000};
        System.out.println("n,lista_us_mediana,avl_us_mediana,mejora_x");
        for (int n : tamaños) ejecutar(n);
    }

    private static void ejecutar(int n) {
        Lista<Vehiculo> lista = new Lista<>();
        ArbolAVL<Vehiculo> avl = new ArbolAVL<>();

        // Setup fuera de la medición. Insertar al frente evita que el costo O(n²)
        // del append de Lista contamine el experimento, porque aquí se mide buscar().
        for (int i = n - 1; i >= 0; i--) {
            Vehiculo v = nuevoVehiculo(i);
            lista.agregar(0, v);
            avl.insertar(v);
        }

        String objetivo = patente(n - 1);

        // Calentamiento de JVM/JIT.
        for (int i = 0; i < 100; i++) {
            sink = lista.buscar(v -> objetivo.equals(v.getPatente()));
            sink = avl.buscar(v -> objetivo.compareTo(v.getPatente()));
        }

        double[] tiemposLista = new double[RONDAS];
        double[] tiemposAvl = new double[RONDAS];

        for (int ronda = 0; ronda < RONDAS; ronda++) {
            tiemposLista[ronda] = medirLista(lista, objetivo);
            tiemposAvl[ronda] = medirAvl(avl, objetivo);
        }

        double listaUs = mediana(tiemposLista);
        double avlUs = mediana(tiemposAvl);
        System.out.printf("%d,%.3f,%.3f,%.2f%n", n, listaUs, avlUs, listaUs / avlUs);
    }

    private static double medirLista(Lista<Vehiculo> lista, String objetivo) {
        long inicio = System.nanoTime();
        for (int i = 0; i < REPETICIONES; i++) {
            sink = lista.buscar(v -> objetivo.equals(v.getPatente()));
        }
        return (System.nanoTime() - inicio) / 1000.0 / REPETICIONES;
    }

    private static double medirAvl(ArbolAVL<Vehiculo> avl, String objetivo) {
        long inicio = System.nanoTime();
        for (int i = 0; i < REPETICIONES; i++) {
            sink = avl.buscar(v -> objetivo.compareTo(v.getPatente()));
        }
        return (System.nanoTime() - inicio) / 1000.0 / REPETICIONES;
    }

    private static double mediana(double[] datos) {
        double[] copia = new double[datos.length];
        for (int i = 0; i < datos.length; i++) copia[i] = datos[i];
        for (int i = 1; i < copia.length; i++) {
            double valor = copia[i];
            int j = i - 1;
            while (j >= 0 && copia[j] > valor) {
                copia[j + 1] = copia[j];
                j--;
            }
            copia[j + 1] = valor;
        }
        return copia[copia.length / 2];
    }

    private static Vehiculo nuevoVehiculo(int i) {
        return new Vehiculo(patente(i), "Marca", "Modelo", "Dueño");
    }

    private static String patente(int i) {
        return String.format("V%08d", i);
    }
}
