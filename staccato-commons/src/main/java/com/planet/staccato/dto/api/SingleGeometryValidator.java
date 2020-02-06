package com.planet.staccato.dto.api;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author joshfix
 * Created on 2/5/20
 */
public class SingleGeometryValidator implements ConstraintValidator<SingleGeometry, SearchRequest> {

    @Override
    public boolean isValid(SearchRequest searchRequest, ConstraintValidatorContext constraintValidatorContext) {
        return !(searchRequest.getBbox() != null && searchRequest.getIntersects() != null);
    }
}
