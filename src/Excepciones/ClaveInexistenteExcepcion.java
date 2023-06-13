package Excepciones;

/**
 * Esta clase funciona como una excepcion personalizada para situaciones donde se elimina una clave inexistente en un Árbol B.
 *
 * @author Ismael
 */
public class ClaveInexistenteExcepcion extends Exception {

    public ClaveInexistenteExcepcion(String message) {
        super(message);
    }

}
