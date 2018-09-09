package exceptions;

/**
 * @author Dmytro Galomko
 */
public class DBException extends Exception {

    public DBException(Throwable ex) {
        super(ex);
    }

    public DBException(Throwable ex, String mes) {

        super(mes, ex);
    }
}
