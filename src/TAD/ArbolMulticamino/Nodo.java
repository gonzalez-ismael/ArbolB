package TAD.ArbolMulticamino;

/**
 * Esta clase representa un Nodo de un Arbol Multicamino de Busqueda.
 *
 * @author GONZALEZ ESPADA, José Ismael
 */
public class Nodo {

    //Atributos privados del NodoB
    private int m;                  //cantidad de hijos = orden
    private Integer[] claves;       //arreglo de claves
    private Nodo[] hijos;          //arreglo de hijos
    private Nodo padre;            //padre del nodo
    private int cantClaves;         //cantidad de claves

    /**
     * Constructor desarrollado para crear nodos con 1 clave.
     *
     * @param clave es la clave a ingresar para este nodo.
     * @param orden es el orden del nodo.
     */
    public Nodo(int orden, int clave) {
        this.m = orden;
        this.claves = new Integer[m + 1];
        this.claves[0] = clave;
        this.hijos = new Nodo[m + 1];
        this.padre = null;
        this.cantClaves = 1;
    }

    /**
     * Constructor desarrollado para crear nodos vácios.
     *
     * @param orden es el orden del nodo.
     */
    public Nodo(int orden) {
        this.m = orden;
        this.claves = new Integer[m + 1];
        this.hijos = new Nodo[m + 1];
        this.padre = null;
        this.cantClaves = 0;
    }

    /**
     * Método para detectar si un nodo es o no Hoja, Si todos sus hijos son "null" es hoja.
     *
     * @return boolean - devuelve "VERDADERO" si es hoja, caso contrario "FALSO".
     */
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

    /**
     * Método que devuelve si un nodo esta lleno o no.
     *
     * @return boolean - devuelve "VERDADERO" si esta lleno, caso contrario "FALSO".
     */
    public boolean estaLleno() {
        return (this.getCantClaves() == this.m - 1);
    }

    /**
     * Método que devuelve si un nodo esta por debajo del minimo de claves o no. El mínimo es de M/2 claves.
     *
     * @return boolean - devuelve "VERDADERO" si esta por debajo del minimo de claves, caso contrario "FALSO".
     */
    public boolean underKeys() {
        return (this.getCantClaves() < (this.m / 2));
    }

    /**
     * Método que devuelve si un nodo no contiene claves.
     *
     * @return boolean - devuelve "VERDADERO" si no contiene claves, caso contrario devuelve "FALSO".
     */
    public boolean estaVacio() {
        return (this.getCantClaves() == 0);
    }

    /**
     * Método para mostrar las claves de un nodo. El nodo empieza en 1 porque se omite la primera posición.
     */
    public void mostrarClaves() {
        int i = 1;
        System.out.print("Claves del nodo: ");
        while (i <= this.getCantClaves()) {
            if (this.getClaveEn(i) != null) {
                System.out.print(this.getClaveEn(i) + " ");
            }
            i++;
        }
        System.out.println("");
    }

    /**
     * Método que retorna la posicion de una clave en un nodo, si no existe retorna -1.
     *
     * @param clave clave buscada en el nodo.
     * @return int - devuele el "indice" de la clave si existe, caso contrario retorna "-1".
     */
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

    public Nodo[] getHijos() {
        return hijos;
    }

    public void setHijos(Nodo[] hijos) {
        this.hijos = hijos;
    }

    public Nodo getHijoEn(int pos) {
        return this.hijos[pos];
    }

    public void setHijoEn(int pos, Nodo nodo) {
        this.hijos[pos] = nodo;
    }

    public Nodo getPadre() {
        return padre;
    }

    public void setPadre(Nodo padre) {
        this.padre = padre;
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
