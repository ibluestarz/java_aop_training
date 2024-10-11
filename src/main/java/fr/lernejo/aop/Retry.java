package fr.lernejo.aop;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int maxTries() default 1;
    Class<? extends Exception>[] errorTypes() default {};
}
