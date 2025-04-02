package svenhjol.charmony.core.common.wood.types;

import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.blocks.CustomPlanksBlock;

import java.util.function.Supplier;

public class Planks extends CustomWoodType {
    public final Supplier<CustomPlanksBlock> block;
    public final Supplier<CustomPlanksBlock.PlanksBlockItem> item;

    public Planks(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var id = material.getSerializedName() + "_planks";

        block = commonRegistry.block(id, key -> new CustomPlanksBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomPlanksBlock.PlanksBlockItem(key, block));

        commonRegistry.ignite(block); // Planks can set on fire.
        woodRegistry.addItemToCreativeTab(item, material, CustomWood.PLANKS);
    }
}
