package svenhjol.charmony.scaffold.base;

import svenhjol.charmony.scaffold.annotations.FeatureDefinition;
import svenhjol.charmony.scaffold.enums.Side;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class Mod {
    private static final Map<String, Mod> REGISTERED = new HashMap<>();

    private final String id;
    private final Log log;
    private final Map<String, CompositeFeature> composites = new HashMap<>();
    private final Map<Class<? extends Feature>, Feature> classFeatures = new HashMap<>();
    private final Map<Side, LinkedList<Class<? extends Feature>>> classes = new LinkedHashMap<>();
    private final Map<Side, Map<Feature, List<Runnable>>> boots = new HashMap<>();
    private final Map<Side, Config> configs = new HashMap<>();

    public Mod(String id) {
        this.id = id;
        this.log = new Log(id(), name());
        REGISTERED.put(id, this);
    }

    public static Optional<Mod> get(String id) {
        return Optional.ofNullable(REGISTERED.get(id));
    }

    public static List<Mod> all() {
        return new ArrayList<>(REGISTERED.values());
    }

    public void run(Side side) {
        var sideName = side.getSerializedName();
        var classes = this.classes.computeIfAbsent(side, l -> new LinkedList<>());
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
                classFeatures.put(clazz, feature);
                composites.computeIfAbsent(feature.name(), c -> new CompositeFeature(this)).put(side, feature);

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
        composites.forEach((name, composite) -> {
            var featureName = composite.name();
            if (composite.enabled()) {
                log().info("✔ Running feature " + featureName);
                composite.get(side).run();
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
        F resolved = (F) classFeatures.get(clazz);
        return Optional.ofNullable(resolved);
    }

    public void addFeature(Class<? extends Feature> clazz) {
        var side = clazz.getAnnotation(FeatureDefinition.class).side();
        classes.computeIfAbsent(side, a -> new LinkedList<>()).add(clazz);
    }

    public void addBootStep(Feature feature, Runnable step) {
        boots.computeIfAbsent(feature.side(), m -> new HashMap<>()).computeIfAbsent(feature, a -> new ArrayList<>()).add(step);
    }

    public List<CompositeFeature> features() {
        return new ArrayList<>(composites.values());
    }

    public LinkedList<Feature> featuresForSide(Side side) {
        return composites.values().stream()
            .map(c -> c.get(side))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toCollection(LinkedList::new));
    }
}
