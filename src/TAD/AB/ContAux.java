package TAD.AB;

/**
 * Esta clase es un mecanismo auxiliar para poder retornar mas de una variable en los distintos métodos de ArbolB.
 * Esta clase nace como solución al uso de punteros en C++.
 * En java solo se puede retornar un unico valor, entonces hare que se retorne un arreglo con todos los valores que usan los punteros en C++.
 * 
 * @author Ismael
 */
public class ContAux {

    //Variables privadas
    private boolean subeArriba;
    private Integer mediana;
    private NodoB nd;
    private int indice;

    public ContAux(boolean subeArriba, int mediana, NodoB nd) {
        this.subeArriba = subeArriba;
        this.mediana = mediana;
        this.nd = nd;
    }

    public ContAux(Integer mediana, NodoB nd, int indice) {
        this.mediana = mediana;
        this.nd = nd;
        this.indice = indice;
    }

    public ContAux(Integer mediana, NodoB nd) {
        this.mediana = mediana;
        this.nd = nd;
    }

    public ContAux() {
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

    public NodoB getNd() {
        return nd;
    }

    public void setNd(NodoB nd) {
        this.nd = nd;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
}
