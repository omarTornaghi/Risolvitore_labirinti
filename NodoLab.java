/*
 * Classe per gestire le informazioni di un nodo
 * Tornaghi Omar
 * otornaghi@studenti.uninsubria.it
 * 09/10/2020
*/

import java.util.LinkedList;

public class NodoLab {
    private int i; /* Numero riga */
    private int j; /* Numero colonna */
    private int chiave; /* id del nodo */
    private NodoLab[] vicini; /* Link dei nodi vicini 0->su 1->giu 2->destra 3->sinistra */
    private Integer[] distanze; /* Distanze dai vicini */
    private boolean visitato; /* flag per vedere se è stato visitato */

    public NodoLab(int i, int j) {
        this.i = i;
        this.j = j;
        vicini = new NodoLab[4];
        distanze = new Integer[4];
        visitato = false;
    }

    /* GETTER E SETTER */

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getChiave() {
        return chiave;
    }

    public void setChiave(int chiave) {
        this.chiave = chiave;
    }

    public NodoLab getSup() {
        return vicini[0];
    }

    public void setSup(NodoLab sup) {
        vicini[0] = sup;
    }

    public NodoLab getInf() {
        return vicini[1];
    }

    public void setInf(NodoLab inf) {
        vicini[1] = inf;
    }

    public NodoLab getDes() {
        return vicini[2];
    }

    public void setDes(NodoLab des) {
        vicini[2] = des;
    }

    public NodoLab getSin() {
        return vicini[3];
    }

    public void setSin(NodoLab sin) {
        vicini[3] = sin;
    }

    public Integer getDistSup() {
        return distanze[0];
    }

    public void setDistSup(int d) {
        distanze[0] = d;
    }

    public Integer getDistInf() {
        return distanze[1];
    }

    public void setDistInf(int d) {
        distanze[1] = d;
    }

    public Integer getDistDes() {
        return distanze[2];
    }

    public void setDistDes(int d) {
        distanze[2] = d;
    }

    public Integer getDistSin() {
        return distanze[3];
    }

    public void setDistSin(int d) {
        distanze[3] = d;
    }

    public boolean isVisitato() {
        return visitato;
    }

    public void setVisitato(boolean visitato) {
        this.visitato = visitato;
    }

    public int getNumVicini() {
        int cont = 0;
        for (int i = 0; i < 4; i++) {
            if (vicini[i] != null)
                cont++;
        }
        return cont;
    }

    public NodoLab getUnicoVicino() {
        if (vicini[0] != null)
            return vicini[0];
        if (vicini[1] != null)
            return vicini[1];
        if (vicini[2] != null)
            return vicini[2];
        return vicini[3];
    }

    public void eliminaRiferimenti() {
        vicini[0] = null;
        vicini[1] = null;
        vicini[2] = null;
        vicini[3] = null;
        for (int i = 0; i < 4; i++) {
            vicini[i] = null;
        }
    }

    public void eliminaRiferimentoNodo(NodoLab nodo) {
        for (int i = 0; i < 4; i++) {
            if (vicini[i] == nodo)
                vicini[i] = null;
        }
    }

    public LinkedList<NodoLab> getNodiVicini() {
        LinkedList<NodoLab> vicini = new LinkedList<NodoLab>();
        for (int i = 0; i < 4; i++) {
            if (this.vicini[i] != null)
                vicini.add(this.vicini[i]);
        }
        return vicini;
    }

    public Integer getDistanzaNodo(NodoLab nL) {
        for (int i = 0; i < 4; i++) {
            if (vicini[i] == nL)
                return distanze[i];
        }
        return null;
    }

    @Override
    public String toString() {
        return "O";
    }

}
