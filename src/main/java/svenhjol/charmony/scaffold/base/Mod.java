package svenhjol.charmony.scaffold.base;

import svenhjol.charmony.scaffold.annotations.FeatureDefinition;
import svenhjol.charmony.scaffold.enums.Side;

import java.util.*;

@SuppressWarnings("unused")
public abstract class Mod {
    private final String id;
    private final Log log;
    private final Map<Class<? extends Feature>, Feature> featureForClass = new HashMap<>();
    private final Map<Side, LinkedList<Class<? extends Feature>>> sidedClasses = new LinkedHashMap<>();
    private final Map<Side, LinkedList<Feature>> sidedFeatures = new LinkedHashMap<>();
    private final Map<Side, Map<Feature, List<Runnable>>> boots = new HashMap<>();
    private final Map<Side, Config> configs = new HashMap<>();

    public Mod(String id) {
        this.id = id;
        this.log = new Log(id(), name());
    }

    public void run(Side side) {
        var sideName = side.getSerializedName();
        var classes = this.sidedClasses.computeIfAbsent(side, l -> new LinkedList<>());
        var features = this.sidedFeatures.computeIfAbsent(side, l -> new LinkedList<>());
        var classCount = classes.size();

        if (classCount == 0) {
            log.info("No " + sideName + " features to set up for " + name() + ", skipping");
            return;
        }

        log.info("Setting up " + classCount + " " + sideName + " feature(s) for " + name());
        classes.sort(Comparator.comparing(c -> c.getAnnotation(FeatureDefinition.class).priority()));

        for (var clazz : classes) {
            Feature feature;
            try {
                feature = clazz.getDeclaredConstructor(Mod.class).newInstance(this);
                featureForClass.put(clazz, feature);
                features.add(feature);
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate feature " + clazz + " for mod " + name() + ": " + e.getMessage());
            }
        }

        log().info("Configuring " + name() + " " + sideName);
        var config = configs.computeIfAbsent(side, o -> new Config(this, side));
        config.populate();
        config.write();

        log().info("Booting up " + name() + " " + sideName);
        var boots = this.boots.computeIfAbsent(side, m -> new HashMap<>());
        boots.forEach((feature, boot) -> {
            if (feature.enabled()) {
                boot.forEach(Runnable::run);
            }
        });

        log().info("Running " + sideName + " features for " + name());
        features.forEach(feature -> {
            var featureName = feature.name();
            if (feature.enabled()) {
                log().info("✔ Running feature " + featureName);
                feature.run();
            } else {
                log().info("✖ Not running feature " + featureName);
            }
        });
    }

    public String id() {
        return this.id;
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public Log log() {
        return this.log;
    }

    public Optional<Config> config(Side side) {
        return Optional.ofNullable(configs.get(side));
    }

    public <F extends Feature> F feature(Class<F> clazz) {
        return tryFeature(clazz).orElseThrow(() -> new RuntimeException("Could not resolve feature for class " + clazz));
    }

    @SuppressWarnings("unchecked")
    public <F extends Feature> Optional<F> tryFeature(Class<F> clazz) {
        F resolved = (F) featureForClass.get(clazz);
        return Optional.ofNullable(resolved);
    }

    public void addFeature(Class<? extends Feature> clazz) {
        var side = clazz.getAnnotation(FeatureDefinition.class).side();
        sidedClasses.computeIfAbsent(side, a -> new LinkedList<>()).add(clazz);
    }

    public void addBootStep(Feature feature, Runnable step) {
        boots.computeIfAbsent(feature.side(), m -> new HashMap<>()).computeIfAbsent(feature, a -> new ArrayList<>()).add(step);
    }

    public Map<Side, LinkedList<Feature>> features() {
        return sidedFeatures;
    }
}
