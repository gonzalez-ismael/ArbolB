package TAD.AB;

/**
 *
 * @author Ismael
 */
public class ArbolB2 extends ArbolM {

    private final int NO_EXISTE = -1;

    public ArbolB2(int m) {
        super(m);
    }

    /*
        Este método inserta un dato en el arbol
        Se analizan dos casos: arbol vacio, y arbol con elementos.
     */
    public boolean insertarB(int clave) {
        boolean seInserto = false;
        if (esVacio()) {                                        //CASO 0: Arbol Vacio
            NodoB nuevo = new NodoB(getM(), clave);             //Creamos nodo nuevo
            setRaiz(nuevo);                                     //Arbol seteado
            seInserto = true;
        } else {                                                        //CASO 1: Arbol con elementos
            if (!existeClave(this.getRaiz(), clave)) {                  //Evitamos claves repetidas
                NodoB nodoActual = buscarNodoB(this.getRaiz(), clave);
                if (!nodoActual.estaLleno()) {                          //Si la hoja contiene menos de m-1 claves, insertar en hoja
                    insertarEnHoja(nodoActual, clave);
                    seInserto = true;
                } else {                                                //Sino, agregar la clave, subir la mediana y dividir los nodos
                    insertarEnHoja(nodoActual, clave);
                    partirNodo(nodoActual);
                }
            }
        }
        return seInserto;
    }

    /*
        Este metodo retorna el indice de una clave en un nodo, si no existe en el nodo, retorna -1
     */
    private int buscarIndice(int e, NodoB r) {
        int indice = 0;
        while (indice < r.getCantClaves() && e > r.getClaveEn(indice)) {
            indice++;
        }
        return indice;
    }

    /*
    Este metodo busca el nodo en el cual debe insertarse la clave
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
        Este metodo retorna la existencia de una clave en el arbol. True si existe, false si no.
     */
    private boolean existeClave(NodoB r, int e) {
        if (r == null) {
            return false;
        } else {
            for (int i = 0; i < r.getCantClaves(); i++) {
                if (e == r.getClaveEn(i)) {
                    return true;
                } else {
                    if (e < r.getClaveEn(i)) {
                        return existeClave(r.getHijoEn(i), e);
                    }
                }
            }
            return existeClave(r.getHijoEn(r.getCantClaves()), e);
        }
    }

    // Función para dividir un nodo lleno en un árbol B
    private void partirNodo(NodoB nodo) {
        NodoB nodoPadre = nodo.getPadre();
        int mediana = nodo.getClaveEn(nodo.getCantClaves() / 2);
        // Crear un nuevo nodo y transferir las claves e hijos
        System.out.println("NUEVO NODO");
        NodoB nuevoNodo = new NodoB(this.getM());
        nuevoNodo.setCantClaves(nodo.getCantClaves() - mediana-1);
//        nuevoNodo.setClaves(transferirClaves(nodo, mediana, nodo.getCantClaves()-1));
//        nuevoNodo.setHijos(trasnferirHijos(nodo, mediana + 1, nodo.getCantHijos()));

        System.out.println("NODO");
        nodo.setCantClaves(mediana);
//        nodo.setClaves(transferirClaves(nodo, 0, mediana-2));
//        nodo.setHijos(trasnferirHijos(nodo, 0, mediana-2));

        // Insertar la mediana en el nodo padre
        if (nodo == this.getRaiz()) {                           //Crear una nueva raíz si el nodoActual era la raíz
            NodoB nuevaRaiz = new NodoB(this.getM(), mediana);
            nuevaRaiz.setHijoEn(0, nodo);
            nuevaRaiz.setHijoEn(1, nuevoNodo);
            nodo.setPadre(nuevaRaiz);
            nuevoNodo.setPadre(nuevaRaiz);
            this.setRaiz(nuevaRaiz);
        } else {                                                //Insertar en el padre, actualizar esto con los hijos
            insertarEnHoja(nodoPadre, mediana);
        }
    }

    /*
        Función para insertar una clave en una hoja del árbol B
     */
    private void insertarEnHoja(NodoB nodo, int clave) {
        int indice = buscarIndice(clave, nodo);
//        System.out.println("La clave, " + clave + " en el indice: " + indice);
        moverClaves(nodo, indice, nodo.getCantClaves());
        nodo.setClaveEn(indice, clave);
        nodo.setCantClaves(nodo.getCantClaves() + 1);
    }

    private NodoB[] trasnferirHijos(NodoB nodoFuente, int indiceInicio, int indiceFin) {
        NodoB[] hijos = new NodoB[nodoFuente.getM() + 1];
        for (int i = 0; i < (indiceFin - indiceInicio); i++) {
            hijos[i] = nodoFuente.getHijoEn(indiceInicio + i);
            nodoFuente.setHijoEn(indiceInicio + i, null);
        }
        return hijos;
    }

    private int[] transferirClaves(NodoB nodoFuente, int indiceInicio, int indiceFin) {
        int[] claves = new int[nodoFuente.getM()];
        for (int i = 0; i <= (indiceFin - indiceInicio); i++) {
            claves[i] = nodoFuente.getClaveEn(indiceInicio + i);
            nodoFuente.setClaveEn(indiceInicio + i, -1);
        }
        return claves;
    }

    /*
        Desplaza las claves 1 a la derecha para agregar una nueva clave en el nodo
     */
    private void moverClaves(NodoB nodo, int indiceInicio, int indiceFin) {
//        System.out.println("Valores inicio: "+indiceInicio+" Valores fin: "+indiceFin);
        for (int i = indiceFin; i > indiceInicio; i--) {
            nodo.setClaveEn(i, nodo.getClaveEn(i - 1));
        }
    }

}
