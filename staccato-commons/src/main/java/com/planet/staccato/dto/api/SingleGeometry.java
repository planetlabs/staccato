package com.planet.staccato.dto.api;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author joshfix
 * Created on 2/5/20
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SingleGeometryValidator.class)
@Documented
public @interface SingleGeometry {
    String message () default "Request should not contain both `intersects` and `bbox`. Please choose a single geometry "
            + "field.";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
