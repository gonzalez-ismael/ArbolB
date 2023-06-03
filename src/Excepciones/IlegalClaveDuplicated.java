package Excepciones;

/**
 *
 * @author Ismael
 */

/*
    Clase Excepcion para mandar mensajes de claves duplicadas.
*/
public class IlegalClaveDuplicated extends Exception{
    public IlegalClaveDuplicated(String mensaje) {
        super(mensaje);
    }
}
