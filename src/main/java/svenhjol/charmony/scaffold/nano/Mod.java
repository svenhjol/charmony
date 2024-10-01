package svenhjol.charmony.scaffold.nano;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.scaffold.nano.enums.Side;

import java.util.*;

public abstract class Mod {
    private final Log log;
    private final Config config;
    private final Map<Side, List<ModFeature>> features = new HashMap<>();
    private final Map<Class<? extends ModFeature>, ModFeature> classFeatures = new HashMap<>();
    private final Map<Side, List<Runnable>> registers = new HashMap<>();
    private final Map<Side, Map<ModFeature, List<Runnable>>> boots = new HashMap<>();

    public Mod() {
        this.log = new Log(id(), name());
        this.config = new Config(this);
    }

    public void run(Side side) {
        var sideName = side.getSerializedName();
        var registers = this.registers.getOrDefault(side, List.of());
        var boots = this.boots.getOrDefault(side, new HashMap<>());
        var features = this.features.getOrDefault(side, List.of());

        log().info("Configuring " + name() + " " + sideName);
        config.populateFromDisk(features);
        config.writeToDisk(features);

        log().info("Registering " + name() + " " + sideName);
        registers.forEach(Runnable::run);

        log().info("Booting up " + name() + " " + sideName);
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

    public abstract String id();

    public ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(id(), path);
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public Log log() {
        return this.log;
    }

    public <F extends ModFeature> F feature(Class<F> clazz) {
        return tryFeature(clazz).orElseThrow(() -> new RuntimeException("Could not resolve feature for class " + clazz));
    }

    @SuppressWarnings("unchecked")
    public <F extends ModFeature> Optional<F> tryFeature(Class<F> clazz) {
        F resolved = (F) classFeatures.get(clazz);
        return Optional.ofNullable(resolved);
    }

    public void addFeature(Class<? extends ModFeature> clazz) {
        ModFeature feature;
        try {
            feature = clazz.getDeclaredConstructor(Mod.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate feature " + clazz + " for mod " + name() + ": " + e.getMessage());
        }

        var side = feature.side();
        features.computeIfAbsent(side, a -> new ArrayList<>()).add(feature);
        classFeatures.put(clazz, feature);
    }

    public void addBootStep(ModFeature feature, Runnable step) {
        boots.computeIfAbsent(feature.side(), m -> new HashMap<>()).computeIfAbsent(feature, a -> new ArrayList<>()).add(step);
    }

    public void addRegisterStep(ModFeature feature, Runnable step) {
        registers.computeIfAbsent(feature.side(), a -> new ArrayList<>()).add(step);
    }

    public Map<Side, List<ModFeature>> features() {
        return features;
    }
}
