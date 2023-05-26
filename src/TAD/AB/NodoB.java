package TAD.AB;

/**
 *
 * @author Ismael
 */
public class NodoB {

    private int m;            //cantidad de hijos = orden
    private NodoB padre;            //Nodo del padre
    private Integer[] claves;           //arreglo de claves
    private NodoB[] hijos;    //arreglo de hijos
    private int cantClaves;         //cantidad de claves

    /*
        Constructor creado por el desarrollador para arbol vacio
     */
    public NodoB(int orden, int clave) {
        this.m = orden;
        this.padre = null;
        this.claves = new Integer[m];
        this.claves[0] = clave;
        this.hijos = new NodoB[m + 1];
        this.cantClaves = 1;
    }

    /*
        Constructor creado por el desarrollador para nodo vacio con orden
     */
    public NodoB(int orden) {
        this.m = orden;
        this.padre = null;
        this.claves = new Integer[m];
        this.hijos = new NodoB[m + 1];
        this.cantClaves = 0;
    }

    public boolean esHoja() {
        boolean esHoja = true;
        int i = 0;
        while ((i < this.m) && (esHoja == true)) {
            if (this.hijos[i] != null) {
                esHoja = false;
            }
            i++;
        }
        return esHoja;
    }

    public boolean estaLleno() {
        return (this.getCantClaves() == this.m - 1);
    }

    public boolean esVacio() {
        return this == null;
    }

    public void mostrarClaves() {
        int i = 0;
        System.out.print("Claves del nodo: ");
        while (i < this.getCantClaves()) {
            if (this.getClaveEn(i) != null) {
                System.out.print(this.getClaveEn(i) + " ");
            }
            i++;
        }
        System.out.println("");
    }

    /*    retorna la posicion de la clave buscada, si no existe retorna -1    */
    public int buscarClave(int clave) {
        int i = 0;
        int pos = -1;
        while ((i < this.getCantClaves()) && (pos == -1)) {
            if (this.getClaveEn(i) == clave) {
                pos = i;
            }
            i++;
        }
        return pos;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public NodoB getPadre() {
        return padre;
    }

    public void setPadre(NodoB padre) {
        this.padre = padre;
    }

    public Integer[] getClaves() {
        return claves;
    }

    public void setClaves(Integer[] claves) {
        this.claves = claves;
    }

    public Integer getClaveEn(int pos) {
        return this.claves[pos];
    }

    public void setClaveEn(int pos, Integer clave) {
        this.claves[pos] = clave;
    }

    public NodoB[] getHijos() {
        return hijos;
    }

    public void setHijos(NodoB[] hijos) {
        this.hijos = hijos;
    }

    public NodoB getHijoEn(int pos) {
        return this.hijos[pos];
    }

    public void setHijoEn(int pos, NodoB nodo) {
        this.hijos[pos] = nodo;
    }

    public int getCantClaves() {
        return cantClaves;
    }

    public void setCantClaves(int cantClaves) {
        this.cantClaves = cantClaves;
    }

    public int getCantHijos() {
        return hijos.length;
    }
}
