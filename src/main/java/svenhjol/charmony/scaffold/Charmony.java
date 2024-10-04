package svenhjol.charmony.scaffold;

import svenhjol.charmony.scaffold.base.Mod;

@SuppressWarnings("unused")
public final class Charmony extends Mod {
    public static final String ID = "charmony";

    private static Charmony instance;

    private Charmony() {}

    public static Charmony instance() {
        if (instance == null) {
            instance = new Charmony();
        }
        return instance;
    }

    @Override
    public String id() {
        return ID;
    }
}
