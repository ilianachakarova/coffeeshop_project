package com.chakarova.demo.web.customAnnotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatcherValidator implements ConstraintValidator<FieldMatcher,String> {

    private String s1;
    private String s2;
    private String message;

    @Override
    public void initialize(FieldMatcher constraintAnnotation) {
    this.s1 = constraintAnnotation.s1();
    this.s2 = constraintAnnotation.s2();
    this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s1.equals(s2);
    }
}
