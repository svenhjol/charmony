package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.blocks.CustomSlabBlock;

import java.util.function.Supplier;

public class Slab extends CustomWood {
    public final Supplier<CustomSlabBlock> block;
    public final Supplier<CustomSlabBlock.SlabBlockItem> item;

    public Slab(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_slab";

        block = commonRegistry.block(id, key -> new CustomSlabBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomSlabBlock.SlabBlockItem(key, block));

        commonRegistry.ignite(block); // Slabs can set on fire.
        woodRegistry.addItemToCreativeTab(item, material, CustomWoodType.SLAB);
    }
}
