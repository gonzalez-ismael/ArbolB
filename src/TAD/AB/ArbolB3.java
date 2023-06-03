/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TAD.AB;

import Excepciones.IlegalClaveDuplicated;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Ismael
 */
public class ArbolB3 extends ArbolM {

    public ArbolB3(int m) {
        super(m);
    }

    /*
        Metodo publico para insertar una clave
     */
    public void insertar(Integer clave) {
        this.setRaiz(insertar(this.getRaiz(), clave));
    }

    /*
        Metodo privado para crear una nueva raíz si la propagación, hacia arriba, 
        del proceso de división llega al actual raíz (el árbol aumenta su altura).
     */
    private NodoB insertar(NodoB raiz, Integer clave) {
        boolean subeArriba;
        int mediana;
        NodoB nodoDerecha = null;
        ContAux arregloAux = new ContAux();
        try {
            arregloAux = empujar(raiz, clave, 0, nodoDerecha);
        } catch (IlegalClaveDuplicated ex) {
            System.out.println(ex.toString());
        }
        //Trabajo de punteros implicito en arregloAux.
        subeArriba = arregloAux.getSubeArriba();
        mediana = arregloAux.getMediana();
        nodoDerecha = arregloAux.getNd();

        // El árbol crece en altura por la raíz.
        // sube una nueva clave mediana y un nuevo hijo derecho nd
        // en la implementación se mantiene que las claves que son
        // menores que mediana se encuentran en raiz y las mayores en nd
        if (subeArriba) {
            NodoB nuevaRaiz = new NodoB(this.getM());    //Nuevo Nodo
            nuevaRaiz.setCantClaves(1);                  //Tiene una sola clave
            nuevaRaiz.setClaveEn(1, mediana);
            nuevaRaiz.setHijoEn(0, raiz);
            nuevaRaiz.setHijoEn(1, nodoDerecha);
            raiz = nuevaRaiz;
        }
        return raiz;
    }

    /*
        Este lo primero que hace es “bajar” por el camino de búsqueda hasta llegar a un nodo vacío.
        A continuación, se prepara para “subir” (activa el indicador subeArriba) por las nodos del camino y realizar la inserción.
     */
    private ContAux empujar(NodoB nodoActual, Integer clave, Integer mediana, NodoB nuevoDerecho) throws IlegalClaveDuplicated {
        boolean subeArriba = false;
        ContAux arregloAux = new ContAux(subeArriba, mediana, nuevoDerecho);
        if (nodoActual == null) {
            // envía hacia arriba la clave y su hijo derecho NULL para que se inserte en el nodo padre
//            subeArriba = true;
            mediana = clave;
            nuevoDerecho = null;

            arregloAux.setSubeArriba(true);
            arregloAux.setMediana(mediana);
            arregloAux.setNd(nuevoDerecho);
        } else {
            int indiceClave;
            boolean seEncuentra;
            AtomicInteger auxIndice = new AtomicInteger();
            seEncuentra = buscarNodo(nodoActual, clave, auxIndice);
            indiceClave = auxIndice.get();
            if (seEncuentra) {
                throw new IlegalClaveDuplicated("Clave Duplicada");
            }
            // siempre se ejecuta
            arregloAux = empujar(nodoActual.getHijoEn(indiceClave), clave, mediana, nuevoDerecho);
//            subeArriba = arregloAux.getSubeArriba();
            mediana = arregloAux.getMediana();
            nuevoDerecho = arregloAux.getNd();

            // devuelve control; vuelve por el camino de búsqueda
            ContAux arregloAux2;
            
            if (!nodoActual.estaLleno()) { //cabe en el nodo, se inserta la mediana y su hijo derecho
                subeArriba = false;
                arregloAux.setSubeArriba(subeArriba);
                meterNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
            } else { //hay que dividir el nodo
                if (nodoActual.estaLleno()) {
                    arregloAux2 = dividirNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
                    arregloAux.setMediana(arregloAux2.getMediana());
                    arregloAux.setNd(arregloAux2.getNd());
                }
            }

//            if (subeArriba) {
//                if (nodoActual.estaLleno()) {    //hay que dividir el nodo
//                    arregloAux2 = dividirNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
//                    arregloAux.setMediana(arregloAux2.getMediana());
//                    arregloAux.setNd(arregloAux2.getNd());
//                }
//            } else {    //cabe en el nodo, se inserta la mediana y su hijo derecho
//                subeArriba = false;
//                arregloAux.setSubeArriba(subeArriba);
//                meterNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
//            }
        }
        return arregloAux;
    }

    /*
        Este método resuelve el problema de que el nodo donde se debe insertar la clave esté lleno.
        Virtualmente, el nodo se divide en dos y la clave mediana es enviada hacia arriba, para una
        re-inserción posterior en un nodo padre o bien en una nueva raíz en el caso de que el árbol
        deba crecer en altura.
     */
    private ContAux dividirNodo(NodoB nodoActual, Integer mediana, NodoB nuevo, int pos) {
        if(mediana==5){
            System.out.println("AHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        }
        int i, posMediana, k;
        NodoB nuevoNodo;
        k = pos;
        //posicion de clave mediana
        posMediana = (k <= this.getM() / 2) ? this.getM() / 2 : this.getM() / 2 + 1; //IF SIMPLE
        nuevoNodo = new NodoB(this.getM());
        for (i = posMediana + 1; i < this.getM(); i++) {
            // desplazada la mitad derecha a la nueva Página, la clave mediana se queda en Página actual
            nuevoNodo.setClaveEn(i - posMediana, nodoActual.getClaveEn(i)); //OJO ACA ----------------------------------------------
//            nuevoNodo.setClaveEn(i - posMediana - 1, nodoActual.getClaveEn(i - 1)); //OJO ACA ----------------------------------------------
            nuevoNodo.setHijoEn(i - posMediana, nodoActual.getHijoEn(i));
        }
        nuevoNodo.setCantClaves((this.getM() - 1) - posMediana); //Claves del nuevo nodo
        nodoActual.setCantClaves(posMediana); //Claves en nodo original

        //Inserta la clave e hijos en el nodo que corresponde
        if (k <= this.getM() / 2) {
            meterNodo(nodoActual, mediana, nuevo, pos); //En el nodo actual
        } else {
            pos = k - posMediana;
            meterNodo(nuevoNodo, mediana, nuevo, pos); //En el nuevo nodo
        }

        //Extrae clave mediana del nodo origen
        mediana = nodoActual.getClaveEn(nodoActual.getCantClaves());
        //El primer hijo de la izq del nuevo nodo, es el hijo de la mediana del nodoActual
        nuevoNodo.setHijoEn(0, nodoActual.getHijoEn(nodoActual.getCantClaves()));
        nodoActual.setClaveEn(nodoActual.getCantClaves(), null); //se quita la mediana de forma real
        nodoActual.setCantClaves(nodoActual.getCantClaves() - 1); //se quita la mediana de forma logica
        nuevo = nuevoNodo; //devuelve el nuevo nodo
        ContAux b = new ContAux(mediana, nuevo);
        return b;
    }

    /*
        Este método inserta una clave en un nodo que tiene un número
        de claves menor que el máximo. El método es invocado una vez que empujar() ha comprobado
        que hay hueco para añadir al nodo una nueva clave
     */
    private void meterNodo(NodoB nodoActual, Integer clave, NodoB hijoDerecho, int indiceClave) {
        //Desplza a la derecha los elementos para hacer un hueco
        for (int i = nodoActual.getCantClaves(); i >= indiceClave + 1; i--) {
            nodoActual.setClaveEn(i + 1, nodoActual.getClaveEn(i)); //OJO ACA ----------------------------------------------
//            nodoActual.setClaveEn(i, nodoActual.getClaveEn(i--)); //OJO ACA ----------------------------------------------
            nodoActual.setHijoEn(i + 1, nodoActual.getHijoEn(i));
        }
        // pone la clave y el hijo derecho en la posicion siguiente (indiceClave+1)
        nodoActual.setClaveEn(indiceClave + 1, clave);
        nodoActual.setHijoEn(indiceClave + 1, hijoDerecho);
        // incrementa el contador de claves almacenadas
        nodoActual.setCantClaves(nodoActual.getCantClaves() + 1);
    }

    /*
        El método devuelve true si encuentra la clave en el árbol. Además, en el argumento k se obtiene
        la posición que ocupa la clave en el nodo, o bien el hijo por donde continuar el proceso de búsqueda
     */
    private boolean buscarNodo(NodoB nodoActual, Integer clave, AtomicInteger pos) {
        int index;
        boolean encontrado;
        if (clave < nodoActual.getClaveEn(1)) {
            encontrado = false;
            index = 0;
        } else { //orden descendente
            index = nodoActual.getCantClaves();
            while (clave < nodoActual.getClaveEn(index) && (index > 1)) {
                index--;
            }
            encontrado = (Objects.equals(clave, nodoActual.getClaveEn(index)));
        }
        pos.set(index);
        return encontrado;
    }

    public NodoB buscar(Integer clave, AtomicInteger pos) {
        return buscar(this.getRaiz(), clave, pos);
    }

    private NodoB buscar(NodoB nodoActual, Integer clave, AtomicInteger pos) {
        if (nodoActual == null) {
            return null;
        } else {
            boolean encontrado = buscarNodo(nodoActual, clave, pos);
            if (encontrado) {
                return nodoActual;
            } else {
                return buscar(nodoActual.getHijoEn(pos.get()), clave, pos);
            }
        }
    }

}
