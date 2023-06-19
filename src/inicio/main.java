package inicio;

import TAD.AB.ArbolB;

/**
 *
 * @author GONZALEZ ESPADA, José Ismael
 */
public class main {

    public static void main(String[] args) {
        ArbolB a = new ArbolB(3);
//        ArbolB a = new ArbolB(5);

        //VALORES
//        int[] valores = {1,2,3,4,5,6,7};
//        int[] valores = {52, 57, 62, 63, 73, 49, 45, 47, 75, 41, 42};
//        int[] valores = {3, 15, 25, 35, 45, 16, 17, 18, 19, 20, 21, 22, 23, 24};
        int[] valores = {
            192, 76, 11, 311, 445, 473, 68, 217, 382, 440,
            221, 204, 394, 395, 434, 316, 231, 285, 139, 26,
            123, 441, 164, 274, 420, 470, 198, 12, 391, 271,
            292, 209, 409, 101, 433, 285, 308, 458, 277, 379,
            102, 338, 367, 234, 27, 228, 380, 422, 437, 188
        };

        System.out.println("-- INICIO --");
        System.out.println("INSERTANDO VALORES AL ARBOL B");
        for (Integer valor : valores) {
            System.out.println("Insertando... valor " + valor);
            a.insertar(valor);
        }

//        int[] eliminados = {73, 49, 57, 42, 63, 47, 45, 41, 52, 62};
//        int[] eliminados = {17};
//        System.out.println("ELIMINANDO VALORES AL ARBOL B");
//        for (Integer valor : eliminados) {
//            System.out.println("Eliminando... valor " + valor);
//            a.eliminar(valor);
//        }

        System.out.println("");
        System.out.println("Profundidad: ");
        a.mostrarProfundidad(ArbolB.ENORDEN);
        System.out.println("");

        System.out.println("");
        System.out.println("Amplitud A");
        a.mostrarAmplitud();
        System.out.println("\n");

        int eliminar = 311;
        System.out.println("ELIMINANDO VALOR: " + eliminar);
        a.eliminar(eliminar);

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
