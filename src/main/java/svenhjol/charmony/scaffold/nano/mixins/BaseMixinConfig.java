package svenhjol.charmony.scaffold.nano.mixins;

import com.google.common.base.CaseFormat;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import svenhjol.charmony.scaffold.nano.annotations.Feature;
import svenhjol.charmony.scaffold.nano.enums.Side;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

public abstract class BaseMixinConfig implements IMixinConfigPlugin {
    protected static final String FEATURES = "features";
    protected static final Logger LOGGER = LogManager.getLogger("MixinConfig");

    protected String mixinPackage;
    protected final List<String> blacklist = new ArrayList<>();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        // TODO: blacklist handling from config file.
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        var mixinSimpleName = mixinClassName.substring(mixinPackage.length() + 1);
        var split = mixinSimpleName.split("\\.");
        var mixinBaseName = split[0];
        var featureName = "";

        if (mixinBaseName.equals(FEATURES)) {
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

        if (!allowBaseName(mixinBaseName, mixinClassName)) {
            return false;
        }

        for (var predicate : runtimeBlacklist()) {
            if (!featureName.isEmpty() && predicate.test(featureName)) {
                blacklist.add(mixinSimpleName);
            }
        }

        if (blacklist.contains(mixinSimpleName)) {
            LOGGER.warn("Blacklisted mixin {}", mixinSimpleName);
            return false;
        }

        var valid = enabledInConfig(featureName);
        if (valid) {
            LOGGER.info("Enabled mixin {}", mixinClassName);
        } else {
            LOGGER.warn("Disabled mixin {}", mixinClassName);
        }
        return valid;
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

    protected List<Predicate<String>> runtimeBlacklist() {
        return List.of();
    }

    private boolean enabledInConfig(String featureName) {
        Feature feature = null;
        var configFile = Paths.get(FabricLoader.getInstance().getConfigDir() + File.separator + modId() + ".toml").toFile();

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
                feature = clazz.getAnnotation(Feature.class);
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

        if (!configFile.exists()) {
            return feature.enabledByDefault();
        }

        // Read the value from the config file.
        var handle = new Toml();
        var toml = handle.read(configFile);
        var enabledPath = featureName + ".Enabled";
        return toml.contains(enabledPath) && toml.getBoolean(enabledPath);
    }
}
