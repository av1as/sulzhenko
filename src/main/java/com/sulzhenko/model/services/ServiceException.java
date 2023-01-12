package com.sulzhenko.model.services;

/**
 * This class represents a generic Service exception. It should wrap any exception to the underlying
 * code, such as DAOExceptions.
 */
public class ServiceException extends RuntimeException {

    // Constants ----------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Constructors -------------------------------------------------------------------------------

    /**
     * Constructs a ServiceException with the given detail message.
     * @param message The detail message of the DAOException.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a ServiceException with the given root cause.
     * @param cause The root cause of the DAOException.
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a DAOException with the given detail message and root cause.
     * @param message The detail message of the DAOException.
     * @param cause The root cause of the DAOException.
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
