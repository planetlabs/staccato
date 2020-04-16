package com.planet.staccato.exceptions.handlers;

import com.planet.staccato.exceptions.InvalidParameterException;
import org.springframework.stereotype.Component;

/**
 * @author joshfix
 * Created on 2/6/20
 */
@Component
public class InvalidParameterExceptionHandler extends AbstractBadRequestExceptionHandler<InvalidParameterException> {

    public InvalidParameterExceptionHandler() {
        super(InvalidParameterException.class.getSimpleName());
    }

}
