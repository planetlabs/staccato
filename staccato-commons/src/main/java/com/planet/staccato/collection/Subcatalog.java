package com.planet.staccato.collection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any extension that contains a field or method annotated with @Subcatalog will be automatically eligible to have
 * subcatalogs build for that field dynamically.  The catalog code will build a subcatalog link for the annotated
 * field and provide links for every unique value matched in the database.
 * @author joshfix
 * Created on 10/23/18
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Subcatalog {}
