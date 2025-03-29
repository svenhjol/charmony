package svenhjol.charmony.core.common.wood.holders;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.WoodType;
import svenhjol.charmony.core.common.wood.blocks.CustomBarrelBlock;

import java.util.List;
import java.util.function.Supplier;

public class CustomBarrel extends CustomWood {
    public final Supplier<CustomBarrelBlock> block;
    public final Supplier<CustomBarrelBlock.BarrelBlockItem> item;

    public CustomBarrel(WoodRegistry registry, WoodMaterial material) {
        super(registry, material);

        var id = material.getSerializedName() + "_barrel";
        var commonRegistry = registry.commonRegistry();

        block = commonRegistry.block(id, key -> new CustomBarrelBlock(key, material));
        item = commonRegistry.item(id, key -> new CustomBarrelBlock.BarrelBlockItem(key, block));

        // Barrels can be used as furnace fuel.
        commonRegistry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        commonRegistry.blocksForBlockEntity(() -> BlockEntityType.BARREL, List.of(block));

        // Add this barrel to the Fisherman's point of interest.
        commonRegistry.pointOfInterestBlockStates(
            () -> BuiltInRegistries.POINT_OF_INTEREST_TYPE.getOrThrow(PoiTypes.FISHERMAN),
            () -> block.get().getStateDefinition().getPossibleStates());

        // Add to creative menu.
        registry.addItemToCreativeTab(item, material, WoodType.BARREL);
    }
}
