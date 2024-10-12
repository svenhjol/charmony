package svenhjol.charmony.scaffold.base;

public interface IFeature {
    void enabled(boolean state);

    boolean enabled();

    boolean enabledByDefault();

    boolean canBeDisabled();

    String name();

    String description();

    Mod mod();

    Log log();
}
