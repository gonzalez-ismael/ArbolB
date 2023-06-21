package TAD.ArbolMulticamino.ArbolB;

import TAD.ArbolMulticamino.ArbolM;
import TAD.ArbolMulticamino.Nodo;
import TAD.Auxiliar.ContenedorAuxiliar;
import TAD.Excepciones.ClaveDuplicadaExcepcion;
import TAD.Excepciones.ClaveInexistenteExcepcion;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Objects;

/**
 * Esta clase hereda de ArbolM y representa un ArbolB de orden M. Esta clase tiene 3 Métodos públicos: Insertar, Eliminar y Buscar.
 *
 * @author GONZALEZ ESPADA, José Ismael
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
     * @param raiz es la clave a ingresar en el árbol.
     * @param clave es la clave a ingresar en el árbol.
     * @return NodoB - es la nueva raiz.
     * @throws ClaveDuplicadaExcepcion - es una excepcion en caso de que se intente ingresar una clave que ya exista en el árbol.
     */
    private Nodo insertar(Nodo raiz, Integer clave) throws ClaveDuplicadaExcepcion {
        boolean subeArriba;
        int mediana;
        Nodo nodoDerecha = null;
        ContenedorAuxiliar arregloAux;
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
            Nodo nuevaRaiz = new Nodo(this.getM());    //Nuevo Nodo
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
     * @throws ClaveDuplicadaExcepcion - es una excepcion en caso de que se intente ingresar una clave que ya exista en el árbol.
     */
    private ContenedorAuxiliar empujar(Nodo nodoActual, Integer clave, Integer mediana, Nodo nuevoDerecho) throws ClaveDuplicadaExcepcion {
        boolean subeArriba = false;
        ContenedorAuxiliar arregloAux = new ContenedorAuxiliar(subeArriba, mediana, nuevoDerecho);
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
            ContenedorAuxiliar arregloAux2;

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
    private ContenedorAuxiliar dividirNodo(Nodo nodoActual, Integer mediana, Nodo nuevo, int pos) {
        int i, posMediana, k;
        Nodo nuevoNodo = new Nodo(this.getM());
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
        ContenedorAuxiliar b = new ContenedorAuxiliar(mediana, nuevo);
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
    private void meterNodo(Nodo nodoActual, Integer clave, Nodo hijoDerecho, int indiceClave) {
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
     * Método público para eliminar una clave.
     *
     * @param clave es la clave a eliminar en el árbol.
     */
    public void eliminar(Integer clave) {
        try {
            this.setRaiz(eliminar(this.getRaiz(), clave));
            actualizarPadres(this.getRaiz());
        } catch (ClaveInexistenteExcepcion ex) {
            System.out.println(ex.toString());
        }
    }

    /**
     * Método privado para analizar el caso de eliminación y ejecutar el procedimiento correcto. Primero busca el nodo de la clave a eliminar. En caso de que no exista, se llama a la excepcion: ClaveInexistenteExcepcion. En caso de que exista, se analiza si es un nodo hoja o un nodo interno. Si es hoja se elimina directamente la clave. En caso de ser un nodo interno, se reemplazar por el Maximo menor, o el Minimo mayor, y se procede a eliminar la clave sustituta del nodo hoja al cual pertenece. Por ultimo se verifica si el nodo del cual se elimino la clave sigue contiendo la cantindad minima de claves. Sino se reestructura el árbol.
     *
     * @param nodo es el nodo donde se busca la clave y se analiza el caso de eliminación.
     * @param clave es la clave a eliminar.
     * @return NodoB - es el nodo que contiene los cambios de la eliminación.
     * @throws ClaveInexistenteExcepcion - es una excepcion en caso de que se intente eliminar una clave que no exista en el árbol.
     */
    private Nodo eliminar(Nodo nodo, Integer clave) throws ClaveInexistenteExcepcion {
        if (nodo != null) {
            AtomicInteger indexClave = new AtomicInteger();
            Nodo nodoEliminar = buscarNodo(nodo, clave, indexClave);
            if (nodoEliminar == null) {
                throw new ClaveInexistenteExcepcion("Clave Inexistente");
            }
            if (nodoEliminar.esHoja()) {
                nodoEliminar = eliminarClaveNodoHoja(nodoEliminar, indexClave);
            } else {
                Integer claveSustituta = buscarMayorMenores(nodoEliminar, indexClave);
//                Integer claveSustituta = buscarMenorMayores(nodoEliminar, indexClave);
                nodoEliminar.setClaveEn(indexClave.get(), claveSustituta);
                eliminar(nodoEliminar.getHijoEn(indexClave.get() - 1), claveSustituta);
            }
            nodo = verificarRestaurar(nodoEliminar);
        }
        return nodo;
    }

    /**
     * Este método verifica la cantidad de claves en el nodo para garantizar la consistencia del árbol B. Si se trata de la raiz, se verifica que no este vacia, porque de estarlo significa que se ha producido una union que llevo a que este nodo preste su unica clave quedando vacio. Y reemplazando este nodo raiz por su hijo izquierdo que sera la nueva raiz. Si es un nodo distinto de la raiz, se verifica si contine el mínimo de claves necesarias con underKeys(). Si necesita más claves, se analiza se existe algun hermano capaz de prestarle una y realizar un "Prestamo", caso contrario se realizara una "Union" entre su hermano proximo y una clave del padre.
     *
     * @param nodo es el nodo a analizar y restaurar en caso de ser necesario.
     * @return NodoB - es la nueva raiz con los cambios realizados (solo se modifica si se produce una "Union" y el nodo padre tenia una unica clave para prestar).
     */
    private Nodo verificarRestaurar(Nodo nodo) {
        if (nodo == this.getRaiz()) {
            if (nodo.estaVacio()) {
                return nodo.getHijoEn(0);
            }
        } else {
            if (nodo.underKeys()) {
                AtomicInteger indiceActual = new AtomicInteger();
                AtomicInteger indiceHermano = new AtomicInteger();
                boolean prestamo = buscarHermano(nodo, indiceActual, indiceHermano);
                if (prestamo == true) {
                    prestarNodos(nodo, indiceActual.get(), indiceHermano.get());
                } else {
                    return unirNodos(nodo, indiceActual.get());
                }
            }
        }
        return this.getRaiz();
    }

    /**
     * Este método transfiere una clave desde el nodo hermano en la posicion j hasta el nodo actual en la posicion i. Se analiza el valor de los indices para saber si el desplazamiento es de "Derecha a Izquierda" o de "Izquierda a Derecha". Si el desplazamiento es de "Derecha a Izquierda" se copia el valor del padre, luego se copia la clave del hijo derecho al padre, se mueve el primer hijo del hermano hacia el ultimo lugar del nodo, se reacomodan las claves y los hijos en el nodo derecho y por último se procede a repetir el proceso hasta llegar al nodo que va a prestar la clave (en caso de que los hermanos sean continuos). Si el desplazamiento es de "Izquierda a Derecha" movemos las claves y los hijos del nodo dejandolo con un espacio para otra clave y otro hijo al inicio de ambos arreglos, copiamos la clave del padre al nodo, copiamos la clave del nodo izquierdo al padre, movemos el ultimo hijo del nodo izquierdo hacia el primer lugar del nodo derecho, eliminamos las claves y los hijos desplazados, actualizamos los nodos y por último se procede a repetir el proceso hasta llegar al nodo que va a prestar la clave (en caso de que los hermanos sean continuos).
     *
     * @param nodoActual es el nodo actual, el cual tiene menos del mínimo de claves permitido.
     * @param i es el indice del nodo actual en el arreglo de hijos del nodo padre.
     * @param j es el indice del nodo hermano, el cual le va a prestar una clave al nodo actual, en el arreglo de hijos del nodo padre.
     */
    private void prestarNodos(Nodo nodoActual, int i, int j) {
        Nodo padre = nodoActual.getPadre();
        Nodo hermano;
        Nodo temp;
        if (i < j) { //desplazamiento de DER A IZQ
            temp = padre.getHijoEn(i);
            hermano = padre.getHijoEn(i + 1);
            while (i < j) {
                //Copiamos el valor del padre y aumentamos en +1 en el nodo actual
                temp.setClaveEn(temp.getCantClaves() + 1, padre.getClaveEn(i + 1));
                temp.setCantClaves(temp.getCantClaves() + 1);
                //Copiamos el valor de la clave del hijo derecho al padre
                padre.setClaveEn(i + 1, hermano.getClaveEn(1));
                //Movemos el primer hijo del hermano hacia el ultimo lugar del nodo temporal
                temp.setHijoEn(temp.getCantClaves(), hermano.getHijoEn(0));
                //Movemos las claves del hermano dejandolo con una clave menos
                int k;
                for (k = 1; k <= hermano.getCantClaves(); k++) {
                    hermano.setClaveEn(k, hermano.getClaveEn(k + 1));
                    hermano.setHijoEn(k - 1, hermano.getHijoEn(k));
                }
                hermano.setHijoEn(k - 1, null);
                hermano.setCantClaves(hermano.getCantClaves() - 1);
                //Actualizamos los nuevos nodos
                temp = padre.getHijoEn(++i);
                hermano = padre.getHijoEn(i + 1);
            }
        } else { //i > j desplazamiento de IZQ a DER
            hermano = padre.getHijoEn(j);
            temp = padre.getHijoEn(j + 1);
            while (j < i) {
                //Movemos las claves y los hijos del nodo dejandolo con un espacio
                for (int k = temp.getCantClaves(); k >= 1; k--) {
                    temp.setClaveEn(k + 1, temp.getClaveEn(k));
                    temp.setHijoEn(k, temp.getHijoEn(k - 1));
                }
                //Copiamos la clave del padre y aumentamos su cantidad en 1
                temp.setClaveEn(1, padre.getClaveEn(j + 1));
                temp.setCantClaves(temp.getCantClaves() + 1);
                //Copiamos el valor del hijo izquierdo al padre
                padre.setClaveEn(j + 1, hermano.getClaveEn(hermano.getCantClaves()));
                //Movemos el ultimo hijo del hermano hacia el primer lugar del nodo temporal
                temp.setHijoEn(0, hermano.getHijoEn(hermano.getCantClaves()));
                //Eliminamos una clave y un hijo en el hermano de forma logica y su cantidad
                hermano.setClaveEn(hermano.getCantClaves(), null);
                hermano.setHijoEn(hermano.getCantClaves(), null);
                hermano.setCantClaves(hermano.getCantClaves() - 1);
                //Actualizamos los nuevos nodos
                hermano = padre.getHijoEn(++j);
                temp = padre.getHijoEn(j + 1);
            }
        }
    }

    /**
     * Este método une un nodo con una clave del nodo padre y el nodo de su hermano mas proximo. En primer lugar se analiza si el indice del nodo es menor al ultimo nodo (para saber de que lado estara el hermano al cual se va a unir).
     *
     * @param nodo es el nodo que contiene menos del mínimo de claves permitido.
     * @param i es el indice del nodo en el arreglo de claves del padre.
     * @return NodoB - es la posible nueva raiz en caso de una reestructuracion que alcanzo la cima del árbol.
     */
    private Nodo unirNodos(Nodo nodo, int i) {
        Nodo padre = nodo.getPadre();
        Nodo hermano;
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
                nodo.setHijoEn(nodo.getCantClaves(), hermano.getHijoEn(j2 - 1));
                nodo.setCantClaves(nodo.getCantClaves() + 1);
            }
            nodo.setHijoEn(nodo.getCantClaves(), hermano.getHijoEn(hermano.getCantClaves()));
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
            padre.setCantClaves(padre.getCantClaves() - 1);
        } else { //El nodo esta a la derecha respecto su nodo hermano al cual se va a unir
            j = i - 1;
            k = i;
            hermano = padre.getHijoEn(j);
            //dejamos espacio en el nodo para 1 clave padre y M/2 claves del nodo hermano
            int l;
            for (l = 1; l <= nodo.getCantClaves(); l++) {
                nodo.setClaveEn(l + ((nodo.getM() / 2) + 1), nodo.getClaveEn(l));
                nodo.setHijoEn(l + ((nodo.getM() / 2) + 1), nodo.getHijoEn(l - 1));
            }
            nodo.setHijoEn(l + ((nodo.getM() / 2)), nodo.getHijoEn(l - 1));
            //copiar clave del padre en el nodo
            nodo.setClaveEn(l + (nodo.getM() / 2), padre.getClaveEn(k));
            nodo.setCantClaves(nodo.getCantClaves() + 1);
            //copiamos la claves del hermano en el nodo
            for (int j2 = 1; j2 <= hermano.getCantClaves(); j2++) {
                nodo.setClaveEn(j2, hermano.getClaveEn(j2));
                nodo.setHijoEn(j2 - 1, hermano.getHijoEn(j2 - 1));
                nodo.setCantClaves(nodo.getCantClaves() + 1);
            }
            nodo.setHijoEn(hermano.getCantClaves(), hermano.getHijoEn(hermano.getCantClaves()));
            //despejamos el nodo hermano
            hermano.setPadre(null);
            //hermano.free()//no existe jaja-
            //reordenamos las claves e hijos del padre
            padre.setHijoEn(k - 1, padre.getHijoEn(k));
            //liberamos esa clave, el hijo y seteamos su cantidad de claves en una menos
            padre.setClaveEn(k, null);
            padre.setHijoEn(k, null);
            padre.setCantClaves(padre.getCantClaves() - 1);
        }
        nodo = padre;
        Nodo raiz = verificarRestaurar(nodo); //se ve que el padre tambien este bien
        return raiz;
    }

    /**
     * Este método es el que efectua la eliminacion de la clave en los nodos hojas de un árbol.
     *
     * @param nodo es el nodo del cual se va a eliminar la clave.
     * @param indice es el indice de la clave la cual se va a eliminar.
     * @return NodoB - es el nodo sin la clave ingresada.
     */
    private Nodo eliminarClaveNodoHoja(Nodo nodo, AtomicInteger indice) {
        for (int i = indice.get(); i <= nodo.getCantClaves(); i++) {
            nodo.setClaveEn(i, nodo.getClaveEn(i + 1));
            nodo.setHijoEn(i, nodo.getHijoEn(i + 1));
        }
        nodo.setCantClaves(nodo.getCantClaves() - 1);
        return nodo;
    }

    /**
     * Este método se utiliza para saber si es posible realizar el "Prestamo" de una clave de un nodo hermano, se encarga de buscar entre todos los hermanos alguno que contenga más del minimo de claves permitidas.
     *
     * @param nodo es el nodo que contiene menos del mínimo de claves permitido.
     * @param indAct retorna el indice del nodo actual en el arreglo de hijos del padre.
     * @param indHer retorna el indice del posible nodo hermano en el arreglo de hijos del padre. Sino retorna "-1".
     * @return boolean - devuelve "VERDADERO" si existe al menos 1 hermano con más del minimo de claves permitido, caso contrario retorna "FALSO".
     */
    private boolean buscarHermano(Nodo nodo, AtomicInteger indAct, AtomicInteger indHer) {
        boolean existeNodo = false;
        indHer.set(-1);
        indAct.set(-1);
        Nodo padre = nodo.getPadre();
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

    /**
     * Este método busca la clave mayor entre todas las claves menores. Tambien se puede interpretar como el valor máximo del subarbol izquierdo.
     *
     * @param nodo es el nodo desde el cual se empieza la busqueda.
     * @param indice es la posicion de la clave en el nodo.
     * @return int - retorna el valor de la clave encontrada.
     */
    private int buscarMayorMenores(Nodo nodo, AtomicInteger indice) {
        //UNO A LA IZQUIERDA
        Nodo izquierda = nodo.getHijoEn(indice.get() - 1);
        //FULL DERECHA
        while (!izquierda.esHoja()) {
            izquierda = izquierda.getHijoEn(izquierda.getCantClaves());
        }
        return izquierda.getClaveEn(izquierda.getCantClaves());
    }

    /**
     * Este método busca la clave menor entre todas las claves mayores. Tambien se puede interpretar como el valor mínimo del subarbol derecho.
     *
     * @param nodo es el nodo desde el cual se empieza la busqueda.
     * @param indice es la posicion de la clave en el nodo.
     * @return int - retorna el valor de la clave encontrada.
     */
    private int buscarMenorMayores(Nodo nodo, AtomicInteger indice) {
        //UNO A LA DERECHA
        Nodo derecha = nodo.getHijoEn(indice.get());
        //FULL IZQUIERDA
        while (!derecha.esHoja()) {
            derecha = derecha.getHijoEn(0);
        }
        return derecha.getClaveEn(1);
    }

    /**
     * Este método se encarga de actualizar todos los padres del árbol.
     *
     * @param nodo es el nodo desde el cual se van a actualiar los padres.
     */
    private void actualizarPadres(Nodo nodo) {
        if (nodo != null) {
            for (int i = 0; i < nodo.getCantClaves() + 1; i++) {
                Nodo hijo = nodo.getHijoEn(i);
                if (hijo != null) {
                    hijo.setPadre(nodo);
                    actualizarPadres(hijo); // Llamada recursiva para el siguiente nivel del árbol
                }
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
    private boolean buscarClave(Nodo nodoActual, Integer clave, AtomicInteger pos) {
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

    /**
     * Este método público se encarga de llamar el metodo recursivo buscar nodo, y retorna el nodo de una clave buscada.
     *
     * @param clave es la clave buscada en los nodos.
     * @param pos es la posicion de esa clave.
     * @return NodoB - es el nodo buscado. Sera "null" en caso de que no se encuentre.
     */
    public Nodo buscarNodo(Integer clave, AtomicInteger pos) {
        return buscarNodo(this.getRaiz(), clave, pos);
    }

    /**
     * Este método busca una clave y retorna su nodo.
     *
     * @param nodoActual es el nodo desde el cual se esta buscando.
     * @param clave es la clave buscada en los nodos.
     * @param pos es la posicion de esa clave.
     * @return NodoB - es el nodo buscado. Sera "null" en caso de que no se encuentre.
     */
    private Nodo buscarNodo(Nodo nodoActual, Integer clave, AtomicInteger pos) {
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
