package svenhjol.charmony.core.base;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.annotations.ModDefinition;
import svenhjol.charmony.core.enums.Side;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class Mod {
    private static final Map<String, Mod> REGISTERED = new HashMap<>();

    private final Log log;
    private final Config config;
    private final Map<String, CompositeFeature> composites = new HashMap<>();
    private final Map<Class<? extends Feature>, Feature> classFeatures = new HashMap<>();
    private final Map<Side, LinkedList<Class<? extends Feature>>> classes = new LinkedHashMap<>();
    private final Map<Side, Map<Feature, List<Runnable>>> boots = new HashMap<>();

    public Mod() {
        this.config = new Config(this);
        this.log = new Log(id(), name());
        REGISTERED.put(id(), this);
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
                composites.computeIfAbsent(feature.className(), c -> new CompositeFeature(this)).put(side, feature);

            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate feature " + clazz + " for mod " + name() + ": " + e.getMessage());
            }
        }

        log().info("Configuring " + name() + " " + sideName);
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
            var featureName = composite.className();
            var feature = composite.get(side).orElse(null);
            if (feature != null && feature.enabled()) {
                log().info("✔ Running feature " + featureName);
                feature.run();
            } else {
                log().info("✖ Not running feature " + featureName);
            }
        });
    }

    public String id() {
        return annotation().id();
    }

    public String description() {
        return annotation().description();
    }

    /**
     * Pretty-format mod name.
     * This is derived from the mod's annotation.
     * @return Prett-format mod name.
     */
    public String name() {
        return annotation().name();
    }

    public List<Side> sides() {
        return Arrays.asList(annotation().sides());
    }

    /**
     * Pascal-case mod name.
     * @return Pascal-case mod name.
     */
    public String className() {
        return this.getClass().getSimpleName();
    }

    public Log log() {
        return this.log;
    }

    public Config config() {
        return this.config;
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

    public List<CompositeFeature> composites() {
        return new ArrayList<>(composites.values());
    }

    public LinkedList<Feature> featuresForSide(Side side) {
        return composites.values().stream()
            .map(c -> c.get(side))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private ModDefinition annotation() {
        var annotation = getClass().getAnnotation(ModDefinition.class);
        if (annotation == null) {
            throw new RuntimeException("Mod " + getClass() + " is missing annotation");
        }
        return annotation;
    }
}
