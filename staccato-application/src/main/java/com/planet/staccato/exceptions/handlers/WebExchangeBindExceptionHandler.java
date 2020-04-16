package com.planet.staccato.exceptions.handlers;

import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Iterator;
import java.util.List;

/**
 * @author joshfix
 * Created on 2/6/20
 */
@Component
public class WebExchangeBindExceptionHandler extends AbstractBadRequestExceptionHandler<WebExchangeBindException> {

    public WebExchangeBindExceptionHandler() {
        super(WebExchangeBindException.class.getSimpleName());
    }

    @Override
    protected String getMessage(WebExchangeBindException ex) {
        List<ObjectError> errors = ex.getAllErrors();
        if (!errors.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            Iterator<ObjectError> it = errors.iterator();
            while (it.hasNext()) {
                sb.append(it.next().getDefaultMessage());
                if (it.hasNext()) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }
        return ex.getMessage();
    }

}
