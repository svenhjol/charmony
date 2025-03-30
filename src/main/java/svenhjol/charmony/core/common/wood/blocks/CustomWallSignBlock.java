package svenhjol.charmony.core.common.wood.blocks;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import svenhjol.charmony.core.common.wood.WoodMaterial;

public class CustomWallSignBlock extends WallSignBlock {
    public CustomWallSignBlock(ResourceKey<Block> key, WoodMaterial material, WoodType woodType) {
        super(woodType, material.blockProperties()
            .sound(woodType.soundType())
            .forceSolidOn()
            .ignitedByLava()
            .instrument(NoteBlockInstrument.BASS)
            .noCollission()
            .strength(1.0f)
            .setId(key));
    }
}
