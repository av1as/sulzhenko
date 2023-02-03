package com.sulzhenko.model.services;

/**
 * ServiceException class creates custom exception thrown at Service layer
 *
 * @author Artem Sulzhenko
 * @version 1.0
 */
public class ServiceException extends RuntimeException {

    // Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a ServiceException with the given detail message.
     * @param message The detail message of the ServiceException.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a ServiceException with the given root cause.
     * @param cause The root cause of the ServiceException.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a ServiceException with the given detail message and root cause.
     * @param message The detail message of the ServiceException.
     * @param cause The root cause of the ServiceException.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
