package svenhjol.charmony.core.common.wood;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.core.common.wood.holders.CustomBarrel;

import java.util.function.Supplier;

public final class WoodRegistry {
    private final SidedFeature feature;
    private final CommonRegistry commonRegistry;
    public final Table<Supplier<? extends Item>, Supplier<ItemLike>, WoodType> itemCreativeTabs = HashBasedTable.create();

    private WoodRegistry(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
        this.feature = commonRegistry.feature();
    }

    public static WoodRegistry forRegistry(CommonRegistry registry) {
        return new WoodRegistry(registry);
    }

    public void addItemToCreativeTab(Supplier<? extends Item> item, WoodMaterial material, WoodType woodType) {
        var after = material.creativeMenuPosition().get(woodType);
        itemCreativeTabs.put(item, after, woodType);
    }

    public Registerable<CustomBarrel> barrel(WoodMaterial material) {
        return new Registerable<>(feature, () -> new CustomBarrel(this, material));
    }

    public CommonRegistry commonRegistry() {
        return commonRegistry;
    }
}
