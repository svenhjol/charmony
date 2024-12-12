package svenhjol.charmony.core.common;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import svenhjol.charmony.core.helper.VillagerHelper;

@SuppressWarnings("unused")
public final class CommonRegistry {
    private static CommonRegistry instance;

    public static CommonRegistry instance() {
        if (instance == null) {
            instance = new CommonRegistry();
        }
        return instance;
    }

    /**
     * Convenience method to register a sound.
     */
    public SoundEvent sound(ResourceLocation id) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    /**
     * May be run late. Use this to conditionally add villager trades if the feature is enabled.
     */
    public void villagerTrade(VillagerProfession profession, int tier, VillagerTrades.ItemListing trade) {
        var trades = VillagerHelper.getMutableTrades(profession);
        trades.get(tier).add(trade);
        VillagerHelper.reassembleTrades(profession, trades);
    }
}
