package svenhjol.charmony.core.common.wood;

public abstract class CustomWoodType {
    protected final WoodRegistry registry;
    protected final WoodMaterial material;

    public CustomWoodType(WoodRegistry registry, WoodMaterial material) {
        this.registry = registry;
        this.material = material;
    }
}
