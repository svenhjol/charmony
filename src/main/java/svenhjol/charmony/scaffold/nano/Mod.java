package svenhjol.charmony.scaffold.nano;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.scaffold.nano.enums.Side;

import java.util.*;

public abstract class Mod {
    private final Log log;
    private final Config config;
    private final Map<String, Map<Side, List<ModFeature>>> features = new HashMap<>();
    private final Map<Class<? extends ModFeature>, ModFeature> classFeatures = new HashMap<>();
    private final Map<Side, List<Runnable>> setups = new HashMap<>();

    public Mod() {
        this.log = new Log(id(), name());
        this.config = new Config(this);
    }

    public void run(Side side) {
        log.info("Setup stage");
        var sidedSetups = setups
            .getOrDefault(side, List.of());
        sidedSetups.forEach(Runnable::run);

        log.info("Run stage");
        var sidedFeatures = features
            .getOrDefault(id(), new HashMap<>())
            .getOrDefault(side, List.of());

        sidedFeatures.forEach(feature -> {
            feature.log().info("Running feature " + feature.name());
            feature.run();
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

    public void addFeature(ModFeature feature) {
        var modId = feature.mod().id();
        var side = feature.side();
        var clazz = feature.getClass();

        if (!features.containsKey(modId)) {
            features.put(modId, new HashMap<>());
        }
        var map = features.get(modId);
        map.computeIfAbsent(side, a -> new ArrayList<>()).add(feature);

        classFeatures.put(clazz, feature);
    }

    public void addSetup(Side side, Runnable setup) {
        setups.computeIfAbsent(side, a -> new ArrayList<>()).add(setup);
    }

    public Map<String, Map<Side, List<ModFeature>>> features() {
        return features;
    }
}
