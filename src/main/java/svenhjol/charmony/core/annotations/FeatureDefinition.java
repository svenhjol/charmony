package svenhjol.charmony.core.annotations;

import svenhjol.charmony.core.enums.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation decorates a SidedFeature.
 * A SidedFeature is one "side" of a charmony-feature and gets instantiated when the mod side is set up.
 */
@SuppressWarnings("unused")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeatureDefinition {
    /**
     * Higher priorities will instantiate the feature sooner.
     */
    int priority() default 0;

    /**
     * Description to be shown in the config file and control panel.
     */
    String description() default "";

    /**
     * The "side" (e.g. common) that this sided feature targets.
     * A feature can one or more sides.
     */
    Side side();

    /**
     * If true, this feature can be disabled via config file and control panel.
     * If false, this setting disappears from the config and panel.
     * In general features should have the ability to toggle this unless the feature is
     * a critical function such as Core.
     */
    boolean canBeDisabled() default true;

    /**
     * If true, this feature is enabled when the mod is first installed.
     * The ability to enable/disable the feature is not affected UNLESS canBeDisabled() is false.
     */
    boolean enabledByDefault() default true;

    /**
     * If true, this feature's configuration will be shown in the corresponding sided config file.
     * E.g. a SidedFeature that targets client side will have an entry in the mod-client.toml.
     */
    boolean showInConfig() default true;
}
