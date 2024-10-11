package svenhjol.charmony.scaffold;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.scaffold.base.Mod;

@SuppressWarnings("unused")
public final class Charmony extends Mod {
    public static final String ID = "charmony";

    private static Charmony instance;

    private Charmony() {
        super(ID);
    }

    public static Charmony instance() {
        if (instance == null) {
            instance = new Charmony();
        }
        return instance;
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.tryBuild(ID, path);
    }
}
