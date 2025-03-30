package svenhjol.charmony.core.common.wood.items;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import svenhjol.charmony.core.common.wood.WoodMaterial;

import java.util.function.Supplier;

public class CustomSignItem extends SignItem {
    protected final WoodMaterial material;
    protected final Supplier<? extends SignBlock> signBlock;
    protected final Supplier<? extends WallSignBlock> wallSignBlock;

    public <S extends SignBlock, W extends WallSignBlock> CustomSignItem(ResourceKey<Item> key, WoodMaterial material, Supplier<S> signBlock, Supplier<W> wallSignBlock) {
        super(Blocks.OAK_SIGN, Blocks.OAK_WALL_SIGN, new Properties()
            .stacksTo(16)
            .setId(key));

        this.material = material;
        this.signBlock = signBlock;
        this.wallSignBlock = wallSignBlock;
    }

    public Supplier<? extends SignBlock> getSignBlock() {
        return signBlock;
    }

    public Supplier<? extends WallSignBlock> getWallSignBlock() {
        return wallSignBlock;
    }
}
