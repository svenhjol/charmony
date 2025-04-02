package svenhjol.charmony.core.common.wood;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.core.common.wood.blocks.CustomStairBlock;
import svenhjol.charmony.core.common.wood.blocks.CustomWallHangingSignBlock;
import svenhjol.charmony.core.common.wood.blocks.CustomWallSignBlock;
import svenhjol.charmony.core.common.wood.items.CustomHangingSignItem;
import svenhjol.charmony.core.common.wood.items.CustomSignItem;
import svenhjol.charmony.core.common.wood.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class WoodRegistry {
    private final SidedFeature feature;
    private final CommonRegistry commonRegistry;

    public final Table<Supplier<? extends Item>, Supplier<ItemLike>, CustomWood> itemCreativeTabs = HashBasedTable.create();
    public final List<Supplier<CustomSignItem>> signItems = new ArrayList<>();
    public final List<Supplier<CustomHangingSignItem>> hangingSignItems = new ArrayList<>();

    private WoodRegistry(CommonRegistry commonRegistry) {
        this.commonRegistry = commonRegistry;
        this.feature = commonRegistry.feature();
    }

    public static WoodRegistry forRegistry(CommonRegistry registry) {
        return new WoodRegistry(registry);
    }

    public void addItemToCreativeTab(Supplier<? extends Item> item, WoodMaterial material, CustomWood woodType) {
        var after = material.creativeMenuPosition().get(woodType);
        itemCreativeTabs.put(item, after, woodType);
    }

    public void addSignItem(Supplier<CustomSignItem> item) {
        if (!signItems.contains(item)) {
            signItems.add(item);
        }
    }

    public void addHangingSignItem(Supplier<CustomHangingSignItem> item) {
        if (!hangingSignItems.contains(item)) {
            hangingSignItems.add(item);
        }
    }

    public Registerable<Barrel> barrel(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Barrel(this, material));
    }

    public Registerable<Boat> boat(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Boat(this, material));
    }

    public Registerable<EntityType<net.minecraft.world.entity.vehicle.Boat>> boatEntity(WoodMaterial material, Supplier<Item> boatItem) {
        var id = material.getSerializedName() + "_boat";

        EntityType.EntityFactory<net.minecraft.world.entity.vehicle.Boat> factory =
            (entityType, level) -> new net.minecraft.world.entity.vehicle.Boat(entityType, level, boatItem);

        var boatBuilder = EntityType.Builder.of(factory, MobCategory.MISC)
            .noLootTable()
            .sized(1.375f, 0.5652f)
            .eyeHeight(0.5652f)
            .clientTrackingRange(10);

        return commonRegistry.entity(id, () -> boatBuilder);
    }

    public Registerable<Button> button(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Button(this, material));
    }

    public Registerable<EntityType<net.minecraft.world.entity.vehicle.ChestBoat>> chestBoatEntity(WoodMaterial material, Supplier<Item> chestBoatItem) {
        var id = material.getSerializedName() + "_chest_boat";

        EntityType.EntityFactory<net.minecraft.world.entity.vehicle.ChestBoat> factory =
            (entityType, level) -> new ChestBoat(entityType, level, chestBoatItem);

        var chestBoatBuilder = EntityType.Builder.of(factory, MobCategory.MISC)
            .noLootTable()
            .sized(1.375f, 0.5652f)
            .eyeHeight(0.5652f)
            .clientTrackingRange(10);

        return commonRegistry.entity(id, () -> chestBoatBuilder);
    }

    public CommonRegistry commonRegistry() {
        return commonRegistry;
    }

    public Registerable<Door> door(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Door(this, material));
    }

    public SidedFeature feature() {
        return feature;
    }

    public Registerable<Fence> fence(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Fence(this, material));
    }

    public Registerable<Gate> gate(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Gate(this, material));
    }

    public Registerable<HangingSign> hangingSign(WoodMaterial material) {
        return new Registerable<>(feature, () -> new HangingSign(this, material));
    }

    public Registerable<Leaves> leaves(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Leaves(this, material));
    }

    public Registerable<Log> log(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Log(this, material));
    }

    public Registerable<Planks> planks(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Planks(this, material));
    }

    public Registerable<PressurePlate> pressurePlate(WoodMaterial material) {
        return new Registerable<>(feature, () -> new PressurePlate(this, material));
    }

    public Registerable<Sapling> sapling(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Sapling(this, material));
    }

    public Registerable<Sign> sign(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Sign(this, material));
    }

    public Registerable<Slab> slab(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Slab(this, material));
    }

    public Registerable<Stairs> stairs(WoodMaterial material, Supplier<Planks> planks) {
        return new Registerable<>(feature, () -> new Stairs(this, material, planks));
    }

    public Pair<Registerable<CustomStairBlock>, Registerable<CustomStairBlock.StairBlockItem>> stairsBlockHelper(String id, Supplier<WoodMaterial> material, Supplier<BlockState> state) {
        var block = commonRegistry.block(id, key -> new CustomStairBlock(key, material.get(), state.get()));
        var item = commonRegistry.item(id, key -> new CustomStairBlock.StairBlockItem(key, block));
        return Pair.of(block, item);
    }

    public Registerable<Trapdoor> trapdoor(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Trapdoor(this, material));
    }

    public Registerable<CustomWallHangingSignBlock> wallHangingSignBlock(String id, WoodMaterial material) {
        return commonRegistry.block(id, key -> new CustomWallHangingSignBlock(key, material));
    }

    public Registerable<CustomWallSignBlock> wallSignBlock(String id, WoodMaterial material) {
        return commonRegistry.block(id, key -> new CustomWallSignBlock(key, material));
    }

    public Registerable<Wood> wood(WoodMaterial material) {
        return new Registerable<>(feature, () -> new Wood(this, material));
    }
}
