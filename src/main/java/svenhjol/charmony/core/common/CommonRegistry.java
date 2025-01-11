package svenhjol.charmony.core.common;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.common.dispenser.ConditionalDispenseItemBehavior;
import svenhjol.charmony.core.enums.Side;
import svenhjol.charmony.core.helpers.VillagerHelper;

import java.util.*;
import java.util.function.*;

import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;

@SuppressWarnings("unused")
public final class CommonRegistry {
    private final SidedFeature feature;
    public static final Map<ItemLike, List<ConditionalDispenseItemBehavior>> CONDITIONAL_DISPENSER_BEHAVIORS = new HashMap<>();

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
                                                                                               Supplier<FabricBlockEntityTypeBuilder.Factory<BE>> builder,
                                                                                               List<Supplier<B>> blocks) {
        return new Registerable<>(feature, () -> {
            var blocksToAdd = blocks.stream().map(Supplier::get).toArray(Block[]::new);
            return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, feature.id(id),
                FabricBlockEntityTypeBuilder.create(builder.get(), blocksToAdd).build());
        });
    }

    public <BE extends BlockEntity> Registerable<BlockEntityType<BE>> blockEntity(String id, Supplier<FabricBlockEntityTypeBuilder.Factory<BE>> builder) {
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

    public <I extends ItemLike, C extends ConditionalDispenseItemBehavior> Registerable<Void> conditionalDispenserBehavior(Supplier<I> itemSupplier, Supplier<C> behaviorSupplier) {
        return new Registerable<>(feature, () -> {
            var item = itemSupplier.get();
            var behavior = behaviorSupplier.get();
            CONDITIONAL_DISPENSER_BEHAVIORS.computeIfAbsent(item, a -> new ArrayList<>()).add(behavior);
            return null;
        });
    }

    public <D> Registerable<DataComponentType<D>> dataComponent(String id, Supplier<UnaryOperator<DataComponentType.Builder<D>>> dataComponent) {
        return new Registerable<>(feature, () -> DataComponents.register(feature.id(id).toString(), dataComponent.get()));
    }

    public <E extends Entity> Registerable<EntityType<E>> entity(String id, Supplier<EntityType.Builder<E>> supplier) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            var key = ResourceKey.create(Registries.ENTITY_TYPE, res);
            return Registry.register(BuiltInRegistries.ENTITY_TYPE, feature.id(id), supplier.get().build(key));
        });
    }

    public <I extends Item> Registerable<I> item(String id, Function<ResourceKey<Item>, I> funcSupplier) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            var key = ResourceKey.create(Registries.ITEM, res);
            return Registry.register(BuiltInRegistries.ITEM, feature.id(id), funcSupplier.apply(key));
        });
    }

    public <T extends MenuType<M>, M extends AbstractContainerMenu> Registerable<T> menuType(String id, Supplier<T> menuSupplier) {
        return new Registerable<>(feature,
            () -> Registry.register(BuiltInRegistries.MENU, feature.id(id), menuSupplier.get()));
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

    /**
     * Register a callback when the server receives a packet from the client.
     * @param type Packet type.
     * @param handler Callback.
     * @return Empty registerable.
     * @param <P> Payload class.
     */
    public <P extends CustomPacketPayload> Registerable<Void> packetReceiver(CustomPacketPayload.Type<P> type, Supplier<BiConsumer<Player, P>> handler) {
        return new Registerable<>(feature, () -> {
            ServerPlayNetworking.registerGlobalReceiver(type,
                (payload, context) -> context.player().server.execute(
                    () -> handler.get().accept(context.player(), payload)));
            return null;
        });
    }

    /**
     * Register packet to send from the specified side.
     * @param side Side to send the packet from.
     * @param type Packet type.
     * @param codec Packet codec.
     * @return Empty registerable.
     * @param <P> Payload class.
     */
    @SuppressWarnings("unchecked")
    public <P extends CustomPacketPayload> Registerable<Void> packetSender(Side side, CustomPacketPayload.Type<P> type, StreamCodec<? extends ByteBuf, P> codec) {
        return new Registerable<>(feature, () -> {
            switch (side) {
                case Common -> PayloadTypeRegistry.playS2C().register(type, (StreamCodec<? super ByteBuf, P>)codec);
                case Client -> PayloadTypeRegistry.playC2S().register(type, (StreamCodec<? super ByteBuf, P>)codec);
            }
            return null;
        });
    }

    public Registerable<SoundEvent> sound(String id) {
        var res = feature.id(id);
        return new Registerable<>(feature, () -> Registry.register(BuiltInRegistries.SOUND_EVENT, res, SoundEvent.createVariableRangeEvent(res)));
    }


    public <S extends Structure> Supplier<StructureType<S>> structure(String id, Supplier<MapCodec<S>> codec) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, res.toString(), codec::get);
        });
    }

    public Supplier<StructurePieceType> structurePiece(String id, Supplier<StructurePieceType> piece) {
        return new Registerable<>(feature, () -> {
            var res = feature.id(id);
            return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, res, piece.get());
        });
    }

    /**
     * May be run late. Use this to conditionally add villager trades if the feature is enabled.
     */
    public Registerable<Void> villagerTrade(Supplier<VillagerProfession> profession, int tier, Supplier<VillagerTrades.ItemListing> trade) {
        return new Registerable<>(feature, () -> {
            var trades = VillagerHelper.getMutableTrades(profession.get());
            trades.get(tier).add(trade.get());
            VillagerHelper.reassembleTrades(profession.get(), trades);
            return null;
        });
    }

    /**
     * May be run late. Use this to conditionally add trades to a wandering trade if the feature is enabled.
     */
    public Registerable<Void> wandererTrade(Supplier<VillagerTrades.ItemListing> trade, boolean isRare) {
        return new Registerable<>(feature, () -> {
            List<VillagerTrades.ItemListing> trades = NonNullList.create();
            int index = isRare ? 2 : 1;

            trades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(index)));
            trades.add(trade.get());

            WANDERING_TRADER_TRADES.put(index, trades.toArray(new VillagerTrades.ItemListing[0]));
            return null;
        });
    }
}
