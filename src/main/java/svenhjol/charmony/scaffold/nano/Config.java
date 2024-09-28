package svenhjol.charmony.scaffold.nano;

import com.electronwill.nightconfig.toml.TomlFormat;
import com.moandjiezana.toml.Toml;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Config {
    private final Mod mod;

    public Config(Mod mod) {
        this.mod = mod;
    }

    public void populateFromDisk(List<? extends ModFeature> featureSet) {
        var toml = new Toml();
        var path = configPath();
        var file = path.toFile();

        if (file.exists()) {
            toml = toml.read(file);
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
                config.add(configName, feature.isEnabledInConfig());
            }
        }
    }

    private Path configPath() {
        return Paths.get(FabricLoader.getInstance().getConfigDir() + "/" + mod.id() + ".toml");
    }
}
