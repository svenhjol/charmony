package svenhjol.charmony.scaffold.annotations;

import svenhjol.charmony.scaffold.enums.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureDefinition {
    int priority() default 0;

    String description() default "";

    Side side();

    boolean canBeDisabled() default true;

    boolean enabledByDefault() default true;
}
