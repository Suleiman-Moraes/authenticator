package com.moraes.authenticator.api.validation.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.moraes.authenticator.api.validation.DayValidBetween5O10O15;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DayValidBetween5O10O15.class)
public @interface IDayValidBetween5O10O15 {
    
    String message() default "Date must be between the 5th, 10th or 15th";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
