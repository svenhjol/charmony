package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomWoodenTrapdoorBlock;

import java.util.function.Supplier;

public class Trapdoor extends CustomWoodType {
    public final Supplier<CustomWoodenTrapdoorBlock> block;
    public final Supplier<CustomWoodenTrapdoorBlock.WoodenTrapdoorBlockItem> item;

    public Trapdoor(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_trapdoor";

        block = commonRegistry.block(id, key -> new CustomWoodenTrapdoorBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomWoodenTrapdoorBlock.WoodenTrapdoorBlockItem(key, block));

        woodRegistry.addItemToCreativeTab(item, material, CustomWood.TRAPDOOR);
    }
}
