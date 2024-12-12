package svenhjol.charmony.core.common;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.helper.VillagerHelper;

import java.util.Arrays;
import java.util.List;
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

    public <B extends Block> Registerable<B> block(String id, Supplier<B> supplier) {
        return new Registerable<>(feature, () -> Registry.register(BuiltInRegistries.BLOCK, feature.id(id), supplier.get()));
    }

    public <BE extends BlockEntity, B extends Block> Registerable<BlockEntityType<BE>> blockEntity(String id,
                                                                                               FabricBlockEntityTypeBuilder.Factory<BE> builder,
                                                                                               List<Supplier<B>> blocks) {
        var blocksToAdd = blocks.stream().map(Supplier::get).toArray(Block[]::new);

        return new Registerable<>(feature, () -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, feature.id(id),
            FabricBlockEntityTypeBuilder.create(builder, blocksToAdd).build()));
    }

    public <BE extends BlockEntity> Registerable<BlockEntityType<BE>> blockEntity(String id, FabricBlockEntityTypeBuilder.Factory<BE> builder) {
        return blockEntity(id, builder, List.of());
    }

    public <D> Registerable<DataComponentType<D>> dataComponent(String id, Supplier<UnaryOperator<DataComponentType.Builder<D>>> dataComponent) {
        return new Registerable<>(feature, () -> DataComponents.register(feature.id(id).toString(), dataComponent.get()));
    }

    public <I extends Item> Registerable<I> item(String id, I supplier) {
        return new Registerable<>(feature, () -> Registry.register(BuiltInRegistries.ITEM, feature.id(id), supplier));
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
