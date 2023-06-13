package Excepciones;

/**
 * Esta clase funciona como una excepcion personalizada para situaciones donde se ingresa una clave existente en un Árbol B.
 *
 *
 * @author Ismael
 */
public class ClaveDuplicadaExcepcion extends Exception {

    public ClaveDuplicadaExcepcion(String mensaje) {
        super(mensaje);
    }
}
