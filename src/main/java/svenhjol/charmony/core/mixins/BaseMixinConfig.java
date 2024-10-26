package svenhjol.charmony.core.mixins;

import com.google.common.base.CaseFormat;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Environment;
import svenhjol.charmony.core.enums.Side;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings({"unused", "OptionalUsedAsFieldOrParameterType"})
public abstract class BaseMixinConfig implements IMixinConfigPlugin {
    protected static final String FEATURES = "features";
    protected static final String ACCESSORS = "accesors";
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");

    protected final List<String> blacklist = new ArrayList<>();
    protected String mixinPackage;
    protected Optional<Boolean> clientMode = Optional.empty();
    protected Optional<Boolean> debugMode = Optional.empty();
    protected Optional<Boolean> mixinDisableMode = Optional.empty();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        // TODO: blacklist handling from config file.
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        var mixinSimpleName = mixinClassName.substring(mixinPackage.length() + 1);
        var split = mixinSimpleName.split("\\.");
        var mixinBaseName = split[0];
        var featureName = "";
        var mixinIsFeature = mixinBaseName.equals(FEATURES);

        if (mixinIsFeature) {
            var builder = new StringBuilder();
            for (int i = 1; i < split.length; i++) {
                if (!split[i].contains("Mixin")) {
                    builder.append(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, (split[i])));
                    builder.append(".");
                }
            }

            featureName = builder.toString();
            if (featureName.endsWith(".")) {
                featureName = featureName.substring(0, featureName.length() - 1);
            }

            if (featureName.isEmpty()) {
                LOGGER.warn("Could not resolve feature name from mixin base name {}", mixinBaseName);
                return false;
            }
        }

        // Allow the subclass to check if this mixin base is permitted.
        if (!allowBaseName(mixinBaseName, mixinClassName)) {
            LOGGER.warn("✖ Mixin {} is not allowed", mixinClassName);
            return false;
        }

        // All mixins other than accessors are disabled when mixin disable mode is active.
        if (isMixinDisableMode() && !mixinBaseName.equals(ACCESSORS)) {
            LOGGER.warn("✖ Mixin disable mode is active, not loading {}", mixinClassName);
            return false;
        }

        for (var predicate : runtimeBlacklist()) {
            if (mixinIsFeature && predicate.test(featureName)) {
                blacklist.add(mixinSimpleName);
            }
        }

        if (blacklist.contains(mixinSimpleName)) {
            LOGGER.warn("✖ Mixin {} is blacklisted", mixinSimpleName);
            return false;
        }

        var valid = !mixinIsFeature || enabledInConfig(featureName);
        if (valid) {
            LOGGER.info("✔ Enabled mixin {}", mixinClassName);
        } else {
            LOGGER.warn("✖ Disabled mixin {}", mixinClassName);
        }
        return valid;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        // no op
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // no op
    }

    /**
     * Let the subclass check if the baseName (the first bit of the path i.e. accessor, feature) is allowed.
     * @param baseName The first bit of the path i.e. accessor, feature
     * @param mixinClassName The mixin classname for reference.
     * @return True if the baseName is allowed.
     */
    protected boolean allowBaseName(String baseName, String mixinClassName) {
        return true;
    }

    /**
     * ID of the mod that extends this base config processor.
     * @return ID of the mod.
     */
    protected abstract String modId();

    /**
     * Classpath to where the "client", "common" and "server" folders are kept.
     * The subclass must define this because it differs per mod.
     * @return Classpath to the client/common/server folders.
     */
    protected abstract String rootClassPath();

    /**
     * Hook to allow the subclass to specify mixins that should not be included.
     * @return List of predicates to test the currently loading mixin against.
     */
    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of();
    }

    /**
     * Read the "mixin disable mode" value from the charmony config file as early as possible.
     * @return The value of mixin disable mode.
     */
    protected boolean isMixinDisableMode() {
        if (mixinDisableMode.isEmpty()) {
            mixinDisableMode = tryReadFromCoreConfig(Side.Common, Environment.MIXIN_DISABLE_MODE);
        }
        return mixinDisableMode.orElse(false);
    }

    /**
     * Read the "client mode" value from the charmony config file as early as possible.
     * @return The value of client mode.
     */
    protected boolean isClientMode() {
        if (clientMode.isEmpty()) {
            clientMode = tryReadFromCoreConfig(Side.Common, Environment.CLIENT_MODE);
        }
        return clientMode.orElse(false);
    }

    /**
     * Read the "debug mode" value from the charmony config file as early as possible.
     * @return The value of debug mode.
     */
    protected boolean isDebugMode() {
        if (debugMode.isEmpty()) {
            debugMode = tryReadFromCoreConfig(Side.Common, Environment.DEBUG_MODE);
        }
        return debugMode.orElse(false);
    }

    protected boolean enabledInConfig(String featureName) {
        FeatureDefinition feature = null;

        if (featureName.contains(".")) {
            var split = featureName.split("\\.");
            if (split.length > 2) {
                LOGGER.error("Feature name can't be parsed. Expected 2 fragments, received {}", split.length);
                return false;
            }
            featureName = split[0];
        }

        for (var side : Side.values()) {
            try {
                var clazz = Class.forName(rootClassPath() + "." +
                    side.name().toLowerCase(Locale.ROOT) + "." +
                    CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, featureName) + "." +
                    featureName);
                feature = clazz.getAnnotation(FeatureDefinition.class);
                break;
            } catch (ClassNotFoundException e) {
                continue;
            }
        }

        if (feature == null) {
            LOGGER.warn("Mixin looking for feature {} but it doesn't exist in the mod", featureName);
            return false;
        }

        if (!feature.canBeDisabled()) {
            return false;
        }

        for (var side : Side.values()) {
            var configFile = getConfigFile(side);

            if (!configFile.exists()) {
                continue;
            }

            // Read the value from the config file.
            var handle = new Toml();
            var toml = handle.read(configFile);
            var enabledPath = featureName + ".Enabled";

            if (toml.contains(enabledPath)) {
                return toml.getBoolean(enabledPath);
            }
        }

        return feature.enabledByDefault();
    }

    private Optional<Boolean> tryReadFromCoreConfig(Side side, String key) {
        var configFile = getConfigFile(side);

        if (configFile.exists()) {
            var handle = new Toml();
            var toml = handle.read(configFile);
            var configKey = "Core.\"" + key + "\"";
            if (toml.contains(configKey)) {
                return Optional.of(toml.getBoolean(configKey));
            }
        }

        return Optional.empty();
    }

    private File getConfigFile(Side side) {
        var sideName = side.getSerializedName();
        var configDir = FabricLoader.getInstance().getConfigDir();
        return Paths.get(configDir + File.separator + Charmony.ID + "-" + sideName + ".toml").toFile();
    }
}
