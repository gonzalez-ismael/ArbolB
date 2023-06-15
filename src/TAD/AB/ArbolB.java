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
            
            if (nodoEliminar.esHoja()) {
                nodoEliminar = eliminarNodoSupresion(nodoEliminar, indexClave);
            } else {
                Integer claveSustituta = buscarMayorMenor(nodoEliminar, clave, indexClave);
//                Integer claveSustituta = buscarMenorMayor(nodoEliminar,clave,auxIndice);
                nodoEliminar.setClaveEn(indexClave.get(), claveSustituta);
                eliminar(nodoEliminar.getHijoEn(indexClave.get() - 1), claveSustituta);
            }
            
            verificarRestaurar(nodoEliminar);

//            if (!nodoEliminar.esHoja()) {   //NODO INTERNO
//                Integer claveSustituta = buscarMayorMenor(nodoEliminar, clave, indexClave);
////                Integer claveSustituta = buscarMenorMayor(nodoEliminar,clave,auxIndice);
////                System.out.println("clave: "+claveSustituta);
//                nodoEliminar.setClaveEn(indexClave.get(), claveSustituta);
//                eliminar(nodoEliminar.getHijoEn(indexClave.get() - 1), claveSustituta);
//            } else {                               //NODO HOJA
//                if (nodoEliminar.getCantClaves() > nodoEliminar.getM() / 2) { //Nodo con mas de M/2 claves
//                    eliminarNodoSupresion(nodoEliminar, indexClave);
//                } else { //NODO CON M/2 claves
//                    eliminarNodoSupresion(nodoEliminar, indexClave);
//                    AtomicInteger indiceHermano = new AtomicInteger();
//                    boolean prestamo = buscarHermano(nodoEliminar, indiceHermano);
//                    restaurar(nodoEliminar.getPadre(), prestamo, indiceHermano);
//                }
//            }
        }
        return nodo;
    }
    
    private void verificarRestaurar(NodoB nodo) {
        if (nodo != this.getRaiz()) {
            if (nodo.underKeys()) {
                AtomicInteger indiceActual = new AtomicInteger();
                AtomicInteger indiceHermano = new AtomicInteger();
                boolean prestamo = buscarHermano(nodo, indiceActual, indiceHermano);
                if (prestamo == true) {
                    prestarNodos(nodo, indiceActual.get(), indiceHermano.get());
                } else {
                    unirNodos(nodo, indiceActual.get());
                }
            }
        }
    }
    
    private void prestarNodos(NodoB nodoActual, int i, int j) {
        NodoB padre = nodoActual.getPadre();
        NodoB hermano;
        NodoB temp;
        if (i < j) { //desplazamiento de IZQ a DER
            temp = padre.getHijoEn(i);
            hermano = padre.getHijoEn(i + 1);
            while (j > i) {
                //Copiamos el valor del padre y aumentamos 1 en el nodo
                temp.setClaveEn(temp.getCantClaves() + 1, padre.getClaveEn(i + 1));
                temp.setCantClaves(temp.getCantClaves() + 1);
                //Copiamos el valor del hijo derecho al padre
                padre.setClaveEn(i + 1, hermano.getClaveEn(1));
                //Movemos las claves del hermano dejandolo con una clave menos
                for (int k = 1; k <= hermano.getCantClaves(); k++) {
                    hermano.setClaveEn(k, hermano.getClaveEn(k + 1));
                }
                hermano.setCantClaves(hermano.getCantClaves() - 1);
                //Actualizamos los nuevos nodos
                temp = padre.getHijoEn(++i);
                hermano = padre.getHijoEn(i + 1);
            }
        } else { //i > j desplazamiento de DER a IZQ
            hermano = padre.getHijoEn(j);
            temp = padre.getHijoEn(j + 1);
            while (j < i) {
                //Movemos las claves del nodo dejandolo con un espacio
                for (int k = temp.getCantClaves(); k >= 1; k--) {
                    temp.setClaveEn(k + 1, temp.getClaveEn(k));
                }
                //Copiamos la clave del padre y aumentamos su cantidad en 1
                temp.setClaveEn(1, padre.getClaveEn(j + 1));
                temp.setCantClaves(temp.getCantClaves() + 1);
                //Copiamos el valor del hijo izquierdo al padre
                padre.setClaveEn(j + 1, hermano.getClaveEn(hermano.getCantClaves()));
                //Eliminamos una clave en el hermano de forma logica y su cantidad
                hermano.setClaveEn(hermano.getCantClaves(), null);
                hermano.setCantClaves(hermano.getCantClaves() - 1);
                //Actualizamos los nuevos nodos
                hermano = padre.getHijoEn(++j);
                temp = padre.getHijoEn(j + 1);
            }
        }
    }
    
    private void unirNodos(NodoB nodo, int i) {
        NodoB padre = nodo.getPadre();
        NodoB hermano;
        int j; //indice del hermano
        int k; //indice de la clave del padre
        if (i < padre.getCantClaves()) { //El nodo esta a la izquierda respecto su nodo hermano al cual se va a unir
            j = i + 1;
            k = j;
            hermano = padre.getHijoEn(j);
            //copiar clave del padre en el nodo
            nodo.setClaveEn(nodo.getCantClaves() + 1, padre.getClaveEn(k));
            nodo.setCantClaves(nodo.getCantClaves() + 1);
            //copiamos la claves del hermano en el nodo
            for (int j2 = 1; j2 <= hermano.getCantClaves(); j2++) {
                nodo.setClaveEn(nodo.getCantClaves() + 1, hermano.getClaveEn(j2));
                nodo.setCantClaves(nodo.getCantClaves() + 1);
            }
            //despejamos el nodo hermano
            hermano.setPadre(null);
            //hermano.free()//no existe jaja-
            //reordenamos las claves e hijos del padre
            int k2;
            for (k2 = k; k2 < padre.getCantClaves(); k2++) {
                padre.setClaveEn(k2, padre.getClaveEn(k2 + 1));
                padre.setHijoEn(k2, padre.getHijoEn(k2 + 1));
            }
            //liberamos esa clave, el hijo y seteamos su cantidad de claves en una menos
            padre.setClaveEn(k2, null);
            padre.setHijoEn(k2, null);
            padre.setCantClaves(padre.getCantClaves()-1);
        } else { //El nodo esta a la derecha respecto su nodo hermano al cual se va a unir
            j = i - 1;
            k = i;
            hermano = padre.getHijoEn(j);
            //dejamos espacio en el nodo para 1 clave padre y M/2 claves del nodo hermano
            for(int l=1; l<=nodo.getCantClaves(); l++){
                nodo.setClaveEn(l+((nodo.getM()/2)+1), nodo.getClaveEn(l));
            }
            //copiar clave del padre en el nodo
            nodo.setClaveEn(nodo.getCantClaves()+(nodo.getM()/2), padre.getClaveEn(k));
            nodo.setCantClaves(nodo.getCantClaves() + 1);
            //copiamos la claves del hermano en el nodo
            for (int j2 = 1; j2 <= hermano.getCantClaves(); j2++) {
                nodo.setClaveEn(j2, hermano.getClaveEn(j2));
                nodo.setCantClaves(nodo.getCantClaves() + 1);
            }
            //despejamos el nodo hermano
            hermano.setPadre(null);
            //hermano.free()//no existe jaja-
            //reordenamos las claves e hijos del padre
            padre.setHijoEn(k-1, padre.getHijoEn(k));
            //liberamos esa clave, el hijo y seteamos su cantidad de claves en una menos
            padre.setClaveEn(k, null);
            padre.setHijoEn(k, null);
            padre.setCantClaves(padre.getCantClaves()-1);
        }
        nodo = padre;
        verificarRestaurar(nodo); //se ve que el padre tambien este bien
    }
    
    private NodoB eliminarNodoSupresion(NodoB nodo, AtomicInteger indice) {
        for (int i = indice.get(); i <= nodo.getCantClaves(); i++) {
            nodo.setClaveEn(i, nodo.getClaveEn(i + 1));
            nodo.setHijoEn(i, nodo.getHijoEn(i + 1));
        }
        nodo.setCantClaves(nodo.getCantClaves() - 1);
        return nodo;
    }
    
    private boolean buscarHermano(NodoB nodo, AtomicInteger indAct, AtomicInteger indHer) {
        boolean existeNodo = false;
        indHer.set(-1);
        indAct.set(-1);
        NodoB padre = nodo.getPadre();
        for (int i = 0; i <= padre.getCantClaves(); i++) {
            if (padre.getHijoEn(i).getCantClaves() < (padre.getM() / 2)) {
                indAct.set(i);
            }
            if (padre.getHijoEn(i).getCantClaves() > (padre.getM() / 2)) {
                existeNodo = true;
                indHer.set(i);
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
    
}
