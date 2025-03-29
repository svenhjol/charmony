package svenhjol.charmony.core.common.wood;

public abstract class CustomWood {
    protected final WoodRegistry registry;
    protected final WoodMaterial material;

    public CustomWood(WoodRegistry registry, WoodMaterial material) {
        this.registry = registry;
        this.material = material;
    }
}
