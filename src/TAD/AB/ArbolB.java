package TAD.AB;


/**
 *
 * @author Ismael
 */
public class ArbolB extends ArbolM{

    public ArbolB(int m) {
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
        } else {                                                    //CASO 1: Arbol con elementos
            if (buscarPos(getRaiz(), clave) == -1) {                //Si no existe la clave
                NodoB nodoHoja = buscarNodoB(getRaiz(), clave);     //Buscamos el Nodo hoja
                insertarElemento(nodoHoja, clave);                  //ingresar elemento
                seInserto = true;
            }
        }
        return seInserto;
    }

    /*
    Este metedo efectiviza la insercion de un elemento en un nodo
    Se analizan dos casos: Si el nodo tiene espacio o si esta lleno
     */
    private void insertarElemento(NodoB nodo, int clave) {
        if (nodo != null) {                                         //SI NO ES VACIO
            if (nodo.getCantClaves() < nodo.getM() - 1) {           //CASO A: Nodo con espacio
                acomodarElementosDentroNodo(nodo, clave);
            } else {                                                //CASO B: Nodo sin espacio
                insertarPadre(nodo, clave);                         //Hay que analizar al padre
            }
        }
    }

    /*
    Este metodo efectiviza la insercion de la mediana en el nodo padre
    Se analizan dos casos: si el nodo tiene padre o si no lo tiene
    Ademas en el primer caso, se anliza si dicho nodo es la raiz.
     */
    private void insertarPadre(NodoB nodoIzquierdo, int clave) {
        int[] claves = obtenerArregloOrdenado(nodoIzquierdo, clave);    //aux del nuevo arreglo con la clave
        int claveMediana = claves[claves.length / 2];                   //obtenermos la mediana del arreglo
        nodoIzquierdo = eliminarMediana(nodoIzquierdo, claves);         //eliminar mediana del nodo, con el arreglo
        
        if (nodoIzquierdo.getPadre() == null) {                         //CASO A: el nodo no tiene padre

            NodoB padre = new NodoB(nodoIzquierdo.getM(), claveMediana);//Creamos el nodo padre
            NodoB nuevoDerecho = particionar(nodoIzquierdo);            //Particionamos el nodo izquierdo
                                                                        //Y almacenamos en el nodo derecho
            nuevoDerecho.setPadre(padre);                               //seteamos al nodo nuevo con su nuevo padre
            nodoIzquierdo.setPadre(padre);                              //seteamos al nodo con su nuevo padre

            padre.setHijoEn(0, nodoIzquierdo);                          //seteamos al hijo izquierdo del padre
            padre.setHijoEn(1, nuevoDerecho);                           //seteamos al hijo derecho del padre    

            //si este nodo era la raiz se la reemplaza por el padre
            if (nodoIzquierdo == this.getRaiz()) {
                this.setRaiz(padre);
            }
        } else {                                                    //CASO B: el nodo si tiene padre
            NodoB padre = nodoIzquierdo.getPadre();
            NodoB nodoDerecho = particionar(nodoIzquierdo);

            if(padre.getCantClaves() < padre.getM() - 1){               //CASO B.1: el nodo padre tiene espacio
                insertarElemento(padre, claveMediana);                
                ordenarHijosPadreInsertar(padre, claveMediana, nodoIzquierdo, nodoDerecho);
            }else{                                                      //CASO B.2: el nodo padre NO tiene espacio
                insertarPadreLleno(padre, nodoDerecho, nodoIzquierdo, claveMediana);
            }
        }
    }
    
    /*
    
    */
    private void insertarPadreLleno(NodoB padre, NodoB nodoDerecho, NodoB nodoIzquierdo, int claveMediana){
        NodoB auxDer = padre.getHijoEn(padre.getCantClaves());
        insertarPadre(padre, claveMediana);

        NodoB abuelo = padre.getPadre();
        NodoB padreDer = abuelo.getHijoEn(abuelo.getCantClaves());

        padreDer.setHijoEn(padre.getCantClaves() - 1, nodoDerecho);      //almacena al hijo derecho en el lado izq
        padreDer.setHijoEn(padre.getCantClaves(), auxDer);               //almacena al nuevo nodo en el lado der

        int i = padreDer.getM() - 1;
        while (0 <= i) {
            if (padreDer.getHijoEn(i) != null) {
                padreDer.getHijoEn(i).setPadre(padreDer);
            }
            i--;
        }
        
        padre.setHijoEn(padre.getCantClaves(), nodoIzquierdo);        //Setea el hijo con el nuevo nodo Der
        padre.setHijoEn(padre.getCantClaves() + 1, null);             //Elimina al hijo de la derecha
    }
    
    /*
    
    */
    private void insertarPadreLlenoRecursivo(NodoB padre, NodoB nodoDerecho, NodoB nodoIzquierdo, int claveMediana){
        
        
        NodoB auxDer = padre.getHijoEn(padre.getCantClaves());
        insertarPadre(padre, claveMediana);

        NodoB abuelo = padre.getPadre();
        NodoB padreDer = abuelo.getHijoEn(abuelo.getCantClaves());

        padreDer.setHijoEn(padre.getCantClaves() - 1, nodoDerecho);      //almacena al hijo derecho en el lado izq
        padreDer.setHijoEn(padre.getCantClaves(), auxDer);               //almacena al nuevo nodo en el lado der

        int i = padreDer.getM() - 1;
        while (0 <= i) {
            if (padreDer.getHijoEn(i) != null) {
                padreDer.getHijoEn(i).setPadre(padreDer);
            }
            i--;
        }
        
        padre.setHijoEn(padre.getCantClaves(), nodoIzquierdo);      //Setea el hijo con el nuevo nodo Der
        padre.setHijoEn(padre.getCantClaves() + 1, null);           //Elimina al hijo de la derecha
    }
    
    /*
    
    */
    private void acomodarElementosDentroNodo(NodoB nodo, int clave){
        int i = nodo.getCantClaves() - 1;                       //Se obtiene la cantidad de claves
        nodo.setClaveEn(i + 1, clave);                          //Agrega la clave al final del arreglo
        
        if (nodo.esHoja()) {
            while ((0 <= i) && (clave < nodo.getClaveEn(i))) {      //ACOMODA TODOS LOS ELEMENTOS
                nodo.setClaveEn(i + 1, nodo.getClaveEn(i));         //Moviendo la clave desde el final
                nodo.setClaveEn(i, clave);                          //Hasta su posicion
            }
        } else {
            NodoB aux;      //Variable auxiliar para desplazar hijos
            while ((0 <= i) && (clave < nodo.getClaveEn(i))) {      //ACOMODA TODOS LOS ELEMENTOS
                nodo.setClaveEn(i + 1, nodo.getClaveEn(i));         //Moviendo la clave desde el final
                nodo.setClaveEn(i, clave);                          //Hasta su posicion
                aux = nodo.getHijoEn(i + 2);                        //Aux de hijo
                nodo.setHijoEn(i + 2, nodo.getHijoEn(i + 1));       //Mueve al hijo desde el final
                nodo.setHijoEn(i + 1, aux);                         //hasta su posicion
                i--;
            }
        }
        nodo.setCantClaves(nodo.getCantClaves() + 1);               //Suma 1 a la cantidad de claves
    }
    /*
    
     */
    private NodoB particionar(NodoB nodoInterno) {
        int i = nodoInterno.getCantClaves() - 1;
        int j = 0;
        //Creamos al nuevo hijo derecho
        NodoB nuevoNodoDerecho = new NodoB(nodoInterno.getM(), nodoInterno.getClaveEn(i--));

        while (i > (nodoInterno.getM() / 2) - 1) {                          //pasamos la mitad de los elementos
            insertarElemento(nuevoNodoDerecho, nodoInterno.getClaveEn(i));      //mayores que la mediana
            nuevoNodoDerecho.setHijoEn(j, nodoInterno.getHijoEn(i));            //seteamos al hijo izquierdo
            nuevoNodoDerecho.setHijoEn(j + 1, nodoInterno.getHijoEn(i + 1));    //setamos al hijo derecho
            nodoInterno.setHijoEn(i + 1, null);                             //eliminamos al hijo derecho
            i--;
            j++;
        }

        nodoInterno.setHijoEn(i + 1, null);         //eliminamos al hijo izquierdo
        nodoInterno.setCantClaves(i + 1);           //eliminamos logicamente la mitad de claves
        return nuevoNodoDerecho;
    }

    /*
    Este metodo ordena los hijos del padre, al ingresar la mediana estos pueden quedar desordenados
     */
    private void ordenarHijosPadreInsertar(NodoB padre, int claveMediana, NodoB hijoIzquierdo, NodoB hijoDerecho) {
        int posMediana = buscarPos(padre, claveMediana);       
        padre.setHijoEn(posMediana, hijoIzquierdo);
        padre.setHijoEn(++posMediana, hijoDerecho);
        hijoIzquierdo.setPadre(padre);
        hijoDerecho.setPadre(padre);
    }

    /*
    Este metodo elimina la clave de un arbolB.
    Se analizan dos casos: un nodo interno o raiz por un lado, y un nodo 
     */
    public void eliminarB(int clave) {
        if (!esVacio()) {                                             //CASO 1: Arbol con elementos
            int pos = buscarPos(getRaiz(), clave);
            if (pos != -1) {                                                    //Si no existe la clave
                NodoB nodo = buscarNodoEliminar(getRaiz(), pos, clave);         //Buscamos el Nodo
                if (!nodo.esHoja()) {                                           //CASO A: Si es un nodo interno o raiz
                    sustituirClave(nodo, pos, clave);                           //se sustituye por alguno de los hijos
                } else {                                                        //CASO B: si es un nodo hoja
                    eliminarClave(nodo, pos);                                   //se elimina la clave del nodo
                }
            }
        }
    }

    /*
    Este metodo sustituye la clave por el maximo menor si es posible o por el minimo mayor.
    Si no es posible solo se procede a eliminar la clave sin sustituir nada
    Se analizan dos casos, hijo izquierdo con las claves menores y el hijo derecho con las claves mayores
     */
    private void sustituirClave(NodoB nodo, int pos, int clave) {
        if (nodo.getHijoEn(pos).getCantClaves() > nodo.getM() / 2) {
            int claveSustituta = nodo.getHijoEn(pos).getClaveEn(nodo.getHijoEn(pos).getCantClaves() - 1);
            this.eliminarB(claveSustituta);
            nodo.setClaveEn(pos, claveSustituta);
        } else {
            if (nodo.getHijoEn(pos + 1).getCantClaves() > nodo.getM() / 2) {
                int claveSustituta = nodo.getHijoEn(pos + 1).getClaveEn(0);
                this.eliminarB(claveSustituta);
                nodo.setClaveEn(pos, claveSustituta);
            } else {
                this.eliminarClave(nodo, pos);
            }
        }
    }

    /*
    En este metedo se efectiviza la eliminacion de la clave del nodo.
    Se analizan dos casos: si el nodo es hoja y puede hacerse una eliminacion directa o
    Si el nodo es un nodo interno / raiz y no hay hijos que le puedan prestar una clave
     */
    private void eliminarClave(NodoB nodo, int pos) {
        if ((nodo.getCantClaves() > (nodo.getM() / 2)) && (nodo.esHoja())) {
            int i;                                  //Si entro aca es para eliminar la clave
            for (i = 0; i < nodo.getCantClaves() - 1; i++) {
                if (i >= pos) {
                    nodo.setClaveEn(i, nodo.getClaveEn(i + 1));
                    nodo.setHijoEn(i, nodo.getHijoEn(i + 2));
                } else {
                    nodo.setClaveEn(i, nodo.getClaveEn(i));
                    nodo.setHijoEn(i, nodo.getHijoEn(i));
                }
            }
            nodo.setCantClaves(i);
        } else {
            int i = 0;                              //si entro aca es xq ambos hijos tinen m/2 claves
            NodoB nuevo = new NodoB(nodo.getM(), 0);
            while (i < nodo.getM() / 2) {
                nuevo.setClaveEn(i, nodo.getHijoEn(pos).getClaveEn(i));
                i++;
            }
            pos++;  //hay que tener en cuenta la poss
            while (i < nodo.getM() - 1) {
                nuevo.setClaveEn(i, nodo.getHijoEn(pos).getClaveEn(i));
                i++;
            }
            nuevo.setCantClaves(i);
            ordenarPadreEliminar(nodo, nuevo, pos);
        }
    }

    /*
    Este metodo ordena las claves y los hijos en caso de hacer una eliminacion de un nodo interno.
    En caso de que sea un nodo raiz, el nuevo nodo debe pasar a ser la nueva raiz.
     */
    private void ordenarPadreEliminar(NodoB nodo, NodoB nuevo, int pos) {
        int i = pos;
        while (i < nodo.getCantClaves() - 1) {
            nodo.setClaveEn(i, nodo.getClaveEn(i + 1));
            nodo.setHijoEn(i + 1, nodo.getHijoEn(i + 2));
            i++;
        }
        nodo.setHijoEn(i + 1, null);
        nodo.setHijoEn(pos, nuevo);
        nodo.setCantClaves(nodo.getCantClaves() - 1);

        if (nodo.getCantClaves() == 0) {
            this.setRaiz(nuevo);
        }
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
    Este metodo busca el nodo en el cual debe insertarse la clave
     */
    private NodoB buscarNodoEliminar(NodoB r, int pos, int clave) {
        if (clave == r.getClaveEn(pos)) {
            return r;
        } else {
            if (clave > r.getClaveEn(r.getCantClaves() - 1)) {
                if (r.getHijoEn(r.getCantClaves()) != null) {
                    return buscarNodoEliminar(r.getHijoEn(r.getCantClaves()), pos, clave);
                }
            } else {
                for (int i = 0; i < r.getCantClaves(); i++) {
                    if (clave < r.getClaveEn(i)) {
                        if (r.getHijoEn(i) != null) {
                            return buscarNodoEliminar(r.getHijoEn(i), pos, clave);
                        }
                    }
                }
            }
        }
        return r;
    }

    /*
    Este metodo retorna la posicion de una clave en un nodo,
    si no existe en el nodo, retorna -1
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
    Este metodo agrega la mediana a un arreglo para evitar usar el arreglo de los nodos.
    Retorna un arreglo ordenado con la nueva clave insertada y
    asegura que la mediana estara en el medio del arreglo
     */
    private int[] obtenerArregloOrdenado(NodoB nodo, int data) {
        int i = 0;
        int[] claves = new int[nodo.getCantClaves() + 1];
        while (i < nodo.getCantClaves()) {
            claves[i] = nodo.getClaveEn(i);
            i++;
        }
        claves[i] = data;
        i--;
//        Arrays.sort(claves);  //ordenar por quickSort - en pseudocodigo
        while ((i >= 0) && (data < claves[i])) {
            claves[i + 1] = claves[i];
            claves[i] = data;
            i--;
        }
        return claves;
    }

    /*
    Este metodo utiliza el arreglo como una variable auxiliar y para localizar la mediana
    Este metodo retorna el mismo nodo que se uso como parametro, pero sin la mediana
     */
    private NodoB eliminarMediana(NodoB nodo, int[] claves) {
        int i = 0;
        int j = 0;
        while (i <= nodo.getCantClaves() - 1) {
            if (j == (nodo.getCantClaves() / 2)) {
//                j++;
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
