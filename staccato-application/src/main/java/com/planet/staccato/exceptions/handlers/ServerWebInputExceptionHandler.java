package com.planet.staccato.exceptions.handlers;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.planet.staccato.exceptions.InvalidParameterException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebInputException;

import java.util.Collection;

/**
 * @author joshfix
 * Created on 2/6/20
 */
@Component
public class ServerWebInputExceptionHandler extends AbstractBadRequestExceptionHandler<ServerWebInputException> {

    public ServerWebInputExceptionHandler() {
        super(ServerWebInputException.class.getSimpleName());
    }

    @Override
    protected String getMessage(ServerWebInputException ex) {

        Throwable t = ex.getCause();
        if (t != null && t.getCause() != null && t.getCause() instanceof UnrecognizedPropertyException) {
            String propertyName = ((UnrecognizedPropertyException) t.getCause()).getPropertyName();
            Collection knownFields = ((UnrecognizedPropertyException) t.getCause()).getKnownPropertyIds();
            return InvalidParameterException.buildMessage(propertyName, knownFields);
        }

        return ex.getMessage();
    }
}
