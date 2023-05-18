package TAD.AB;

/**
 *
 * @author Ismael
 */
public class ArbolM {
    private NodoB raiz;
    private int m;
    public static final int PREORDEN = 1;
    public static final int ENORDEN = 2;
    public static final int POSTORDEN = 3;

    
    /*
        Constructor creado por el desarrollador
        @param m: recibe la dimension del arbol, la cantidad de ramas/hijos que tiene.
     */
    public ArbolM(int m) {
        this.m = m;
        raiz = null;
    }
    
    public int altura() {
        return alturaNodo(raiz);
    }

    public int alturaNodo(NodoB r) {
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

    public void mostrarAmplitud() {
        for (int i = 0; i < altura(); i++) {
            System.out.println("NIVEL " + i + " - ");
            recorridoNivel(raiz, i);
        }
    }

    public void recorridoNivel(NodoB r, int nivel) {
        if (r != null) {
            if (nivel == 0) {
                r.mostrarClaves();
            } else {
                for (int i = 0; i < r.getM(); i++) {
                    recorridoNivel(r.getHijoEn(i), nivel - 1);
                }
            }
        }
    }

    public void mostrarProfundidad(int modo) {
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
    }

    public void recorridoPreorden(NodoB r) {
        if (r != null) {
            int nClaves = r.getCantClaves();
            for (int i = 0; i < nClaves; i++) {
                System.out.print(r.getClaveEn(i) + " ");
                recorridoPreorden(r.getHijoEn(i));
            }
            recorridoPreorden(r.getHijoEn(nClaves));
        }
    }

    public void recorridoEnorden(NodoB r) {
        if (r != null) {
            int nClaves = r.getCantClaves();
            for (int i = 0; i < nClaves; i++) {
                recorridoEnorden(r.getHijoEn(i));
                System.out.print(r.getClaveEn(i) + " ");
            }
            recorridoEnorden(r.getHijoEn(nClaves));
        }
    }

    public void recorridoPostorden(NodoB r) {
        if (r != null) {
            int i = 0;
            recorridoPostorden(r.getHijoEn(i));
            int nClaves = r.getCantClaves()-1;
            while(i <= nClaves){
                recorridoPostorden(r.getHijoEn(++i));
                System.out.print(r.getClaveEn(nClaves) + " ");
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
