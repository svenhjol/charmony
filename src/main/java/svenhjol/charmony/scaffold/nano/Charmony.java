package svenhjol.charmony.scaffold.nano;

public class Charmony extends Mod {
    public static final String ID = "charmony-nano";
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
