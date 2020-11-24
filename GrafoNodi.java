
/*
 * Classe per gestire un grafo di nodi 
 * Tornaghi Omar
 * otornaghi@studenti.uninsubria.it
 * 09/10/2020
*/
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class GrafoNodi {
    private ArrayList<NodoLab> nodi;
    private int chiaveAtt;

    public GrafoNodi() {
        nodi = new ArrayList<NodoLab>();
        chiaveAtt = 0;
    }

    public int inserisciNodo(NodoLab nuovoNodo) {
        nuovoNodo.setChiave(chiaveAtt);
        nodi.add(nuovoNodo);
        chiaveAtt++;
        return chiaveAtt;
    }

    public void ottimizzaGrafo() {
        /* Elimino tutti i nodi con solo un collegamento */
        /* ad eccezione del primo e dell'ultimo */
        for (int cont = 0; cont < nodi.size(); cont++) {
            if (cont != 0 && cont != nodi.size() - 1)
                eliminaNodo(nodi.get(cont));
        }
    }

    private void eliminaNodo(NodoLab corrente) {
        if (corrente.getNumVicini() != 1)
            return;
        NodoLab vicino = corrente.getUnicoVicino();
        nodi.get(vicino.getChiave()).eliminaRiferimentoNodo(corrente);
        nodi.get(corrente.getChiave()).eliminaRiferimenti();
        eliminaNodo(vicino);
    }

    public Queue<NodoLab> convertiInCoda() {
        Queue<NodoLab> coda = new LinkedList<NodoLab>();
        for (int i = 0; i < nodi.size(); i++) {
            NodoLab n = nodi.get(i);
            if (n.getNumVicini() > 0)
                coda.add(n);
        }
        return coda;
    }

    public LinkedList<NodoLab> dijkstra() {
        CodaPriorita coda = new CodaPriorita();
        LinkedList<DijkstraNode> lista = new LinkedList<DijkstraNode>();
        /* Inizializzo l'algoritmo con il nodo di partenza */
        DijkstraNode nodoScorrimento = new DijkstraNode(nodi.get(0), 0, null);
        coda.add(nodoScorrimento);
        while (nodoScorrimento != null && !isNodoFinale(nodoScorrimento.nodo)) {
            LinkedList<NodoLab> vicini = nodoScorrimento.nodo.getNodiVicini();
            for (NodoLab vicino : vicini) {
                if (!vicino.isVisitato()) {
                    /* Se il nodo non è ancora stato visitato */
                    Integer distanzaNodoVicino = vicino.getDistanzaNodo(nodoScorrimento.nodo)
                            + nodoScorrimento.distanza;
                    if (!coda.exists(vicino)) {
                        /* Se non esiste l'elemento nella coda, lo aggiungo */
                        coda.add(new DijkstraNode(vicino, distanzaNodoVicino, nodoScorrimento));
                    } else {
                        /* L'elemento esiste già nella coda */
                        DijkstraNode elementoTrovato = coda.remove(vicino);
                        if (elementoTrovato.distanza > distanzaNodoVicino) {
                            /* Se la distanza memorizzata è maggiore rispetto a quella appena calcolata */
                            /* Lo reinserisco utilizzando la distanza minore e quindi cambiando anche */
                            /* il predecessore */
                            coda.add(new DijkstraNode(vicino, distanzaNodoVicino, nodoScorrimento));
                        } else {
                            /* La distanza calcolata è maggiore, quindi lo reinserisco */
                            coda.add(elementoTrovato);
                        }
                    }
                }
            }
            /* Marco il nodo corrente come visitato */
            nodoScorrimento.nodo.setVisitato(true);
            /* Lo inserisco nella lista e lo cancello dalla coda */
            lista.add(coda.removeFirst());
            /* Prendo il primo elemento della coda */
            nodoScorrimento = coda.getFirst();
        } /* Fine while */

        if (nodoScorrimento != null && isNodoFinale(nodoScorrimento.nodo)) {
            /* Percorso trovato */
            lista.add(nodoScorrimento);
        } else /* Percorso non trovato */
            return null;

        /* Converto la lista operativa, in una lista equivalente */
        /* più facile da stampare */
        LinkedList<NodoLab> output = new LinkedList<NodoLab>();
        nodoScorrimento = lista.getLast();
        NodoLab nodoPrecedente = null;
        while (nodoScorrimento != null) {
            /* Serve per rifare i link */
            NodoLab nodoCorrente = nodoScorrimento.nodo;
            /* Inizializzo il nodo da aggiungere */
            NodoLab nodoDaAggiungere = new NodoLab(nodoCorrente.getI(), nodoCorrente.getJ());
            nodoDaAggiungere.setChiave(nodoCorrente.getChiave());
            /* Per ogni nodo nel percorso setto i riferimenti */
            if (nodoPrecedente != null) {
                /* Se esiste un nodo prima di quello corrente */
                nodoDaAggiungere = aggiustaRiferimenti(nodoDaAggiungere, nodoCorrente, nodoPrecedente);
            }
            if (nodoScorrimento.precedente != null && nodoScorrimento.precedente.nodo != null) {
                NodoLab nodoSuccessivo = nodoScorrimento.precedente.nodo;
                nodoDaAggiungere = aggiustaRiferimenti(nodoDaAggiungere, nodoCorrente, nodoSuccessivo);
            }
            nodoPrecedente = nodoCorrente;
            output.addFirst(nodoDaAggiungere);
            nodoScorrimento = nodoScorrimento.precedente;
        }
        return output;
    }

    private NodoLab aggiustaRiferimenti(NodoLab nodoOutput, NodoLab nodoConfrontoCorrente, NodoLab nodoRiferimento) {
        if (nodoRiferimento.getSup() == nodoConfrontoCorrente)
            nodoOutput.setInf(nodoRiferimento);
        if (nodoRiferimento.getInf() == nodoConfrontoCorrente)
            nodoOutput.setSup(nodoRiferimento);
        if (nodoRiferimento.getDes() == nodoConfrontoCorrente)
            nodoOutput.setSin(nodoRiferimento);
        if (nodoRiferimento.getSin() == nodoConfrontoCorrente)
            nodoOutput.setDes(nodoRiferimento);
        return nodoOutput;
    }

    private boolean isNodoFinale(NodoLab n) {
        return n.getChiave() == this.chiaveAtt - 1;
    }
}
