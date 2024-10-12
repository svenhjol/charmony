package svenhjol.charmony.scaffold.base;

import svenhjol.charmony.scaffold.enums.Side;

import java.util.*;

public final class CompositeFeature implements IFeature {
    private final Log log;
    private final Mod mod;
    private final Map<Side, Feature> features = new HashMap<>();

    public CompositeFeature(Mod mod) {
        this.mod = mod;
        this.log = mod.log();
    }

    @Override
    public void enabled(boolean state) {
        features.values().forEach(f -> f.enabled(state));
    }

    @Override
    public boolean enabled() {
        return features.values().stream().anyMatch(Feature::enabled);
    }

    @Override
    public boolean enabledByDefault() {
        return features.values().stream().anyMatch(Feature::enabledByDefault);
    }

    @Override
    public boolean canBeDisabled() {
        return features.values().stream().anyMatch(Feature::canBeDisabled);
    }

    @Override
    public Mod mod() {
        return this.mod;
    }

    @Override
    public Log log() {
        return log;
    }

    @Override
    public String name() {
        return getFirst().name();
    }

    @Override
    public String description() {
        return getFirst().description();
    }

    public void put(Side side, Feature feature) {
        features.put(side, feature);
    }

    public Optional<Feature> get(Side side) {
        return Optional.ofNullable(features.get(side));
    }

    public Feature getFirst() {
        var first = all().getFirst();
        if (first == null) {
            throw new RuntimeException("No features in this composite");
        }
        return first;
    }

    public List<Feature> all() {
        return new ArrayList<>(features.values());
    }

    public Optional<Feature> client() {
        return get(Side.Client);
    }

    public Optional<Feature> common() {
        return get(Side.Common);
    }

    public Optional<Feature> server() {
        return get(Side.Server);
    }
}
