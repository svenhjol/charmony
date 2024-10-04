package svenhjol.charmony.scaffold.base;

import com.electronwill.nightconfig.toml.TomlFormat;
import com.electronwill.nightconfig.toml.TomlWriter;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charmony.scaffold.annotations.Configurable;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class Config {
    private final Mod mod;
    private final Log log;
    private final Map<Field, Object> defaultFieldValues = new HashMap<>();
    private final List<Class<? extends ModFeature>> populatedFeatures = new ArrayList<>();

    public Config(Mod mod) {
        this.mod = mod;
        this.log = new Log(mod.id(), "Config");
    }

    public void populateFromDisk(List<? extends ModFeature> featureSet) {
        var toml = new Toml();
        var path = configPath();
        var file = path.toFile();
        var features = new LinkedList<>(featureSet);

        if (file.exists()) {
            toml = toml.read(file);
        }

        for (var feature : features) {
            var featurePath = feature.name() + ".Enabled";
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
                    if (toml.contains(feature.name())) {

                        // Get the set of key/value pairs for this feature.
                        var featureKeys = toml.getTable(feature.name());
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
                            log.debug("In feature " + feature.name() + ": setting `" + fieldName + "` to `" + configValue + "`");
                            field.set(null, configValue);
                        }
                    }

                } catch (Exception e) {
                    log.warn("Failed to read config field in feature " + feature.name() + ": " + e.getMessage());
                }
            }

            if (!populatedFeatures.contains(feature.getClass())) {
                populatedFeatures.add(feature.getClass());
            }
        }
    }

    public void writeToDisk(List<? extends ModFeature> featureSet) {
        // Blank config is appended and then written out. LinkedHashMap supplier sorts contents alphabetically.
        var config = TomlFormat.newConfig(LinkedHashMap::new);
        var features = new LinkedList<>(featureSet);

        // Sort alphabetically.
        features.sort(Comparator.comparing(ModFeature::name));

        for (var feature : features) {
            if (feature.canBeDisabled()) {
                var field = "Enabled";
                var description = feature.description();
                var configName = feature.name() + "." + field;

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
                    var featureConfigName = feature.name() + "." + fieldName;
                    config.setComment(featureConfigName, fieldDescription);
                    config.add(featureConfigName, fieldValue);

                } catch (Exception e) {
                    log.error("Failed to write config property `" + field.getName() + "` in `" + feature.name() + "`: " + e.getMessage());
                }
            }
        }

        if (!config.isEmpty()) {
            var path = configPath();

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
    }

    public boolean hasDefaultValues(ModFeature feature) {
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
                log.warn("Failed to read config field in feature " + feature.name() + ": " + e.getMessage());
            }
        }

        return true;
    }

    public Optional<Object> defaultValue(Field field) {
        return Optional.ofNullable(defaultFieldValues.get(field));
    }

    private Path configPath() {
        return Paths.get(FabricLoader.getInstance().getConfigDir() + "/" + mod.id() + ".toml");
    }
}
