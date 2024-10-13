package svenhjol.charmony.core.base;


public abstract class Setup<F extends Feature> {
    private final F feature;

    public Setup(F feature) {
        this.feature = feature;
        this.feature.mod().addBootStep(feature, boot());
    }

    public F feature() {
        return this.feature;
    }

    public Log log() {
        return this.feature.log();
    }

    public Runnable boot() {
        return () -> {};
    }
}
