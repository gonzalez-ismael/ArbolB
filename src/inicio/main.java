package inicio;

import TAD.AB.ArbolB;

/**
 *
 * @author Ismael
 */
public class main {

    public static void main(String[] args) {
        ArbolB a = new ArbolB(3);
        
        //VALORES
        int[] valores = {3, 15, 25, 35, 45, 16, 17, 18, 19, 20, 21 ,22 ,23, 24};
        
        System.out.println("-- INICIO --");
        System.out.println("INSERTANDO VALORES AL ARBOL B");
        for (Integer valor : valores) {
            System.out.println("Insertando... valor " + valor);
            a.insertar(valor);
        }

        System.out.println("");
        System.out.println("Profundidad: ");
        a.mostrarProfundidad(ArbolB.ENORDEN);
        System.out.println("");
        
        System.out.println("");
        System.out.println("Amplitud A");
        a.mostrarAmplitud();
        
    }
}
