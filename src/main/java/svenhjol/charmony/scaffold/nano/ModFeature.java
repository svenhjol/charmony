package svenhjol.charmony.scaffold.nano;

import svenhjol.charmony.scaffold.nano.annotations.Feature;
import svenhjol.charmony.scaffold.nano.enums.Side;

public abstract class ModFeature {
    private final Mod mod;
    private final Log log;

    private boolean enabled;

    public ModFeature(Mod mod) {
        this.mod = mod;
        this.log = new Log(mod.id(), name());
        this.enabled = enabledByDefault();
    }

    public Mod mod() {
        return mod;
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public String description() {
        return annotation().description();
    }

    public Log log() {
        return log;
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

    public void run() {
        // no op
    }

    public Side side() {
        return annotation().side();
    }

    private Feature annotation() {
        var annotation = getClass().getAnnotation(svenhjol.charmony.scaffold.nano.annotations.Feature.class);
        if (annotation == null) {
            throw new RuntimeException("Feature " + getClass() + " is missing annotation");
        }
        return annotation;
    }
}
