package TAD.AB;

import Excepciones.ClaveDuplicadaExcepcion;
import Excepciones.ClaveInexistenteExcepcion;
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
        try {
            this.setRaiz(insertar(this.getRaiz(), clave));
        } catch (ClaveDuplicadaExcepcion ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Metodo privado para crear una nueva raíz si la propagación, hacia arriba, del proceso de división llega a la actual raíz (el árbol aumenta su altura).
     *
     * @param raiz es la raiz actual del árbol.
     * @param clave es la clave a ingresar en el árbol.
     * @return NodoB - es la nueva raiz.
     */
    private NodoB insertar(NodoB raiz, Integer clave) throws ClaveDuplicadaExcepcion {
        boolean subeArriba;
        int mediana;
        NodoB nodoDerecha = null;
        ContAux arregloAux;
        arregloAux = empujar(raiz, clave, 0, nodoDerecha);
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
            actualizarPadres(raiz);
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
     * @throws ClaveDuplicadaExcepcion - es una excepcion en caso de que se intente ingresar una clave que ya este en el árbol.
     */
    private ContAux empujar(NodoB nodoActual, Integer clave, Integer mediana, NodoB nuevoDerecho) throws ClaveDuplicadaExcepcion {
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
            seEncuentra = buscarClave(nodoActual, clave, auxIndice);
            indiceClave = auxIndice.get();
            if (seEncuentra) {
                throw new ClaveDuplicadaExcepcion("Clave Duplicada");
            }
            // siempre se ejecuta // Recursivividad
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
        NodoB nuevoNodo = new NodoB(this.getM());
        k = pos;
        //posicion de clave mediana
        posMediana = (k <= this.getM() / 2) ? this.getM() / 2 : this.getM() / 2 + 1; //IF SIMPLE
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
     * Este método inserta una clave en un nodo que tiene un número de claves menor que el máximo, es invocado una vez que empujar() ha comprobado que hay espacio para añadir al nodo una nueva clave.
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

    public void eliminar(Integer clave) {
        try {
            this.setRaiz(eliminar(this.getRaiz(), clave));
            actualizarPadres(this.getRaiz());
        } catch (ClaveInexistenteExcepcion ex) {
            System.out.println(ex.toString());
        }
    }

    private NodoB eliminar(NodoB nodo, Integer clave) throws ClaveInexistenteExcepcion {
        if (nodo != null) {
            AtomicInteger indexClave = new AtomicInteger();
            NodoB nodoEliminar = buscarNodo(nodo, clave, indexClave);
            if (nodoEliminar == null) {
                throw new ClaveInexistenteExcepcion("Clave Inexistente");
            }

            if (!nodoEliminar.esHoja()) {   //NODO INTERNO
                Integer claveSustituta = buscarMayorMenor(nodoEliminar, clave, indexClave);
//                Integer claveSustituta = buscarMenorMayor(nodoEliminar,clave,auxIndice);
//                System.out.println("clave: "+claveSustituta);
                nodoEliminar.setClaveEn(indexClave.get(), claveSustituta);
                eliminar(nodoEliminar.getHijoEn(indexClave.get() - 1), claveSustituta);
            } else {                               //NODO HOJA
                if (nodoEliminar.getCantClaves() > nodoEliminar.getM() / 2) { //Nodo con mas de M/2 claves
                    eliminarNodoSupresion(nodoEliminar, clave, indexClave);
                } else { //NODO CON M/2 claves
                    eliminarNodoSupresion(nodoEliminar, clave, indexClave);
                    AtomicInteger indiceHermano = new AtomicInteger();
                    boolean prestamo = buscarHermano(nodoEliminar, indiceHermano);
                    restaurar(nodoEliminar.getPadre(), prestamo, indiceHermano);
                }
            }
        }
        return nodo;
    }

    private void eliminarNodoSupresion(NodoB nodo, Integer clave, AtomicInteger indice) {
        for (int i = indice.get(); i <= nodo.getCantClaves(); i++) {
            nodo.setClaveEn(i, nodo.getClaveEn(i + 1));
            nodo.setHijoEn(i, nodo.getHijoEn(i + 1));
        }
        nodo.setCantClaves(nodo.getCantClaves() - 1);
    }

    private void restaurar(NodoB padre, boolean prestamo, AtomicInteger indiceHermano) {
        if (prestamo) {
            //desdeIzq()
            //desdeDer()
        } else {
            //UNIR
//            merge();
            if (padre.getCantClaves() < padre.getM() / 2) {
                AtomicInteger indiceHermano2 = new AtomicInteger();
                boolean prestamo2 = buscarHermano(padre, indiceHermano2);
                restaurar(padre.getPadre(), prestamo2, indiceHermano2);
            }

        }
    }

    private boolean buscarHermano(NodoB nodo, AtomicInteger index) {
        boolean existeNodo = false;
        NodoB padre = nodo.getPadre();
        for (int i = 0; i < padre.getCantClaves() && existeNodo == false; i++) {
            if (padre.getHijoEn(i).getCantClaves() > padre.getM() / 2) {
                existeNodo = true;
                index.set(i);
            }
        }
        return existeNodo;
    }

    private int buscarMayorMenor(NodoB nodo, Integer clave, AtomicInteger indice) {
        //UNO A LA IZQUIERDA
        NodoB izquierda = nodo.getHijoEn(indice.get() - 1);
        //FULL DERECHA
        while (!izquierda.esHoja()) {
            izquierda = izquierda.getHijoEn(izquierda.getCantClaves());
        }
        return izquierda.getClaveEn(izquierda.getCantClaves());
    }

    private int buscarMenorMayor(NodoB nodo, Integer clave, AtomicInteger indice) {
        //UNO A LA DERECHA
        NodoB derecha = nodo.getHijoEn(indice.get());
        //FULL IZQUIERDA
        while (!derecha.esHoja()) {
            derecha = derecha.getHijoEn(0);
        }
        return derecha.getClaveEn(1);
    }

    //Actualizar el padre de cada hijo del nodo actual
    public void actualizarPadres(NodoB nodo) {
        for (int i = 0; i < nodo.getCantClaves() + 1; i++) {
            NodoB hijo = nodo.getHijoEn(i);
            if (hijo != null) {
                hijo.setPadre(nodo);
                actualizarPadres(hijo); // Llamada recursiva para el siguiente nivel del árbol
            }
        }
    }

    // Java Code
    // Deletes value from the node
    public NodoB del(int val, NodoB root) {
        NodoB temp;
        if (!delhelp(val, root)) {
            System.out.println(
                    String.format("Value %d not found.", val));
        } else {
            if (root.count == 0) {
                temp = root;
                root = root.child[0];
                temp = null;
            }
        }
        return root;
    }

    // GFG
// Java Code
// Helper function for del()
    public boolean delhelp(int val, NodoB root) {
        AtomicInteger i = new AtomicInteger();
        boolean flag;
        if (root == null) {
            return 0;
        } else {
            // Again searches for the node
            flag = searchnode(val, root, i);
            // if flag is true
            if (flag) {
                if (root.getHijoEn(i.get()-1) == null) {
                    clear(root, i);
                } else {
                    copysucc(root, i);
                    // delhelp() is called recursively
                    flag = delhelp(root.value[i], root.child[i]);
                    if (flag == 0) {
                        System.out.println(
                                String.format("Value %d not found.",
                                        root.value[i]));
                    }
                }
            } else {
                // Recursion
                flag = delhelp(val, root.getHijoEn(i.get()));
            }
            if (root.child[i] != null) {
                if (root.child[i].count < MIN) {
                    restore(root, i);
                }
            }
            return flag;
        }
    }

    /**
     * El método devuelve true si encuentra la clave en el árbol - Además, en el argumento k se obtiene la posición que ocupa la clave en el nodo, o bien el hijo por donde continuar el proceso de búsqueda.
     *
     * @param nodoActual es el nodo actual.
     * @param clave es la clave a buscar.
     * @param pos es la posicion de la clave. Se utiliza AtomicInteger para poder modificar el indice.
     * @return boolean - retorna "verdadero" en caso de encontrar la clave, y "falso" en caso contrario.
     */
    private boolean buscarClave(NodoB nodoActual, Integer clave, AtomicInteger pos) {
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

    public NodoB buscarNodo(Integer clave, AtomicInteger pos) {
        return buscarNodo(this.getRaiz(), clave, pos);
    }

    private NodoB buscarNodo(NodoB nodoActual, Integer clave, AtomicInteger pos) {
        if (nodoActual == null) {
            return null;
        } else {
            boolean encontrado = buscarClave(nodoActual, clave, pos);
            if (encontrado) {
                return nodoActual;
            } else {
                return buscarNodo(nodoActual.getHijoEn(pos.get()), clave, pos);
            }
        }
    }

    public boolean searchnode(Integer val, NodoB n, AtomicInteger pos) {
        // if val is less than node.value[1]
        if (val < n.getClaveEn(1)) {
            pos.set(0);
            return false;
        } // if the val is greater
        else {
            pos.set(n.getCantClaves());

            // check in the child array
            // for correct position
            while ((val < n.getClaveEn(pos.get())) && pos.get() > 1) {
                pos.set(pos.get()-1);
            }

            return Objects.equals(val, n.getClaveEn(pos.get()));
        }
    }
}
