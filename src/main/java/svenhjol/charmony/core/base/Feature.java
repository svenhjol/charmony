package svenhjol.charmony.core.base;

import svenhjol.charmony.core.enums.Side;

import java.util.*;

/**
 * Holder for sidedFeature instances.
 * Use get(side) to get a specific sided feature and sides() for all sided features.
 */
public final class Feature {
    private final Log log;
    private final Mod mod;
    private final Map<Side, SidedFeature> features = new HashMap<>();

    public Feature(Mod mod) {
        this.mod = mod;
        this.log = mod.log();
    }

    public void enabled(boolean state) {
        features.values().forEach(f -> f.enabled(state));
    }

    public boolean enabled() {
        return features.values().stream().anyMatch(SidedFeature::enabled);
    }

    public boolean enabledByDefault() {
        return features.values().stream().anyMatch(SidedFeature::enabledByDefault);
    }

    public boolean canBeDisabled() {
        return features.values().stream().anyMatch(SidedFeature::canBeDisabled);
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

    public void put(Side side, SidedFeature sidedFeature) {
        features.put(side, sidedFeature);
    }

    public Optional<SidedFeature> get(Side side) {
        return Optional.ofNullable(features.get(side));
    }

    public SidedFeature getFirst() {
        var first = sides().getFirst();
        if (first == null) {
            throw new RuntimeException("No sided features in this feature.");
        }
        return first;
    }

    public List<SidedFeature> sides() {
        return new ArrayList<>(features.values());
    }

    public Optional<SidedFeature> client() {
        return get(Side.Client);
    }

    public Optional<SidedFeature> common() {
        return get(Side.Common);
    }

    public Optional<SidedFeature> server() {
        return get(Side.Server);
    }
}
