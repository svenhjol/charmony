package svenhjol.charmony.core.common.wood.types;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.common.wood.CustomWoodType;
import svenhjol.charmony.core.common.wood.CustomWood;
import svenhjol.charmony.core.common.wood.WoodMaterial;
import svenhjol.charmony.core.common.wood.WoodRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomStandingSignBlock;
import svenhjol.charmony.core.common.wood.blocks.CustomWallSignBlock;
import svenhjol.charmony.core.common.wood.items.CustomSignItem;

import java.util.List;
import java.util.function.Supplier;

public class Sign extends CustomWood {
    public final Supplier<CustomStandingSignBlock> standingBlock;
    public final Supplier<CustomWallSignBlock> wallBlock;
    public final Supplier<CustomSignItem> item;

    public Sign(WoodRegistry woodRegistry, WoodMaterial material) {
        super(woodRegistry, material);

        var customRegistry = woodRegistry.commonRegistry();
        var woodType = material.woodType();

        var signId = material.getSerializedName() + "_sign";
        var wallSignId = material.getSerializedName() + "_wall_sign";

        standingBlock = customRegistry.block(signId, key -> new CustomStandingSignBlock(key, material, woodType));
        wallBlock = woodRegistry.wallSignBlock(wallSignId, material, woodType);
        item = customRegistry.item(signId, key -> new CustomSignItem(key, material, standingBlock, wallBlock));

        // This is needed so we can set the correct blocks to signs later on in the registration.
        woodRegistry.addSignItem(item);

        // Associate with the sign block entity.
        customRegistry.blocksForBlockEntity(() -> BlockEntityType.SIGN, List.of(standingBlock, wallBlock));
        woodRegistry.addItemToCreativeTab(item, material, CustomWoodType.SIGN);
    }
}
