package svenhjol.charmony.core.common.features.wood;

public abstract class CustomWoodType {
    protected final WoodRegistry registry;
    protected final WoodMaterial material;

    public CustomWoodType(WoodRegistry registry, WoodMaterial material) {
        this.registry = registry;
        this.material = material;
    }

    public WoodMaterial material() {
        return material;
    }
}
