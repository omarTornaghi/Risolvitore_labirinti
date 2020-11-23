/* Classe ausiliaria per algoritmo di dijkstra */
/* Tornaghi Omar */
/* otornaghi@studenti.uninsubria.it*/
/* 23/11/2020 */
public class DijkstraNode implements Comparable<DijkstraNode> {
    public NodoLab nodo;
    public Integer distanza;
    public DijkstraNode precedente;

    public DijkstraNode(NodoLab nodo, Integer distanza, DijkstraNode precedente) {
        this.nodo = nodo;
        this.distanza = distanza;
        this.precedente = precedente;
    }

    public int compareTo(DijkstraNode altroNodo) {
        return this.distanza.compareTo(altroNodo.distanza);
    }

    public String toString() {
        return String.valueOf(nodo.getChiave());
    }
}
