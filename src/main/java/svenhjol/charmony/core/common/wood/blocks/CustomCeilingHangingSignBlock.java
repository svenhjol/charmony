package svenhjol.charmony.core.common.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;

public class CustomCeilingHangingSignBlock extends CeilingHangingSignBlock {
    protected final WoodMaterial material;

    public CustomCeilingHangingSignBlock(ResourceKey<Block> key, WoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties()
            .noCollission()
            .sound(SoundType.HANGING_SIGN)
            .strength(1.0f)
            .setId(key));

        this.material = material;
    }
}
