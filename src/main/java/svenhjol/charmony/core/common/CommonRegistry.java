package svenhjol.charmony.core.common;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.helper.VillagerHelper;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;

@SuppressWarnings("unused")
public final class CommonRegistry {
    private static CommonRegistry instance;

    public static CommonRegistry instance() {
        if (instance == null) {
            instance = new CommonRegistry();
        }
        return instance;
    }

    public <B extends Block> Supplier<B> block(ResourceLocation id, B supplier) {
        return () -> Registry.register(BuiltInRegistries.BLOCK, id, supplier);
    }

    public <BE extends BlockEntity, B extends Block> Supplier<BlockEntityType<BE>> blockEntity(ResourceLocation id,
                                                                                               FabricBlockEntityTypeBuilder.Factory<BE> builder,
                                                                                               List<Supplier<B>> blocks) {
        var blocksToAdd = blocks.stream().map(Supplier::get).toArray(Block[]::new);

        return () -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id,
            FabricBlockEntityTypeBuilder.create(builder, blocksToAdd).build());
    }

    public <BE extends BlockEntity> Supplier<BlockEntityType<BE>> blockEntity(ResourceLocation id, FabricBlockEntityTypeBuilder.Factory<BE> builder) {
        return blockEntity(id, builder, List.of());
    }

    public <D> Supplier<DataComponentType<D>> dataComponent(ResourceLocation id, Supplier<UnaryOperator<DataComponentType.Builder<D>>> dataComponent) {
        return () -> DataComponents.register(id.toString(), dataComponent.get());
    }

    public <I extends Item> Supplier<I> item(ResourceLocation id, I supplier) {
        return () -> Registry.register(BuiltInRegistries.ITEM, id, supplier);
    }

    public Supplier<SoundEvent> sound(ResourceLocation id) {
        return () -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
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
