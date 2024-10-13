package svenhjol.charmony.core.base;

import svenhjol.charmony.core.enums.Side;

import java.util.*;

/**
 * Holder for client, common and server feature instances.
 * Use get(side) to get a sided feature and all() for all instances held in the composite.
 */
public final class CompositeFeature {
    private final Log log;
    private final Mod mod;
    private final Map<Side, Feature> features = new HashMap<>();

    public CompositeFeature(Mod mod) {
        this.mod = mod;
        this.log = mod.log();
    }

    public void enabled(boolean state) {
        features.values().forEach(f -> f.enabled(state));
    }

    public boolean enabled() {
        return features.values().stream().anyMatch(Feature::enabled);
    }

    public boolean enabledByDefault() {
        return features.values().stream().anyMatch(Feature::enabledByDefault);
    }

    public boolean canBeDisabled() {
        return features.values().stream().anyMatch(Feature::canBeDisabled);
    }

    public Mod mod() {
        return this.mod;
    }

    public Log log() {
        return log;
    }

    public Config config() {
        return mod().config();
    }

    /**
     * Pretty-format feature name.
     * Use className() for the pascal-case feature name.
     * @return Pretty-format feature name.
     */
    public String name() {
        return getFirst().name();
    }

    /**
     * Pascal-case feature name.
     * @return Pascal-case feature name.
     */
    public String className() {
        return getFirst().className();
    }

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
