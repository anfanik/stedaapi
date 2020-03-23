package me.anfanik.steda.api.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Anfanik
 * Date: 05/01/2020
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SubcommandHandler {

    String[] value(); // Aliases

    boolean ignoreParentAccessChecks = false;

}
