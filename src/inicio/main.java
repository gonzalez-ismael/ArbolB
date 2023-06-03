package inicio;

import TAD.AB.ArbolB;
import TAD.AB.ArbolB3;

/**
 *
 * @author Ismael
 */
public class main {

    public static void main(String[] args) {
        ArbolB3 a = new ArbolB3(3);
        
        //VALORES FUNCIONA
        int[] valores = {1,2,3,4,5};
//        int[] valores = {3, 15, 25, 35, 45, 16, 17, 18, 19, 20, 21 ,22 ,23, 24};
        //VALORES DONDE FALLA
//        int[] valores = {1,2,3,4,5,6,7};
//        int[] valores = {3, 15, 25, 35, 45, 16, 17, 18, 19, 20, 21 ,22 ,23, 24, 26};
        
        System.out.println("-- INICIO --");
        System.out.println("INSERTANDO VALORES AL ARBOL B");
        for (Integer valor : valores) {
            System.out.println("Insertando... valor " + valor);
            a.insertar(valor);
//            System.out.println("Se inserto "+valores[i]+"?: "+a.insertarB(valores[i]));
        }
        
//        System.out.println(a.getRaiz().getHijoEn(0).getClaveEn(1));
        System.out.println("");
        System.out.println("Profundidad: ");
        a.mostrarProfundidad(ArbolB.ENORDEN);
        System.out.println("");
        
        System.out.println("");
        System.out.println("Amplitud A");
        a.mostrarAmplitud();
        
//        
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        System.out.println("Valor inicial: " + atomicInteger.get());
//
//        int resultado = atomicInteger.incrementAndGet();
//        System.out.println("Incremento: " + resultado);
//
//        atomicInteger.compareAndSet(1, 5);
//        System.out.println("Actualización: " + atomicInteger.get());
//
//        int decremento = atomicInteger.decrementAndGet();
//        System.out.println("Decremento: " + decremento);
        
        
        
//        System.out.println("NODO PADRE FIJADO");
//        a.getRaiz().getHijoEn(1).getHijoEn(0).getHijoEn(1).mostrarClaves();
//        System.out.println("");
        
//        a.insertarB(26);
////        a.eliminarB(24);
//
//        System.out.println("");
//        System.out.println("Profundidad: ");
//        a.mostrarProfundidad(ArbolB.ENORDEN);
//        
//        System.out.println("");
//        System.out.println("Amplitud");
//        a.mostrarAmplitud();
//        
//        System.out.println("");
        
//        a.getRaiz().getHijoEn(1).getHijoEn(1).getHijoEn(1).mostrarClaves();
    
//        a.eliminarB(16);
//        a.eliminarB(19);
//        a.eliminarB(25);
//        System.out.println("");
//        System.out.println("Profundidad: ");
//        a.mostrarProfundidad(ArbolB.ENORDEN);
//        System.out.println("");
//        System.out.println("Amplitud");
//        a.mostrarAmplitud();
    }
}
