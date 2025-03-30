package svenhjol.charmony.core.common.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;

public class CustomStandingSignBlock extends StandingSignBlock {
    protected final WoodMaterial material;

    public CustomStandingSignBlock(ResourceKey<Block> key, WoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties()
            .strength(1.0f)
            .noCollission()
            .setId(key));

        this.material = material;
    }
}
