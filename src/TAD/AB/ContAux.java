/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TAD.AB;

/**
 *
 * @author Ismael
 */

/*
    Clase auxiliar para poder pasar arreglos de distintos tipos de parametros.
    Esta clase nace como solucion al uso de punteros.
    En java solo se puede retornar un unico valor, entonces hare que se retorne un arreglo con todos los valores que usan los punteros en C++.
 */
public class ContAux {

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
