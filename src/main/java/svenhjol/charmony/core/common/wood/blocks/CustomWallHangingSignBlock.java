package svenhjol.charmony.core.common.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import svenhjol.charmony.core.common.wood.WoodMaterial;

public class CustomWallHangingSignBlock extends WallHangingSignBlock {
    public CustomWallHangingSignBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(material.woodType(), material.blockProperties()
            .sound(material.woodType().soundType())
            .forceSolidOn()
            .ignitedByLava()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0f)
            .setId(key));
    }
}
