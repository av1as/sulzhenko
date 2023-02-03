package com.sulzhenko.Util;

/**
 * UtilException class creates custom exception thrown by utils
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class UtilException extends RuntimeException {

    // Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a UtilException with the given detail message.
     * @param message The detail message of the UtilException.
     */
    public UtilException(String message) {
        super(message);
    }
}
