package svenhjol.charmony.core.base;

import com.google.common.base.CaseFormat;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.enums.Side;

@SuppressWarnings("unused")
public abstract class SidedFeature {
    private final Mod mod;
    private final Log log;

    private boolean enabled;

    public SidedFeature(Mod mod) {
        this.mod = mod;
        this.log = new Log(mod.id(), className());
        this.enabled = enabledByDefault();
    }

    public Mod mod() {
        return mod;
    }

    /**
     * Returns a pretty-format version of the feature name.
     * For example, "CharmonySettings" becomes "Charmony settings".
     * Use className() for the pascal-case feature name.
     * @return Pretty-format feature name.
     */
    public String name() {
        var name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className());
        var first = name.substring(0, 1);
        return first.toUpperCase() + name.replace("_", " ").substring(1);
    }

    /**
     * Pascal-case feature name.
     * @return Pascal-case feature name.
     */
    public String className() {
        return this.getClass().getSimpleName();
    }

    public String description() {
        return annotation().description();
    }

    public Log log() {
        return log;
    }

    public Config config() {
        return mod().config();
    }

    public boolean enabled() {
        return enabled;
    }

    public void enabled(boolean flag) {
        this.enabled = flag;
    }

    public boolean enabledByDefault() {
        return annotation().enabledByDefault();
    }

    public boolean canBeDisabled() {
        return annotation().canBeDisabled();
    }

    public boolean showInConfig() {
        return annotation().showInConfig();
    }

    public void run() {
        // no op
    }

    public Side side() {
        return annotation().side();
    }

    private FeatureDefinition annotation() {
        var annotation = getClass().getAnnotation(FeatureDefinition.class);
        if (annotation == null) {
            throw new RuntimeException("Feature " + getClass() + " is missing annotation");
        }
        return annotation;
    }
}
