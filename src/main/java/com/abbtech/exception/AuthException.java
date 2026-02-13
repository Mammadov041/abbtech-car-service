package com.abbtech.exception;

import com.abbtech.exception.base.BaseErrorService;
import com.abbtech.exception.base.BaseException;
import lombok.Getter;

import java.io.Serial;

/**
 * Exception class representing custom exceptions related to sample error cases.
 * Inherits from {@link BaseException}, allowing it to utilize error details
 * from {@link BaseErrorService} and optional additional arguments for flexible error message formatting.
 */
@Getter
public class AuthException extends BaseException {

    @Serial
    private static final long serialVersionUID = 2L;

    /**
     * Constructor for {@link AuthException} with a throwable and additional arguments.
     *
     * @param baseErrorService The error service providing error details.
     * @param throwable        The root cause of the exception.
     * @param args             Additional arguments for the error.
     */
    public AuthException(BaseErrorService baseErrorService, Throwable throwable, Object... args) {
        super(baseErrorService, throwable, args);
    }

    /**
     * Constructor for {@link AuthException} with additional arguments.
     *
     * @param baseErrorService The error service providing error details.
     * @param args             Additional arguments for the error.
     */
    public AuthException(BaseErrorService baseErrorService, Object... args) {
        super(baseErrorService, args);
    }
}


