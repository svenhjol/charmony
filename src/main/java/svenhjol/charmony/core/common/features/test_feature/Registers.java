package svenhjol.charmony.core.common.features.test_feature;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.core.common.GenericTrades;
import svenhjol.charmony.core.helpers.TagHelper;

import java.util.ArrayList;
import java.util.List;

public class Registers extends Setup<TestFeature> {
    public Registers(TestFeature feature) {
        super(feature);

        var registry = CommonRegistry.forFeature(feature);
        registry.wandererTrade(() -> new VillagerTrades.EmeraldForItems(Items.LAVA_BUCKET, 1, 1, 1, 2), true);
    }

    @Override
    public Runnable boot() {
        return () -> ServerLifecycleEvents.SERVER_STARTING.register(this::handleServerStarting);
    }

    private void handleServerStarting(MinecraftServer server) {
        var registry = CommonRegistry.forFeature(feature());

        List<VillagerTrades.ItemListing> concretes = new ArrayList<>();
        for (var concrete : TagHelper.getValues(BuiltInRegistries.BLOCK, BlockTags.CONCRETE_POWDER)) {
            concretes.add(new GenericTrades.ItemsForEmeralds(concrete, 5, 8, 0, 1));
        }

        registry.wandererTradeTier("concretes", concretes, concretes.size());
    }
}
