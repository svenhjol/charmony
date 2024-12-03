package svenhjol.charmony.core.base;

import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.annotations.ModDefinition;
import svenhjol.charmony.core.enums.Side;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public abstract class Mod {
    private static final Map<String, Mod> REGISTERED = new HashMap<>();

    private final Log log;
    private final Config config;
    private final Map<String, Feature> features = new HashMap<>();
    private final Map<Class<? extends SidedFeature>, SidedFeature> classFeatures = new HashMap<>();
    private final Map<Side, LinkedList<Class<? extends SidedFeature>>> classes = new LinkedHashMap<>();
    private final Map<Side, Map<SidedFeature, List<Runnable>>> boots = new HashMap<>();
    private final Map<Side, Map<SidedFeature, List<BooleanSupplier>>> checks = new HashMap<>();

    public Mod() {
        this.config = new Config(this);
        this.log = new Log(id(), "Mod");
        REGISTERED.put(id(), this);
    }

    public static Optional<Mod> get(String id) {
        return Optional.ofNullable(REGISTERED.get(id));
    }

    public static LinkedList<Mod> all() {
        var values = new LinkedList<>(REGISTERED.values());
        values.sort(Comparator.comparing(Mod::name));
        return values;
    }

    public void run(Side side) {
        var sideName = side.getSerializedName();
        var classes = this.classes.computeIfAbsent(side, l -> new LinkedList<>());
        var classCount = classes.size();

        if (Environment.isClientMode() && !side.equals(Side.Client)) {
            log.warn("Client mode is active, skipping " + sideName + " features");
            return;
        }

        if (classCount == 0) {
            log.info("No " + sideName + " features to set up for " + name() + ", skipping");
            return;
        }

        log.info("Setting up " + classCount + " " + sideName + " feature(s) for " + name());
        classes.sort(Comparator.comparing(c -> c.getAnnotation(FeatureDefinition.class).priority()));

        for (var clazz : classes) {
            SidedFeature sidedFeature;
            try {
                sidedFeature = clazz.getDeclaredConstructor(Mod.class).newInstance(this);
                classFeatures.put(clazz, sidedFeature);
                features.computeIfAbsent(sidedFeature.className(), c -> new Feature(this)).put(side, sidedFeature);

            } catch (Exception e) {
                String message = "Failed to instantiate feature " + clazz + " for mod " + name() + ": " + e.getMessage();
                if (Environment.isDebugMode()) {
                    throw new RuntimeException(message, e);
                } else {
                    throw new RuntimeException(message);
                }
            }
        }

        log().info("Configuring " + name() + " " + sideName);
        config.populate();
        config.write();

        log().info("Checking " + name() + " " + sideName);
        var checks = this.checks.computeIfAbsent(side, m -> new HashMap<>());
        checks.forEach((feature, check) ->
            feature.enabled(check.stream().allMatch(BooleanSupplier::getAsBoolean)));

        log().info("Booting up " + name() + " " + sideName);
        var boots = this.boots.computeIfAbsent(side, m -> new HashMap<>());
        boots.forEach((feature, boot) -> {
            if (feature.enabled()) {
                boot.forEach(Runnable::run);
            }
        });

        log().info("Running " + sideName + " features for " + name());
        features.forEach((name, feature) -> {
            var featureName = feature.className();
            var sided = feature.get(side).orElse(null);
            if (sided == null) return;

            if (feature.enabled()) {
                log().info("✔ Running " + sideName  + " feature " + featureName);
                sided.run();
            } else {
                log().info("✖ Not running " + sideName + " feature " + featureName);
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

    public <F extends SidedFeature> F feature(Class<F> clazz) {
        return tryFeature(clazz).orElseThrow(() -> new RuntimeException("Could not resolve feature for class " + clazz));
    }

    @SuppressWarnings("unchecked")
    public <F extends SidedFeature> Optional<F> tryFeature(Class<F> clazz) {
        F resolved = (F) classFeatures.get(clazz);
        return Optional.ofNullable(resolved);
    }

    public void addFeature(Class<? extends SidedFeature> clazz) {
        var side = clazz.getAnnotation(FeatureDefinition.class).side();
        classes.computeIfAbsent(side, a -> new LinkedList<>()).add(clazz);
    }

    public void addCheckStep(SidedFeature feature, BooleanSupplier check) {
        checks.computeIfAbsent(feature.side(), m -> new HashMap<>())
            .computeIfAbsent(feature, a -> new ArrayList<>()).add(check);
    }

    public void addBootStep(SidedFeature feature, Runnable step) {
        boots.computeIfAbsent(feature.side(), m -> new HashMap<>())
            .computeIfAbsent(feature, a -> new ArrayList<>()).add(step);
    }

    public LinkedList<Feature> features() {
        var values = new LinkedList<>(features.values());
        values.sort(Comparator.comparing(Feature::name));
        return values;
    }

    public LinkedList<SidedFeature> featuresForSide(Side side) {
        return features.values().stream()
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
