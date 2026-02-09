package com.project.prjauth.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WordRangeValidator implements ConstraintValidator<WordRange, String> {
    private int min;
    private int max;

    @Override
    public void initialize(WordRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        String[] words = value.trim().split("\\s+");
        int count = words.length;

        return count >= min && count <= max;
    }

}
