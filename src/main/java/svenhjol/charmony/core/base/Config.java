package svenhjol.charmony.core.base;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.enums.Side;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Config {
    private final Mod mod;
    private final Log log;
    private final Map<Field, Object> defaultFieldValues = new HashMap<>();
    private final List<Class<? extends Feature>> populatedFeatures = new ArrayList<>();

    public Config(Mod mod) {
        this.mod = mod;
        this.log = new Log(mod.id(), "Config");
    }

    public void populate() {
        var composites = mod.composites();

        // Initialize a toml object for each side.
        Map<Side, Toml> configs = new HashMap<>();
        for (var side : Side.values()) {
            var toml = new Toml();
            var file = configPath(side).toFile();
            if (file.exists()) {
                toml = toml.read(file);
            }
            configs.put(side, toml);
        }

        for (var composite : composites) {
            for (var feature : composite.all()) {
                var side = feature.side();
                var toml = configs.get(side);
                var featurePath = feature.className() + ".Enabled";
                if (toml.contains(featurePath) && feature.canBeDisabled()) {
                    feature.enabled(toml.getBoolean(featurePath));
                }

                // Process fields in the feature that have the @Config annotation.
                var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
                for (var field : fields) {
                    try {
                        // Ignore fields that don't have the Config annotation.
                        var annotation = field.getDeclaredAnnotation(Configurable.class);
                        if (annotation == null) continue;

                        // Set the static property as writable so that the config can modify it
                        field.setAccessible(true);
                        var fieldName = annotation.name();

                        if (fieldName.isEmpty()) {
                            fieldName = field.getName();
                        }

                        Object fieldValue = field.get(null);
                        Object configValue;

                        if (!populatedFeatures.contains(feature.getClass())) {
                            defaultFieldValues.put(field, fieldValue);
                        }

                        // Get the config values that were set in the toml file and apply them to the feature objects.
                        if (toml.contains(feature.className())) {

                            // Get the set of key/value pairs for this feature.
                            var featureKeys = toml.getTable(feature.className());
                            Map<String, Object> mappedKeys = new HashMap<>();

                            // Key names sometimes have quotes, map to remove them.
                            featureKeys.toMap().forEach((k, v) -> mappedKeys.put(k.replace("\"", ""), v));
                            configValue = mappedKeys.get(fieldName);

                            if (configValue != null) {
                                // There's some weirdness with casting, deal with that here.
                                if (fieldValue instanceof Integer && configValue instanceof Double) {
                                    configValue = (int) (double) configValue;
                                }

                                if (fieldValue instanceof Integer && configValue instanceof Long) {
                                    configValue = (int) (long) configValue;
                                }

                                if (fieldValue instanceof Float && configValue instanceof Double) {
                                    configValue = (float) (double) configValue;
                                }

                                // Set the class property.
                                log.debug("In feature " + feature.className() + ": setting `" + fieldName + "` to `" + configValue + "`");
                                field.set(null, configValue);
                            }
                        }

                    } catch (Exception e) {
                        log.warn("Failed to read config field in feature " + feature.className() + ": " + e.getMessage());
                    }
                }

                if (!populatedFeatures.contains(feature.getClass())) {
                    populatedFeatures.add(feature.getClass());
                }
            }
        }
    }

    public void write() {
        // Blank config is appended and then written out. LinkedHashMap supplier sorts contents alphabetically.
        Map<Side, CommentedConfig> configs = new HashMap<>();
        var composites = mod.composites();

        for (var composite : composites) {
            for (var feature : composite.all()) {
                var config = configs.computeIfAbsent(feature.side(), o -> TomlFormat.newConfig(LinkedHashMap::new));

                if (feature.canBeDisabled()) {
                    var field = "Enabled";
                    var description = feature.description();
                    var configName = feature.className() + "." + field;

                    config.setComment(configName, description);
                    config.add(configName, feature.enabled());
                }

                // Get config values and write them into the toml object.
                var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
                for (var field : fields) {
                    try {
                        var annotation = field.getDeclaredAnnotation(Configurable.class);
                        if (annotation == null) continue;

                        var fieldName = annotation.name();
                        var fieldDescription = annotation.description();
                        field.setAccessible(true);
                        Object fieldValue = field.get(null);

                        // Set the key/value pair. The "." specifies that it is nested
                        var featureConfigName = feature.className() + "." + fieldName;
                        config.setComment(featureConfigName, fieldDescription);
                        config.add(featureConfigName, fieldValue);

                    } catch (Exception e) {
                        log.error("Failed to write config property `" + field.getName() + "` in `" + feature.className() + "`: " + e.getMessage());
                    }
                }
            }
        }

        configs.forEach((side, config) -> {
            if (!config.isEmpty()) {
                var path = configPath(side);

                try {
                    // Write out and close the file.
                    var writer = new TomlWriter();
                    var buffer = Files.newBufferedWriter(path);

                    writer.write(config, buffer);
                    buffer.close();
                    log.debug( "Written config to disk");
                } catch (Exception e) {
                    log.error( "Failed to write config: " + e.getMessage());
                }
            }
        });
    }

    public boolean hasConfiguration(CompositeFeature composite) {
        for (var feature : composite.all()) {
            var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
            if (fields.stream().anyMatch(f -> f.getDeclaredAnnotation(Configurable.class) != null)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDefaultValues(CompositeFeature composite) {
        for (var feature : composite.all()) {
            var fields = new ArrayList<>(Arrays.asList(feature.getClass().getDeclaredFields()));
            for (var field : fields) {
                try {
                    var annotation = field.getDeclaredAnnotation(Configurable.class);
                    if (annotation == null) continue;

                    field.setAccessible(true);
                    var val = field.get(null);
                    var defaultVal = defaultValue(field);
                    if (defaultVal.isPresent() && !val.equals(defaultVal.get())) {
                        return false;
                    }
                } catch (Exception e) {
                    log.warn("Failed to read config field in feature " + feature.className() + ": " + e.getMessage());
                }
            }
        }

        return true;
    }

    public Optional<Object> defaultValue(Field field) {
        return Optional.ofNullable(defaultFieldValues.get(field));
    }

    private Path configPath(Side side) {
        return Paths.get(FabricLoader.getInstance().getConfigDir() + "/" + mod.id() + "-" + side.getSerializedName() + ".toml");
    }
}
