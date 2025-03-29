package svenhjol.charmony.core.common.wood.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.common.material.FuelProvider;
import svenhjol.charmony.core.common.wood.WoodMaterial;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class CustomBarrelBlock extends BarrelBlock {
    private final WoodMaterial material;

    public CustomBarrelBlock(ResourceKey<Block> key, WoodMaterial material) {
        super(Properties.ofFullCopy(Blocks.BARREL).setId(key));
        this.material = material;

        this.registerDefaultState(this.getStateDefinition()
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(OPEN, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BarrelBlockEntity(pos, state);
    }

    public WoodMaterial getMaterial() {
        return material;
    }

    public static class BarrelBlockItem extends BlockItem implements FuelProvider {
        private final WoodMaterial material;

        public BarrelBlockItem(ResourceKey<Item> key, Supplier<CustomBarrelBlock> block) {
            super(block.get(), new Properties().setId(key));
            this.material = block.get().getMaterial();
        }

        @Override
        public int fuelTime() {
            return material.fuelTime();
        }
    }
}
