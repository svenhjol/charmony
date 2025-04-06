package svenhjol.charmony.core.common.features.wandering_trader_tiers;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

public class Handlers extends Setup<WanderingTraderTiers>  {
    public Handlers(WanderingTraderTiers feature) {
        super(feature);
    }

    public void addRandomTier(WanderingTrader trader) {
        if (!feature().enabled()) return;

        var tiers = CommonRegistry.WANDERING_TRADER_TIERS;
        if (tiers.isEmpty()) return;

        var random = trader.getRandom();
        var listings = tiers.values().stream().toList().get(random.nextInt(tiers.size()));
        var offers = trader.getOffers();

        trader.addOffersFromItemListings(offers, listings.toArray(new VillagerTrades.ItemListing[0]), listings.size());
    }
}
