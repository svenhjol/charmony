package svenhjol.charmony.scaffold.nano.annotations;

import svenhjol.charmony.scaffold.nano.enums.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {
    int priority() default 0;

    String description() default "";

    Side side();

    boolean canBeDisabled() default true;

    boolean enabledByDefault() default true;
}
