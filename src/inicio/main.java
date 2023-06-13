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
        int[] valores = {1,2,3,4,5,6,7};
//        int[] valores = {52, 57, 62, 63, 73, 49, 45, 47, 75, 41, 42};
//        int[] valores = {3, 15, 25, 35, 45, 16, 17, 18, 19, 20, 21 ,22 ,23, 24};

        System.out.println("-- INICIO --");
        System.out.println("INSERTANDO VALORES AL ARBOL B");
        for (Integer valor : valores) {
            System.out.println("Insertando... valor " + valor);
            a.insertar(valor);
        }

//        System.out.println("ELIMINANDO VALOR: 73");
//        a.eliminar(73);
//        System.out.println("ELIMINANDO VALOR: 49");
//        a.eliminar(49);

        System.out.println("");
        System.out.println("Profundidad: ");
        a.mostrarProfundidad(ArbolB.ENORDEN);
        System.out.println("");

        System.out.println("");
        System.out.println("Amplitud A");
        a.mostrarAmplitud();
        System.out.println("\n");

        System.out.println("ELIMINANDO VALOR: 73");
        a.eliminar(73);
        System.out.println("ELIMINANDO VALOR: 49");
        a.eliminar(49);
        System.out.println("ELIMINANDO VALOR: 57");
        a.eliminar(57);

        System.out.println("");
        System.out.println("Profundidad: ");
        a.mostrarProfundidad(ArbolB.ENORDEN);
        System.out.println("");

        System.out.println("");
        System.out.println("Amplitud A");
        a.mostrarAmplitud();
        System.out.println("\n");
    }
}
