
/*
 * Risolvitore di labirinti V 1.0
 * Tornaghi Omar
 * otornaghi@studenti.uninsubria.it
 * 10/10/2020
*/

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RisLab {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length > 0) {
            trovaPercorso(args[0]);
        } else
            trovaPercorso("Esempi/lab_30x30.txt");
    }

    private static void trovaPercorso(String percorsoFile) throws FileNotFoundException, IOException {
        /* Leggo il labirinto */
        System.out.println("Leggo il labirinto...");
        boolean[][] labirinto = leggiLabirinto(percorsoFile);
        int lunghezza = labirinto[0].length;
        int altezza = labirinto.length;
        System.out.println("Lettura terminata");
        /* Creo il grafo associato */
        System.out.println("Conversione del labirinto in grafo...");
        GrafoNodi grafo = convertiMatrInGrafo(labirinto, lunghezza, altezza);
        System.out.print("Grafo creato vuoi stamparlo?[S/N]: ");
        Scanner scanner = new Scanner(System.in);
        String resp = scanner.nextLine();
        while (!resp.toLowerCase().equals("s") && !resp.toLowerCase().equals("n")) {
            System.out.print("Carattere non consentito\nS-> si\nN-> no\n> ");
            resp = scanner.nextLine();
        }
        scanner.close();
        /* Stampo il grafo */
        if (resp.toLowerCase().equals("s"))
            stampaGrafo(labirinto, grafo, lunghezza, altezza);

        /* Trovo il percorso più breve utilizzando l'algoritmo di dijkstra */
        System.out.println("Calcolando il percorso più breve...");
        LinkedList<NodoLab> percorso = grafo.dijkstra();
        if (percorso == null)
            System.out.println("Impossibile trovare un percorso");
        else {
            System.out.println("Percorso trovato:\n");
            stampaPercorso(labirinto, percorso, lunghezza, altezza);
        }
    }

    private static boolean[][] leggiLabirinto(String percorsoFile) throws IOException {
        /* Ottengo il numero di linee */
        int numeroLinee = 0;
        try (Stream<String> linee = Files.lines(Paths.get(percorsoFile), Charset.defaultCharset())) {
            numeroLinee = (int) linee.count();
        }
        int i = 0;
        boolean[][] labirinto = null;
        /* Leggo riga per riga il file */
        BufferedReader reader = new BufferedReader(new FileReader(percorsoFile));
        String linea = reader.readLine();
        if (linea != null)
            labirinto = new boolean[numeroLinee][linea.length()];
        while (linea != null) {
            for (int j = 0; j < linea.length(); j++) {
                labirinto[i][j] = linea.charAt(j) == '#' ? true : false;
            }
            linea = reader.readLine();
            i++;
        }
        reader.close();
        return labirinto;
    }

    private static GrafoNodi convertiMatrInGrafo(boolean[][] labirinto, int lunghezza, int altezza) {
        GrafoNodi grafo = new GrafoNodi();
        NodoLab[] arrColonne = new NodoLab[lunghezza];
        NodoLab nodoRiga = null;
        NodoLab iniziale = new NodoLab(0, 1); /* Nodo iniziale */
        grafo.inserisciNodo(iniziale); /* Inserisco il nodo iniziale */
        arrColonne[1] = iniziale;
        for (int i = 1; i < altezza - 1; i++) {
            for (int j = 1; j < lunghezza - 1; j++) {
                switch (valutaCaso(labirinto, i, j)) {
                    case 0: /* Muro */
                        nodoRiga = null;
                        arrColonne[j] = null;
                        break;
                    case 4: /* Punto di interesse */
                        NodoLab nodoCorrente = new NodoLab(i, j);
                        /* Collego il nodo agli altri e calcolo la distanza */
                        if (arrColonne[j] != null) { /* Nodo superiore */
                            int distanza = nodoCorrente.getI() - arrColonne[j].getI();
                            arrColonne[j].setInf(nodoCorrente);
                            arrColonne[j].setDistInf(distanza);
                            nodoCorrente.setSup(arrColonne[j]);
                            nodoCorrente.setDistSup(distanza);
                        }
                        if (nodoRiga != null) /* Nodo a sinistra */
                        {
                            int distanza = nodoCorrente.getJ() - nodoRiga.getJ();
                            nodoRiga.setDes(nodoCorrente);
                            nodoRiga.setDistDes(distanza);
                            nodoCorrente.setSin(nodoRiga);
                            nodoCorrente.setDistSin(distanza);
                        }
                        arrColonne[j] = nodoCorrente;
                        nodoRiga = nodoCorrente;
                        /* Inserisco il nodo nel grafo */
                        grafo.inserisciNodo(nodoCorrente);
                        break;
                }
            }
            nodoRiga = null;
        }
        /* Inserisco il nodo finale */
        NodoLab finale = new NodoLab(altezza - 1, lunghezza - 2);
        if (arrColonne[lunghezza - 2] != null) {
            int distanza = finale.getI() - arrColonne[lunghezza - 2].getI();
            arrColonne[lunghezza - 2].setInf(finale);
            arrColonne[lunghezza - 2].setDistInf(distanza);
            finale.setSup(arrColonne[lunghezza - 2]);
            finale.setDistSup(distanza);
        }
        grafo.inserisciNodo(finale);
        grafo.ottimizzaGrafo();
        return grafo;
    }

    private static int valutaCaso(boolean[][] labirinto, int i, int j) {
        if (labirinto[i][j])
            return 0;
        if (checkVicoloCiecoSup(labirinto, i, j) || checkVicoloCiecoInf(labirinto, i, j)
                || checkVicoloCiecoDes(labirinto, i, j) || checkVicoloCiecoSin(labirinto, i, j))
            return 1;
        if (labirinto[i - 1][j] && labirinto[i + 1][j])
            return 2;
        if (labirinto[i][j - 1] && labirinto[i][j + 1])
            return 3;
        return 4;
    }

    private static boolean checkVicoloCiecoSup(boolean[][] labirinto, int i, int j) {
        return labirinto[i - 1][j] && labirinto[i][j - 1] && labirinto[i][j + 1];
    }

    private static boolean checkVicoloCiecoInf(boolean[][] labirinto, int i, int j) {
        return labirinto[i + 1][j] && labirinto[i][j - 1] && labirinto[i][j + 1];
    }

    private static boolean checkVicoloCiecoDes(boolean[][] labirinto, int i, int j) {
        return labirinto[i][j + 1] && labirinto[i - 1][j] && labirinto[i + 1][j];
    }

    private static boolean checkVicoloCiecoSin(boolean[][] labirinto, int i, int j) {
        return labirinto[i][j - 1] && labirinto[i - 1][j] && labirinto[i + 1][j];
    }

    private static void stampaGrafo(boolean[][] labirinto, GrafoNodi grafo, int lunghezza, int altezza) {
        Queue<NodoLab> nodi = grafo.convertiInCoda();
        NodoLab primo = nodi.remove();
        boolean disegnaRiga = false;
        boolean[] disegnaColonna = new boolean[lunghezza];
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < lunghezza; j++) {
                if (labirinto[i][j]) { /* Se la cella è un muro */
                    System.out.print(ANSI_BLACK_BACKGROUND + " " + ANSI_RESET);
                    disegnaRiga = false;
                    continue;
                }
                if (primo.getI() == i && primo.getJ() == j) { /* Se è un nodo */
                    System.out.print(ANSI_RED + ANSI_WHITE_BACKGROUND + primo + ANSI_RESET);
                    disegnaRiga = primo.getDes() != null ? true : false;
                    disegnaColonna[j] = primo.getInf() != null ? true : false;
                    if (!nodi.isEmpty())
                        primo = nodi.remove();
                } else { /* Spazio vuoto quindi o ' ' '-' '|' */
                    if (disegnaRiga)
                        System.out.print(ANSI_RED + ANSI_WHITE_BACKGROUND + "-" + ANSI_RESET);
                    else if (disegnaColonna[j])
                        System.out.print(ANSI_RED + ANSI_WHITE_BACKGROUND + "|" + ANSI_RESET);
                    else
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void stampaPercorso(boolean[][] labirinto, List<NodoLab> percorso, int lunghezza, int altezza) {
        NodoLab primo = null;
        if (percorso.size() > 0) {
            percorso.sort((a, b) -> Integer.compare(a.getChiave(), b.getChiave()));
            primo = percorso.remove(0);
        }
        boolean disegnaRiga = false;
        boolean[] disegnaColonna = new boolean[lunghezza];
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < lunghezza; j++) {
                if (labirinto[i][j]) { /* Se la cella è un muro */
                    System.out.print(ANSI_BLACK_BACKGROUND + " " + ANSI_RESET);
                    disegnaRiga = false;
                    continue;
                }
                if (primo.getI() == i && primo.getJ() == j) { /* Se è un nodo */
                    System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);
                    disegnaRiga = primo.getDes() != null ? true : false;
                    disegnaColonna[j] = primo.getInf() != null ? true : false;
                    if (!percorso.isEmpty())
                        primo = percorso.remove(0);
                } else {
                    if (disegnaRiga || disegnaColonna[j])
                        System.out.print(ANSI_BLUE_BACKGROUND + " " + ANSI_RESET);
                    else
                        System.out.print(ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);
                }
            }
            System.out.println();
        }
    }

}
