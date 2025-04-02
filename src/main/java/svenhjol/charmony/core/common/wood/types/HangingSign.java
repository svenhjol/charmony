package svenhjol.charmony.core.common.wood.types;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomCeilingHangingSignBlock;
import svenhjol.charmony.core.common.wood.blocks.CustomWallHangingSignBlock;
import svenhjol.charmony.core.common.wood.items.CustomHangingSignItem;

import java.util.List;
import java.util.function.Supplier;

public class HangingSign extends CustomWoodType {
    public final Supplier<CustomCeilingHangingSignBlock> hangingBlock;
    public final Supplier<CustomWallHangingSignBlock> wallBlock;
    public final Supplier<CustomHangingSignItem> item;

    public HangingSign(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var commonRegistry = woodRegistry.commonRegistry();
        var hangingId = material.getSerializedName() + "_hanging_sign";
        var wallId = material.getSerializedName() + "_wall_hanging_sign";

        hangingBlock = commonRegistry.block(hangingId, key -> new CustomCeilingHangingSignBlock(key, material));
        wallBlock = woodRegistry.wallHangingSignBlock(wallId, material);
        item = commonRegistry.item(hangingId, key -> new CustomHangingSignItem(key, material, hangingBlock, wallBlock));

        // This is needed so we can set the correct blocks to hanging signs later on in the registration.
        woodRegistry.addHangingSignItem(item);

        // Associate with the hanging sign block entity.
        commonRegistry.blocksForBlockEntity(() -> BlockEntityType.HANGING_SIGN, List.of(hangingBlock, wallBlock));

        woodRegistry.addItemToCreativeTab(item, material, CustomWood.HANGING_SIGN);
    }
}
