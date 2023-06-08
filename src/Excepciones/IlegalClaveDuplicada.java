package Excepciones;

/**
 * Esta clase funciona como una excepcion personalizada para situaciones donde se ingresa una clave existente en un Árbol B.
 * 
 * 
 * @author Ismael
 */
public class IlegalClaveDuplicada extends Exception{
    public IlegalClaveDuplicada(String mensaje) {
        super(mensaje);
    }
}
