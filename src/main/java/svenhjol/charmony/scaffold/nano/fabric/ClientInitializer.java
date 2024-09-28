package svenhjol.charmony.scaffold.nano.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.scaffold.nano.Charmony;
import svenhjol.charmony.scaffold.nano.client.repair_cost_visible.RepairCostVisible;
import svenhjol.charmony.scaffold.nano.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        var charmony = Charmony.instance();
        var feature = new RepairCostVisible(charmony);

        // Add features.
        charmony.addFeature(feature);

        // Run the mod.
        charmony.run(Side.Client);
    }
}
