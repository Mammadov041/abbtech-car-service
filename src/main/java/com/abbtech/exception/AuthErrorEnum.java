package com.abbtech.exception;

import com.abbtech.exception.base.BaseErrorService;

/**
 * Enum representing a set of sample error types.
 * Each error includes a specific error code, message, and corresponding HTTP status code.
 * Implements the {@link BaseErrorService} interface.
 */
public enum AuthErrorEnum implements BaseErrorService {

    /**
     * Error representing unauthorized access.
     */
    INVALID_TOKEN("INVALID_TOKEN-0001", "INVALID_TOKEN", 401),
    USERNAME_EXISTS("USERNAME_EXISTS-0002", "USERNAME_EXISTS", 400),
    USER_NOT_EXISTS("USER_NOT_EXISTS-0003", "USER_NOT_EXISTS", 401),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS-0004", "INVALID_TOKEN", 401);


    final String message;

    /**
     * HTTP status code corresponding to the error.
     */
    final int httpStatus;

    /**
     * Unique error code.
     */
    final String errorCode;

    /**
     * Constructor for the CarErrorEnum.
     *
     * @param errorCode  The unique error code.
     * @param message    The error message.
     * @param httpStatus The HTTP status code associated with the error.
     */
    AuthErrorEnum(String errorCode, String message, int httpStatus) {
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the error message.
     *
     * @return The error message.
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return The HTTP status code.
     */
    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets the unique error code.
     *
     * @return The error code.
     */
    @Override
    public String getErrorCode() {
        return errorCode;
    }
}

