package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomWoodenPressurePlateBlock;

import java.util.function.Supplier;

public class PressurePlate extends CustomWoodType {
    public final Supplier<CustomWoodenPressurePlateBlock> block;
    public final Supplier<CustomWoodenPressurePlateBlock.WoodenPressurePlateBlockItem> item;

    public PressurePlate(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_pressure_plate";

        block = commonRegistry.block(id, key -> new CustomWoodenPressurePlateBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomWoodenPressurePlateBlock.WoodenPressurePlateBlockItem(key, block));

        woodRegistry.addItemToCreativeTab(item, material, CustomWood.PRESSURE_PLATE);
    }
}
