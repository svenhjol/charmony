package svenhjol.charmony.core.common;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.helper.VillagerHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;

@SuppressWarnings("unused")
public final class CommonRegistry {
    private final SidedFeature feature;

    private CommonRegistry(SidedFeature feature) {
        this.feature = feature;
    }

    public static CommonRegistry forFeature(SidedFeature feature) {
        return new CommonRegistry(feature);
    }

    public <E extends Entity> Registerable<Void> biomeSpawn(Predicate<Holder<Biome>> predicate, MobCategory category,
                                                            Supplier<EntityType<E>> entity, int weight, int minGroupSize, int maxGroupSize) {
        return new Registerable<>(feature, () -> {
            Predicate<BiomeSelectionContext> biomeSelectionContext = c -> predicate.test(c.getBiomeRegistryEntry());
            BiomeModifications.addSpawn(biomeSelectionContext, category, entity.get(), weight, minGroupSize, maxGroupSize);
            return null;
        });
    }

    public <B extends Block> Registerable<B> block(String id, Function<ResourceKey<Block>, B> funcSupplier) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            var key = ResourceKey.create(Registries.BLOCK, res);
            return Registry.register(BuiltInRegistries.BLOCK, res, funcSupplier.apply(key));
        });
    }

    public <BE extends BlockEntity, B extends Block> Registerable<BlockEntityType<BE>> blockEntity(String id,
                                                                                               FabricBlockEntityTypeBuilder.Factory<BE> builder,
                                                                                               List<Supplier<B>> blocks) {
        return new Registerable<>(feature, () -> {
            var blocksToAdd = blocks.stream().map(Supplier::get).toArray(Block[]::new);
            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, feature.id(id),
                FabricBlockEntityTypeBuilder.create(builder, blocksToAdd).build());
        });
    }

    public <BE extends BlockEntity> Registerable<BlockEntityType<BE>> blockEntity(String id, FabricBlockEntityTypeBuilder.Factory<BE> builder) {
        return blockEntity(id, builder, List.of());
    }

    /**
     * May be run late. Use this to conditionally add blocks to a block entity if the feature is enabled.
     */
    public <BE extends BlockEntity> void blocksForBlockEntity(Supplier<BlockEntityType<BE>> supplier, List<Supplier<? extends Block>> blocks) {
        var blockEntity = supplier.get();
        var blockEntityBlocks = blockEntity.validBlocks;
        List<Block> mutable = new ArrayList<>(blockEntityBlocks);

        for (Supplier<? extends Block> blockSupplier : blocks) {
            var block = blockSupplier.get();
            if (!mutable.contains(block)) {
                mutable.add(block);
            }
        }

        blockEntity.validBlocks = new HashSet<>(mutable);
    }

    public <D> Registerable<DataComponentType<D>> dataComponent(String id, Supplier<UnaryOperator<DataComponentType.Builder<D>>> dataComponent) {
        return new Registerable<>(feature, () -> DataComponents.register(feature.id(id).toString(), dataComponent.get()));
    }

    public <E extends Entity> Registerable<EntityType<E>> entity(String id, Function<ResourceKey<EntityType<?>>, Supplier<EntityType.Builder<E>>> funcSupplier) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            var key = ResourceKey.create(Registries.ENTITY_TYPE, res);
            return Registry.register(BuiltInRegistries.ENTITY_TYPE, feature.id(id), funcSupplier.apply(key).get().build(key));
        });
    }

    public <I extends Item> Registerable<I> item(String id, Function<ResourceKey<Item>, I> funcSupplier) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            var key = ResourceKey.create(Registries.ITEM, res);
            return Registry.register(BuiltInRegistries.ITEM, feature.id(id), funcSupplier.apply(key));
        });
    }

    /**
     * This sets all the attributes for a mob. Use it when creating custom mobs.
     * Don't do this for vanilla mobs; use mobAttribute() to add new attributes.
     */
    public <M extends Mob> Registerable<Void> mobAttributes(Supplier<EntityType<M>> mob, Supplier<AttributeSupplier.Builder> builder) {
        return new Registerable<>(feature, () -> {
            FabricDefaultAttributeRegistry.register(mob.get(), builder.get());
            return null;
        });
    }

    public <M extends Mob> Registerable<Void> mobSpawnPlacement(Supplier<EntityType<M>> mob, SpawnPlacementType placementType,
                                                                Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<M> predicate) {
        return new Registerable<>(feature, () -> {
            SpawnPlacements.register(mob.get(), placementType, heightmapType, predicate);
            return null;
        });
    }

    public Registerable<SoundEvent> sound(String id) {
        var res = feature.id(id);
        return new Registerable<>(feature, () -> Registry.register(BuiltInRegistries.SOUND_EVENT, res, SoundEvent.createVariableRangeEvent(res)));
    }

    /**
     * May be run late. Use this to conditionally add villager trades if the feature is enabled.
     */
    public void villagerTrade(VillagerProfession profession, int tier, VillagerTrades.ItemListing trade) {
        var trades = VillagerHelper.getMutableTrades(profession);
        trades.get(tier).add(trade);
        VillagerHelper.reassembleTrades(profession, trades);
    }

    /**
     * May be run late. Use this to conditionally add trades to a wandering trade if the feature is enabled.
     */
    public void wandererTrade(VillagerTrades.ItemListing trade, boolean isRare) {
        List<VillagerTrades.ItemListing> trades = NonNullList.create();
        int index = isRare ? 2 : 1;

        trades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(index)));
        trades.add(trade);

        WANDERING_TRADER_TRADES.put(index, trades.toArray(new VillagerTrades.ItemListing[0]));
    }
}
