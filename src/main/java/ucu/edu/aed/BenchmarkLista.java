package ucu.edu.aed;

import ucu.edu.aed.tda.Lista;
import ucu.edu.aed.tda.ListaOptimizada;

public class BenchmarkLista {

    private static final int REPETICIONES = 10;

    public static void main(String[] args) {

        int[] tamaños = {
                1_000,
                5_000,
                10_000,
                25_000,
                50_000,
                100_000
        };

        System.out.println("==============================================================");
        System.out.println("                  BENCHMARK LISTA");
        System.out.println("==============================================================");
        System.out.println("Repeticiones: " + REPETICIONES);
        System.out.println("Primera repeticion descartada por calentamiento de la JVM.");
        System.out.println();

        System.out.printf(
                "%-12s %-18s %-18s%n",
                "Elementos",
                "Lista promedio",
                "Optimizada promedio"
        );

        System.out.printf(
                "%-12s %-18s %-18s%n",
                "",
                "(microsegundos)",
                "(microsegundos)"
        );

        System.out.println("--------------------------------------------------------------");

        for (int tamaño : tamaños) {

            // Primera ejecución: calentamiento de la JVM.
            medirLista(tamaño);
            medirListaOptimizada(tamaño);

            long totalLista = 0;
            long totalOptimizada = 0;

            long minimoLista = Long.MAX_VALUE;
            long maximoLista = Long.MIN_VALUE;

            long minimoOptimizada = Long.MAX_VALUE;
            long maximoOptimizada = Long.MIN_VALUE;

            for (int i = 0; i < REPETICIONES; i++) {

                long tiempoLista = medirLista(tamaño);
                long tiempoOptimizada = medirListaOptimizada(tamaño);

                totalLista += tiempoLista;
                totalOptimizada += tiempoOptimizada;

                minimoLista = Math.min(minimoLista, tiempoLista);
                maximoLista = Math.max(maximoLista, tiempoLista);

                minimoOptimizada =
                        Math.min(minimoOptimizada, tiempoOptimizada);

                maximoOptimizada =
                        Math.max(maximoOptimizada, tiempoOptimizada);
            }

            double promedioLista =
                    (double) totalLista / REPETICIONES;

            double promedioOptimizada =
                    (double) totalOptimizada / REPETICIONES;

            System.out.printf(
                    "%-12d %-18.2f %-18.2f%n",
                    tamaño,
                    promedioLista,
                    promedioOptimizada
            );

            System.out.println(
                    "             Lista:       min="
                            + minimoLista
                            + " us, max="
                            + maximoLista
                            + " us"
            );

            System.out.println(
                    "             Optimizada:  min="
                            + minimoOptimizada
                            + " us, max="
                            + maximoOptimizada
                            + " us"
            );

            System.out.println();
        }

        System.out.println("==============================================================");
        System.out.println("                    FIN DEL BENCHMARK");
        System.out.println("==============================================================");
    }

    private static long medirLista(int cantidad) {

        Lista<Integer> lista = new Lista<>();

        long inicio = System.nanoTime();

        for (int i = 0; i < cantidad; i++) {
            lista.agregar(i);
        }

        long fin = System.nanoTime();

        return (fin - inicio) / 1_000;
    }

    private static long medirListaOptimizada(int cantidad) {

        ListaOptimizada<Integer> lista =
                new ListaOptimizada<>();

        long inicio = System.nanoTime();

        for (int i = 0; i < cantidad; i++) {
            lista.agregar(i);
        }

        long fin = System.nanoTime();

        return (fin - inicio) / 1_000;
    }
}