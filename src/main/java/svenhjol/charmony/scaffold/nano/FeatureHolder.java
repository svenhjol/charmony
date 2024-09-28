package svenhjol.charmony.scaffold.nano;

public abstract class FeatureHolder<F extends ModFeature> {
    private final F feature;

    public FeatureHolder(F feature) {
        this.feature = feature;
    }

    public F feature() {
        return this.feature;
    }

    public Log log() {
        return this.feature.log();
    }
}
