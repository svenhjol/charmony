package svenhjol.charmony.core.common.features.wood.types;

import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.CustomWoodType;
import svenhjol.charmony.core.common.features.wood.WoodMaterial;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;
import svenhjol.charmony.core.common.features.wood.blocks.CustomSaplingBlock;

import java.util.function.Supplier;

public class Sapling extends CustomWoodType {
    public final Supplier<CustomSaplingBlock> block;
    public final Supplier<CustomSaplingBlock.SaplingBlockItem> item;

    public Sapling(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var saplingId = material.getSerializedName() + "_sapling";

        var tree = material.tree();
        if (tree.isEmpty()) {
            throw new RuntimeException("No tree defined for sapling!");
        }

        block = commonRegistry.block(saplingId, key -> new CustomSaplingBlock(key, material, tree.get()));
        item = commonRegistry.item(saplingId, key -> new CustomSaplingBlock.SaplingBlockItem(key, block));

        woodRegistry.addItemToCreativeTab(item, material, CustomWood.SAPLING);
    }
}
