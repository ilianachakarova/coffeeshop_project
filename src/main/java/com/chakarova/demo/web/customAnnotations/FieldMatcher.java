package com.chakarova.demo.web.customAnnotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FieldMatcherValidator.class)
public @interface FieldMatcher {
    String message() default "Fields dont match";
    String s1();
    String s2();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
