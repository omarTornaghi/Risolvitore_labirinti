
/* Semplice classe che implementa una coda di priorità */
/* perchè purtroppo quella che fornisce java fa schifo.*/
/* Tornaghi Omar                                       */
/* otornaghi@studenti.uninsubria.it                    */
/* 23/11/2020                                          */

import java.util.LinkedList;

public class CodaPriorita {
    private LinkedList<DijkstraNode> lista;

    public CodaPriorita() {
        lista = new LinkedList<DijkstraNode>();
    }

    /* Inserimento di un nuovo elemento secondo un parametro */
    public void add(DijkstraNode nuovoItem) {
        if (lista.size() == 0) {
            lista.add(nuovoItem);
            return;
        }
        for (int i = 0; i < lista.size(); i++) {
            DijkstraNode item = lista.get(i);
            if (nuovoItem.compareTo(item) < 0) {
                lista.add(i, nuovoItem);
                return;
            }
        }
        /* Non ho trovato elementi con priorità minore */
        /* dunque aggiungo alla fine */
        lista.add(nuovoItem);
    }

    private int find(NodoLab nodoLab) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).nodo.getChiave() == nodoLab.getChiave())
                return i;
        }
        return -1; /* Elemento non trovato */
    }

    public DijkstraNode getFirst() {
        if (lista.size() > 0)
            return lista.getFirst();
        return null;
    }

    public DijkstraNode remove(NodoLab nodoLab) {
        return lista.remove(find(nodoLab));
    }

    public DijkstraNode removeFirst() {
        return lista.remove(0);
    }

    public boolean exists(NodoLab nodoLab) {
        return find(nodoLab) > -1;
    }

    public void display() {
        for (DijkstraNode item : lista) {
            System.out.println(item);
        }
    }

}
