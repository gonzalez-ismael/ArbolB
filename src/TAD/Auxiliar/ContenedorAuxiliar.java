package TAD.Auxiliar;

import TAD.ArbolMulticamino.Nodo;

/**
 * Esta clase es un mecanismo auxiliar para poder retornar mas de una variable en los distintos métodos de ArbolB.
 * Esta clase nace como solución al uso de punteros en C++. Dado que el uso de multiples variables que apuntan a otras permiten que "retornen" mas de 1 solo valor.
 * En java solo se puede retornar un unico valor, entonces hare que se retorne un arreglo con todos los valores que usan los punteros en C++.
 * 
 * @author GONZALEZ ESPADA, José Ismael
 */
public class ContenedorAuxiliar {

    //Variables privadas
    private boolean subeArriba;
    private Integer mediana;
    private Nodo nd;
    private int indice;

    /**
     * Contenedor Auxiliar para retornar un booleano, una nueva mediana y un nuevo nodo derecho "nd".
     * 
     * @param subeArriba es indicador para saber si el nodo crece hacia arriba.
     * @param mediana es la clave mediana de un nodo.
     * @param nd es el nodo derecho.
     */
    public ContenedorAuxiliar(boolean subeArriba, int mediana, Nodo nd) {
        this.subeArriba = subeArriba;
        this.mediana = mediana;
        this.nd = nd;
    }

    /**
     * Contedor Auxiliar para retornar una nueva mediana, un nuevo nodo derecho "nd" y un indice.
     * 
     * @param mediana es la clave mediana de un nodo.
     * @param nd es el nodo derecho.
     * @param indice es la ubicacion de una clave en un nodo.
     */
    public ContenedorAuxiliar(Integer mediana, Nodo nd, int indice) {
        this.mediana = mediana;
        this.nd = nd;
        this.indice = indice;
    }

    /**
     * Contendor Auxiliar para retornar una nueva mediana y un nuevo nodo derecho.
     * 
     * @param mediana es la clave mediana de un nodo.
     * @param nd es el nodo derecho.
     */
    public ContenedorAuxiliar(Integer mediana, Nodo nd) {
        this.mediana = mediana;
        this.nd = nd;
    }

    public boolean getSubeArriba() {
        return subeArriba;
    }

    public void setSubeArriba(boolean subeArriba) {
        this.subeArriba = subeArriba;
    }

    public Integer getMediana() {
        return mediana;
    }

    public void setMediana(Integer mediana) {
        this.mediana = mediana;
    }

    public Nodo getNd() {
        return nd;
    }

    public void setNd(Nodo nd) {
        this.nd = nd;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
}
