package com.planet.staccato.exceptions.handlers;

import com.planet.staccato.exception.StacException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Abstract class to handle various types of exceptions.
 *
 * @author joshfix
 * Created on 2/6/20
 */
@Data
@RequiredArgsConstructor
public class AbstractExceptionHandler<T extends Throwable> {

    private final String exceptionName;

    protected String getMessage(T ex) {
        return ex.getMessage();
    }

    protected HttpStatus getStatus(T ex) {
        return null;
    }

    public StacException getErrorResponse(T ex) {

        StacException stacException = new StacException();
        stacException.description(getMessage(ex));

        HttpStatus status = getStatus(ex);
        if (status != null) {
            stacException.setCode(Integer.toString(status.value()));
        }

        return stacException;
    }
}
