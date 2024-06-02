package com.moraes.authenticator.api.validation;

import java.time.LocalDate;

import com.moraes.authenticator.api.validation.interfaces.IDayValidBetween5O10O15;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DayValidBetween5O10O15 implements ConstraintValidator<IDayValidBetween5O10O15, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value != null) {
            final int day = value.getDayOfMonth();
            return day == 5 || day == 10 || day == 15;
        }
        return true;
    }

}
