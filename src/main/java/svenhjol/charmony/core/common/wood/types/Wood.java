package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.blocks.CustomLogBlock;

import java.util.function.Supplier;

public class Wood extends CustomWoodType {
    public final Supplier<CustomLogBlock> block;
    public final Supplier<CustomLogBlock.LogBlockItem> item;
    public final Supplier<CustomLogBlock> strippedBlock;
    public final Supplier<CustomLogBlock.LogBlockItem> strippedItem;

    public Wood(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_wood";
        var strippedId = "stripped_" + material.getSerializedName() + "_wood";

        block = commonRegistry.block(id, key ->  new CustomLogBlock(key, material));
        item = commonRegistry.item(id, key ->  new CustomLogBlock.LogBlockItem(key, block));
        strippedBlock = commonRegistry.block(strippedId, key ->  new CustomLogBlock(key, material));
        strippedItem = commonRegistry.item(strippedId, key ->  new CustomLogBlock.LogBlockItem(key, strippedBlock));

        // Wood can set on fire.
        commonRegistry.ignite(block);
        commonRegistry.ignite(strippedBlock);

        // Wood can be stripped.
        commonRegistry.strippable(block, strippedBlock);

        woodRegistry.addItemToCreativeTab(item, material, CustomWood.WOOD);
        woodRegistry.addItemToCreativeTab(strippedItem, material, CustomWood.STRIPPED_WOOD);
    }
}
