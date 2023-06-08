package Excepciones;

/**
 *
 * @author Ismael
 */

/*
    Clase Excepcion para mandar mensajes de claves duplicadas.
*/
public class IlegalClaveDuplicada extends Exception{
    public IlegalClaveDuplicada(String mensaje) {
        super(mensaje);
    }
}
