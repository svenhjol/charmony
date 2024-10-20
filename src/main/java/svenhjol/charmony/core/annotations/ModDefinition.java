package svenhjol.charmony.core.annotations;

import svenhjol.charmony.core.enums.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModDefinition {
    String id();

    String name() default "";

    String description() default "";

    Side[] sides();
}
