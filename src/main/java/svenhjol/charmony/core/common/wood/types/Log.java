package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.blocks.CustomLogBlock;

import java.util.function.Supplier;

public class Log extends CustomWood {
    public final Supplier<CustomLogBlock> block;
    public final Supplier<CustomLogBlock.LogBlockItem> item;
    public final Supplier<CustomLogBlock> strippedBlock;
    public final Supplier<CustomLogBlock.LogBlockItem> strippedItem;

    public Log(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_log";
        var strippedId = "stripped_" + material.getSerializedName() + "_log";

        block = commonRegistry.block(id, key ->  new CustomLogBlock(key, material));
        item = commonRegistry.item(id, key ->  new CustomLogBlock.LogBlockItem(key, block));
        strippedBlock = commonRegistry.block(strippedId, key ->  new CustomLogBlock(key, material));
        strippedItem = commonRegistry.item(strippedId, key ->  new CustomLogBlock.LogBlockItem(key, strippedBlock));

        // Logs can set on fire.
        commonRegistry.ignite(block);
        commonRegistry.ignite(strippedBlock);

        // Logs can be stripped.
        commonRegistry.strippable(block, strippedBlock);

        woodRegistry.addItemToCreativeTab(item, material, CustomWoodType.LOG);
        woodRegistry.addItemToCreativeTab(strippedItem, material, CustomWoodType.STRIPPED_LOG);
    }
}
