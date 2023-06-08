package TAD.AB;

import Excepciones.IlegalClaveDuplicada;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;

/**
 * Esta clase hereda de ArbolM y representa un ArbolB de orden M. Esta clase tiene 3 Métodos públicos: Insertar, Eliminar y Buscar.
 *
 * @author Ismael
 */
public class ArbolB extends ArbolM {

    /**
     * Constructor desarrollado para crear un arbol vacio de orden m. Hace uso del constructor de la clase Heredada ArbolM.
     *
     * @param m es la dimension del arbol, la cantidad de ramas/hijos que tiene.
     */
    public ArbolB(int m) {
        super(m);
    }

    /**
     * Método público para insertar una clave.
     *
     * @param clave es la clave a ingresar en el árbol.
     */
    public void insertar(Integer clave) {
        this.setRaiz(insertar(this.getRaiz(), clave));
    }

    /**
     * Metodo privado para crear una nueva raíz si la propagación, hacia arriba, del proceso de división llega a la actual raíz (el árbol aumenta su altura).
     *
     * @param raiz es la raiz actual del árbol.
     * @param clave es la clave a ingresar en el árbol.
     * @return NodoB - es la nueva raiz.
     */
    private NodoB insertar(NodoB raiz, Integer clave) {
        boolean subeArriba;
        int mediana;
        NodoB nodoDerecha = null;
        ContAux arregloAux = new ContAux();
        try {
            arregloAux = empujar(raiz, clave, 0, nodoDerecha);
        } catch (IlegalClaveDuplicada ex) {
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

    /**
     * Método privado - este lo primero que hace es “bajar” por el camino de búsqueda hasta llegar a un nodo vacío A continuación, se prepara para “subir” (activa el indicador subeArriba) por las nodos del camino y realizar la inserción.
     *
     * @param nodoActual es el nodo actual.
     * @param clave es la clave a ingresar.
     * @param mediana la mediana es la clave que se encuentra en la mitad del nodo (M/2).
     * @param nuevoDerecho es el nuevo hijo derecho del nodo actual.
     * @return ContAux - devuelve un arreglo que contiene un booleano "subeArriba", un int "mediana" y un NodoB "nodoDerecha".
     * @throws IlegalClaveDuplicada - es una excepcion en caso de que se intente ingresar una clave que ya este en el árbol.
     */
    private ContAux empujar(NodoB nodoActual, Integer clave, Integer mediana, NodoB nuevoDerecho) throws IlegalClaveDuplicada {
        boolean subeArriba = false;
        ContAux arregloAux = new ContAux(subeArriba, mediana, nuevoDerecho);
        if (nodoActual == null) {
            // envía hacia arriba la clave y su hijo derecho NULL para que se inserte en el nodo padre
            subeArriba = true;
            mediana = clave;
            nuevoDerecho = null;

            arregloAux.setSubeArriba(subeArriba);
            arregloAux.setMediana(mediana);
            arregloAux.setNd(nuevoDerecho);
        } else {
            int indiceClave;
            boolean seEncuentra;
            AtomicInteger auxIndice = new AtomicInteger();
            seEncuentra = buscarNodo(nodoActual, clave, auxIndice);
            indiceClave = auxIndice.get();
            if (seEncuentra) {
                throw new IlegalClaveDuplicada("Clave Duplicada");
            }
            // siempre se ejecuta
            // Recursivividad
            arregloAux = empujar(nodoActual.getHijoEn(indiceClave), clave, mediana, nuevoDerecho);
            subeArriba = arregloAux.getSubeArriba();
            mediana = arregloAux.getMediana();
            nuevoDerecho = arregloAux.getNd();
            // devuelve control; vuelve por el camino de búsqueda
            ContAux arregloAux2;

            if (subeArriba) {
                if (!nodoActual.estaLleno()) {
                    subeArriba = false;
                    arregloAux.setSubeArriba(subeArriba);
                    meterNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
                } else {
                    arregloAux2 = dividirNodo(nodoActual, mediana, nuevoDerecho, indiceClave);
                    arregloAux.setMediana(arregloAux2.getMediana());
                    arregloAux.setNd(arregloAux2.getNd());
                    subeArriba = true;
                    arregloAux.setSubeArriba(subeArriba);
                }
            }
        }
        return arregloAux;
    }

    /**
     * Este método resuelve el problema de que el nodo donde se debe insertar la clave esté lleno. Virtualmente, el nodo se divide en dos y la clave mediana es enviada hacia arriba, para una re-inserción posterior en un nodo padre o bien en una nueva raíz en el caso de que el árbol deba crecer en altura.
     *
     * @param nodoActual es el nodo actual.
     * @param mediana la mediana es la clave que se encuentra en la mitad del nodo (M/2).
     * @param nuevo es el nuevo nodo.
     * @param pos es el indice de la clave.
     * @return ContAux - es un arreglo que contiene un int "mediana" y un NodoB "nuevo".
     */
    private ContAux dividirNodo(NodoB nodoActual, Integer mediana, NodoB nuevo, int pos) {
        int i, posMediana, k;
        NodoB nuevoNodo;
        k = pos;
        //posicion de clave mediana
        posMediana = (k <= this.getM() / 2) ? this.getM() / 2 : this.getM() / 2 + 1; //IF SIMPLE
        nuevoNodo = new NodoB(this.getM());
        for (i = posMediana + 1; i < this.getM(); i++) {
            // desplazada la mitad derecha a la nueva Página, la clave mediana se queda en Página actual
            nuevoNodo.setClaveEn(i - posMediana, nodoActual.getClaveEn(i));
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
        nodoActual.setHijoEn(nodoActual.getCantClaves(), null);
        nodoActual.setClaveEn(nodoActual.getCantClaves(), null); //se quita la mediana de forma real
        nodoActual.setCantClaves(nodoActual.getCantClaves() - 1); //se quita la mediana de forma logica
        nuevo = nuevoNodo; //devuelve el nuevo nodo
        ContAux b = new ContAux(mediana, nuevo);
        return b;
    }

    /**
     * Este método inserta una clave en un nodo que tiene un número de claves menor que el máximo, 
     * es invocado una vez que empujar() ha comprobado que hay espacio para añadir al nodo una nueva clave.
     * 
     * @param nodoActual es el nodo actual.
     * @param clave es la clave a ingresar.
     * @param hijoDerecho es el hijo derecho del nodo.
     * @param indiceClave es el indice de la clave a ingresar.
     */
    private void meterNodo(NodoB nodoActual, Integer clave, NodoB hijoDerecho, int indiceClave) {
        //Desplza a la derecha los elementos para hacer un hueco
        for (int i = nodoActual.getCantClaves(); i >= indiceClave + 1; i--) {
            nodoActual.setClaveEn(i + 1, nodoActual.getClaveEn(i));
            nodoActual.setHijoEn(i + 1, nodoActual.getHijoEn(i));
        }
        // pone la clave y el hijo derecho en la posicion siguiente (indiceClave+1)
        nodoActual.setClaveEn(indiceClave + 1, clave);
        nodoActual.setHijoEn(indiceClave + 1, hijoDerecho);
        // incrementa el contador de claves almacenadas
        nodoActual.setCantClaves(nodoActual.getCantClaves() + 1);
    }

    /**
     * El método devuelve true si encuentra la clave en el árbol - Además, en el argumento k se obtiene
     * la posición que ocupa la clave en el nodo, o bien el hijo por donde continuar el proceso de búsqueda.
     * 
     * @param nodoActual es el nodo actual.
     * @param clave es la clave a buscar.
     * @param pos es la posicion de la clave. Se utiliza AtomicInteger para poder modificar el indice.
     * @return boolean - retorna "verdadero" en caso de encontrar la clave, y "falso" en caso contrario.
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
