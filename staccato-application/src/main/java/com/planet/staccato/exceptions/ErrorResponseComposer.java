package com.planet.staccato.exceptions;

import com.planet.staccato.exception.StacException;
import com.planet.staccato.exceptions.handlers.AbstractExceptionHandler;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author joshfix
 * Created on 2/6/20
 */
@Component
public class ErrorResponseComposer<T extends Throwable> {

    private final Map<String, AbstractExceptionHandler<T>> handlers;

    public ErrorResponseComposer(List<AbstractExceptionHandler<T>> handlers) {
        this.handlers = handlers.stream().collect(
                Collectors.toMap(AbstractExceptionHandler::getExceptionName,
                        Function.identity(), (handler1, handler2) -> AnnotationAwareOrderComparator
                                .INSTANCE.compare(handler1, handler2) < 0 ?
                                handler1 : handler2));
    }

    /**
     * Given an exception, finds a handler for
     * building the response and uses that to build and return the response
     */
    public Optional<StacException> compose(T ex) {

        AbstractExceptionHandler<T> handler = null;

        // find a handler for the exception
        // if no handler is found,
        // loop into for its cause (ex.getCause())
        T originalEx = ex;
        while (ex != null) {
            handler = handlers.get(ex.getClass().getSimpleName());

            if (handler != null) // found a handler
                break;

            ex = (T) ex.getCause();
        }

        if (handler != null) {
            // a handler is found
            return Optional.of(handler.getErrorResponse(ex));
        }

        if (ex == null) {
            ex = originalEx;
        }

        return Optional.of(new StacException(String.valueOf(HttpStatus.BAD_REQUEST.value()), ex.getMessage()));
    }
}