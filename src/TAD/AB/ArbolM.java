package TAD.AB;

/**
 * Esta clase representa la estructura basica de un Arbol Multicamino.
 *
 * @author Ismael
 */
public class ArbolM {

    //Atributos privados del NodoB
    private NodoB raiz;
    private int m;
    //Constantes públicas del NodoB
    public static final int PREORDEN = 1;
    public static final int ENORDEN = 2;
    public static final int POSTORDEN = 3;

    /**
     * Constructor desarrollado para crear un arbol vacio de orden m.
     *
     * @param m es la dimension del arbol, la cantidad de ramas/hijos que tiene.
     */
    public ArbolM(int m) {
        this.m = m;
        this.raiz = null;
    }

    /**
     * Método público que retorna la altura de un árbol.
     *
     * @return int - devuelve la altura como un número natural igual o mayor a 0.
     */
    public int altura() {
        return alturaNodo(raiz);
    }

    /**
     * Método privado que retorna la altura de un árbol.
     *
     * @param r es la raíz del árbol.
     * @return int - devuelve la altura del árbol.
     */
    private int alturaNodo(NodoB r) {
        int aMax = 0;
        if (r != null) {
            if (r.esHoja()) {
                aMax = 1;
            } else {
                for (int i = 0; i < r.getM(); i++) {
                    int ak = 1 + alturaNodo(r.getHijoEn(i));
                    if (aMax < ak) {
                        aMax = ak;
                    }
                }
            }
        }
        return aMax;
    }

    /**
     * Método público para mostrar las claves del nodo por nivel - Amplitud.
     */
    public void mostrarAmplitud() {
        if (this.getRaiz() != null) {
            for (int i = 0; i < altura(); i++) {
                System.out.println("NIVEL " + i + " - ");
                recorridoNivel(raiz, i);
            }
        } else {
            System.out.println("NO HAY CLAVES PARA MOSTRAR!\n");
        }
    }

    /**
     * Método privado recursivo que se utiliza para mostrar las claves del nodo por nivel.
     *
     * @param r es el nodo actual.
     * @param nivel es el nivel actual.
     */
    private void recorridoNivel(NodoB r, int nivel) {
        if (r != null) {
            if (nivel == 0) {
                r.mostrarClaves();
            } else {
                for (int i = 0; i < r.getM(); i++) {
                    if (r.getHijoEn(i) != null) {
                        recorridoNivel(r.getHijoEn(i), nivel - 1);
                    }
                }
            }
        }
    }

    /**
     * Método público que muestran las claves en profundidad segun el modo que se use.
     *
     * @param modo indica si el recorrido será en PREORDEN, ENORDEN o POSTORDEN.
     */
    public void mostrarProfundidad(int modo) {
        if (this.getRaiz() != null) {
            switch (modo) {
                case ArbolM.PREORDEN:
                    recorridoPreorden(this.getRaiz());
                    break;
                case ArbolM.ENORDEN:
                    recorridoEnorden(this.getRaiz());
                    break;
                case ArbolM.POSTORDEN:
                    recorridoPostorden(this.getRaiz());
                    break;
                default:
                    System.out.println("Modo Invalido");
                    break;
            }
        } else {
            System.out.println("SIN CLAVES PARA MOSTRAR");
        }

    }

    /**
     * Método privado recursivo que realiza un recorrido en pre-orden.
     *
     * @param r es el nodo actual.
     */
    private void recorridoPreorden(NodoB r) {
        if (r != null) {
            int nClaves = r.getCantClaves();
            if (r.getClaveEn(1) != null) {
                System.out.print(r.getClaveEn(1) + " ");
            }
            recorridoPreorden(r.getHijoEn(0));
            for (int i = 1; i <= nClaves; i++) {
                recorridoPreorden(r.getHijoEn(i));
                if (r.getClaveEn(i + 1) != null) {
                    System.out.print(r.getClaveEn(i + 1) + " ");
                }
            }
        }
    }

    /**
     * Método privado recursivo que realiza un recorrido en-orden.
     *
     * @param r es el nodo actual.
     */
    private void recorridoEnorden(NodoB r) {
        if (r != null) {
            int nClaves = r.getCantClaves();
            recorridoEnorden(r.getHijoEn(0));
            for (int i = 1; i <= nClaves; i++) {
                if (r.getClaveEn(i) != null) {
                    System.out.print(r.getClaveEn(i) + " ");
                }
                recorridoEnorden(r.getHijoEn(i));
            }
        }
    }

    /**
     * Método privado recursivo que realiza un recorrido en post-orden.
     *
     * @param r es el nodo actual.
     */
    private void recorridoPostorden(NodoB r) {
        if (r != null) {
            int nClaves = r.getCantClaves();
            recorridoPostorden(r.getHijoEn(0));
            for (int i = 1; i <= nClaves; i++) {
                recorridoPostorden(r.getHijoEn(i));
                if (r.getClaveEn(i) != null) {
                    System.out.print(r.getClaveEn(i) + " ");
                }
            }
        }
    }

    public boolean esVacio() {
        return raiz == null;
    }

    public NodoB getRaiz() {
        return raiz;
    }

    public void setRaiz(NodoB raiz) {
        this.raiz = raiz;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }
}
