package TAD.AB;

/**
 *
 * @author Ismael
 */
public class ArbolB extends ArbolM {

    public ArbolB(int m) {
        super(m);
    }

    /*
     * Este método inserta un dato en el arbol
     * Se analizan dos casos: arbol vacio, y arbol con elementos.
     */
    public boolean insertarB(int clave) {
        boolean seInserto = false;
        if (esVacio()) { // CASO 0: Arbol Vacio
            NodoB nuevo = new NodoB(getM(), clave); // Creamos nodo nuevo
            setRaiz(nuevo); // Arbol seteado
            seInserto = true;
        } else { // CASO 1: Arbol con elementos
            if (buscarPos(getRaiz(), clave) == -1) { // Evitamos claves repetidas
                NodoB nodoHoja = buscarNodoB(getRaiz(), clave); // Buscamos el Nodo hoja
                insertarElemento(nodoHoja, clave); // ingresar elemento
                seInserto = true;
            }
        }
        return seInserto;
    }

    /*
     * Este metedo efectiviza la insercion de un elemento en un nodo
     * Se analizan dos casos: Si el nodo tiene espacio o si esta lleno
     */
    private void insertarElemento(NodoB nodo, int clave) {
        if (nodo != null) { // SI NO ES VACIO
            if (nodo.getCantClaves() < nodo.getM() - 1) { // CASO A: Nodo con espacio
                acomodarElementosDentroNodo(nodo, clave);
            } else { // CASO B: Nodo sin espacio
                insertarPadre(nodo, clave); // Hay que analizar al padre
            }
        }
    }

    /*
     * Este metodo efectiviza la insercion de la mediana en el nodo padre
     * Se analizan dos casos: si el nodo tiene padre o si no lo tiene
     * Ademas en el primer caso, se anliza si dicho nodo es la raiz.
     */
    private void insertarPadre(NodoB nodoActual, int clave) {
        Integer[] claves = obtenerArregloOrdenado(nodoActual, clave); // aux del nuevo arreglo con la clave
        Integer claveMediana = claves[claves.length / 2]; // obtenermos la mediana del arreglo
        nodoActual = eliminarMediana(nodoActual, claves); // eliminar mediana del nodo, con el arreglo

        if (nodoActual.getPadre() == null) { // CASO A: El Nodo es Raiz | el nodo no tiene padre

            NodoB padre = new NodoB(nodoActual.getM(), claveMediana);// Creamos el nodo padre
            NodoB nuevoDerecho = particionar(nodoActual); // Particionamos el nodo izquierdo
            // Y almacenamos en el nodo derecho
            nuevoDerecho.setPadre(padre); // seteamos al nodo nuevo con su nuevo padre
            nodoActual.setPadre(padre); // seteamos al nodo con su nuevo padre

            padre.setHijoEn(0, nodoActual); // seteamos al hijo izquierdo del padre
            padre.setHijoEn(1, nuevoDerecho); // seteamos al hijo derecho del padre

            // si este nodo era la raiz se la reemplaza por el padre
            if (nodoActual == this.getRaiz()) {
                this.setRaiz(padre);
            }
        } else { // CASO B: el nodo si tiene padre
            NodoB padre = nodoActual.getPadre();
            NodoB nodoDerecho = particionar(nodoActual);

            if (padre.getCantClaves() < padre.getM() - 1) { // CASO B.1: el nodo padre tiene espacio
                insertarElemento(padre, claveMediana);
                ordenarHijosPadreInsertar(padre, claveMediana, nodoActual, nodoDerecho);
            } else { // CASO B.2: el nodo padre NO tiene espacio
                insertarPadreLleno(padre, nodoDerecho, nodoActual, claveMediana);
            }
        }
    }

    // Necesito ayuda con este algoritmo
    private void insertarPadreLleno(NodoB padre, NodoB nodoDerecho, NodoB nodoIzquierdo, int claveMediana) {
        NodoB auxDer = padre.getHijoEn(padre.getCantClaves());
        insertarPadre(padre, claveMediana);

        NodoB abuelo = padre.getPadre();
        NodoB padreDer = abuelo.getHijoEn(abuelo.getCantClaves());

        padreDer.setHijoEn(padre.getCantClaves() - 1, nodoDerecho); // almacena al hijo derecho en el lado izq
        padreDer.setHijoEn(padre.getCantClaves(), auxDer); // almacena al nuevo nodo en el lado der

        int i = padreDer.getM() - 1;
        while (0 <= i) {
            if (padreDer.getHijoEn(i) != null) {
                padreDer.getHijoEn(i).setPadre(padreDer);
            }
            i--;
        }

        padre.setHijoEn(padre.getCantClaves(), nodoIzquierdo); // Setea el hijo con el nuevo nodo Der
        padre.setHijoEn(padre.getCantClaves() + 1, null); // Elimina al hijo de la derecha
        System.out.println("NODO AUXDER: "+auxDer.getClaveEn(0));
        System.out.println("NODO NODO DER: "+nodoDerecho.getClaveEn(0));
        System.out.println("NODO NODO IZQ: "+nodoIzquierdo.getClaveEn(0));
    }
    

    /*
    
     */
    private void insertarPadreLleno2(NodoB padre, NodoB nodoDerecho, NodoB nodoIzquierdo, int claveMediana){
        if(padre.estaLleno()){
            NodoB nuevoPadre = padre.getPadre();
        }
        
        
        

//    si padre está lleno:
//        claveMediana = clave
//        hijoIzquierdoMediano = hijoIzquierdo
//        hijoDerechoMediano = hijoDerecho
//        nuevoPadre = padre.padre
//        insertarClaveEnPadreConRedistribucion(nuevoPadre, claveMediana, hijoIzquierdoMediano, hijoDerechoMediano)
//    sino:
//        insertarClaveEnPadre(padre, clave, hijoIzquierdo, hijoDerecho)
    }
    
    
    private void acomodarElementosDentroNodo(NodoB nodo, int clave) {
        int i = nodo.getCantClaves() - 1; // Se obtiene la cantidad de claves
        nodo.setClaveEn(i + 1, clave); // Agrega la clave al final del arreglo

        if (nodo.esHoja()) {
            while ((0 <= i) && (clave < nodo.getClaveEn(i))) { // ACOMODA TODOS LOS ELEMENTOS
                nodo.setClaveEn(i + 1, nodo.getClaveEn(i)); // Moviendo la clave desde el final
                nodo.setClaveEn(i, clave); // Hasta su posicion
            }
        } else {
            NodoB aux; // Variable auxiliar para desplazar hijos
            while ((0 <= i) && (clave < nodo.getClaveEn(i))) { // ACOMODA TODOS LOS ELEMENTOS
                nodo.setClaveEn(i + 1, nodo.getClaveEn(i)); // Moviendo la clave desde el final
                nodo.setClaveEn(i, clave); // Hasta su posicion
                aux = nodo.getHijoEn(i + 2); // Aux de hijo
                nodo.setHijoEn(i + 2, nodo.getHijoEn(i + 1)); // Mueve al hijo desde el final
                nodo.setHijoEn(i + 1, aux); // hasta su posicion
                i--;
            }
        }
        nodo.setCantClaves(nodo.getCantClaves() + 1); // Suma 1 a la cantidad de claves
    }

    /*
    
     */
    private NodoB particionar(NodoB nodoActual) {
        int i = nodoActual.getCantClaves() - 1;
        int j = 0;
        // Creamos al nuevo hijo derecho
        NodoB nuevoNodo = new NodoB(nodoActual.getM());

        while (i > (nodoActual.getM() / 2) - 1) { // pasamos la mitad de los elementos
            insertarElemento(nuevoNodo, nodoActual.getClaveEn(i)); // mayores que la mediana
            nuevoNodo.setHijoEn(j, nodoActual.getHijoEn(i)); // seteamos al hijo izquierdo
            nuevoNodo.setHijoEn(j + 1, nodoActual.getHijoEn(i + 1)); // setamos al hijo derecho
            nodoActual.setHijoEn(i + 1, null); // eliminamos al hijo derecho
            i--;
            j++;
        }

        nodoActual.setHijoEn(i + 1, null); // eliminamos al hijo izquierdo
        nodoActual.setCantClaves(i + 1);    // modificamos la cantidad de claves actual
        limpiarClavesExedentes(nodoActual); // eliminamos logicamente la mitad de claves
        return nuevoNodo;
    }
    
    /*
        Este metodo se utiliza en la particion, elimina las claves sobrantes del nodo actual
    */
    private void limpiarClavesExedentes(NodoB nodoActual){
        for(int i=nodoActual.getM()/2; i<=nodoActual.getCantClaves(); i++){
            nodoActual.setClaveEn(i, null);
        }
    }

    /*
     * Este metodo ordena los hijos del padre, al ingresar la mediana estos pueden
     * quedar desordenados
     */
    private void ordenarHijosPadreInsertar(NodoB padre, int claveMediana, NodoB hijoIzquierdo, NodoB hijoDerecho) {
        int posMediana = buscarPos(padre, claveMediana);
        padre.setHijoEn(posMediana, hijoIzquierdo);
        padre.setHijoEn(++posMediana, hijoDerecho);
        hijoIzquierdo.setPadre(padre);
        hijoDerecho.setPadre(padre);
    }

    /*
     * Este metodo busca el nodo en el cual debe insertarse la clave
     */
    private NodoB buscarNodoB(NodoB r, int clave) {
        if (clave > r.getClaveEn(r.getCantClaves() - 1)) {
            if (r.getHijoEn(r.getCantClaves()) != null) {
                return buscarNodoB(r.getHijoEn(r.getCantClaves()), clave);
            }
        } else {
            for (int i = 0; i < r.getCantClaves(); i++) {
                if (clave < r.getClaveEn(i)) {
                    if (r.getHijoEn(i) != null) {
                        return buscarNodoB(r.getHijoEn(i), clave);
                    }
                }
            }
        }
        return r;
    }

    /*
     * Este metodo retorna la posicion de una clave en un nodo,
     * si no existe en el nodo, retorna -1
     */
    private int buscarPos(NodoB r, int e) {
        if (r == null) {
            return -1;
        } else {
            for (int i = 0; i < r.getCantClaves(); i++) {
                if (e == r.getClaveEn(i)) {
                    return i;
                } else {
                    if (e < r.getClaveEn(i)) {
                        return buscarPos(r.getHijoEn(i), e);
                    }
                }
            }
            return buscarPos(r.getHijoEn(r.getCantClaves()), e);
        }
    }

    /*
     * Este metodo agrega la mediana a un arreglo para evitar usar el arreglo de los
     * nodos.
     * Retorna un arreglo ordenado con la nueva clave insertada y
     * asegura que la mediana estara en el medio del arreglo
     */
    private Integer[] obtenerArregloOrdenado(NodoB nodo, int data) {
        int i = 0;
        Integer[] claves = new Integer[nodo.getCantClaves() + 1];
        while (i < nodo.getCantClaves()) {
            claves[i] = nodo.getClaveEn(i);
            i++;
        }
        claves[i] = data;
        i--;
        // Arrays.sort(claves); //ordenar por quickSort - en pseudocodigo
        while ((i >= 0) && (data < claves[i])) {
            claves[i + 1] = claves[i];
            claves[i] = data;
            i--;
        }
        return claves;
    }

    /*
     * Este metodo utiliza el arreglo como una variable auxiliar y para localizar la
     * mediana
     * Este metodo retorna el mismo nodo que se uso como parametro, pero sin la
     * mediana
     */
    private NodoB eliminarMediana(NodoB nodo, Integer[] claves) {
        int i = 0;
        int j = 0;
        while (i <= nodo.getCantClaves() - 1) {
            if (j == (nodo.getM() / 2)) {
//                 j++;
                nodo.setClaveEn(i, claves[++j]);
            } else {
                nodo.setClaveEn(i, claves[j]);
            }
            j++;
            i++;
        }
        return nodo;
    }
}
