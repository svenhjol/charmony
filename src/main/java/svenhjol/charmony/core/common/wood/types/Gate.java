package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomGateBlock;

import java.util.function.Supplier;

public class Gate extends CustomWoodType {
    public final Supplier<CustomGateBlock> block;
    public final Supplier<CustomGateBlock.GateBlockItem> item;

    public Gate(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_fence_gate";

        block = commonRegistry.block(id, key -> new CustomGateBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomGateBlock.GateBlockItem(key, block));

        commonRegistry.ignite(block); // Gates can set on fire.
        woodRegistry.addItemToCreativeTab(item, material, CustomWood.GATE);
    }
}
