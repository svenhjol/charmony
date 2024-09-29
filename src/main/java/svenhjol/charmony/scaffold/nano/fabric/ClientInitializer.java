package svenhjol.charmony.scaffold.nano.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charmony.scaffold.nano.Charmony;
import svenhjol.charmony.scaffold.nano.client.ClientEvents;
import svenhjol.charmony.scaffold.nano.client.compasses_show_position.CompassesShowPosition;
import svenhjol.charmony.scaffold.nano.client.repair_cost_visible.RepairCostVisible;
import svenhjol.charmony.scaffold.nano.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEvents.init();

        // Test data follows.
        var charmony = Charmony.instance();
        var feature = new RepairCostVisible(charmony);

        // Add features.
        charmony.addFeature(new CompassesShowPosition(charmony));
        charmony.addFeature(new RepairCostVisible(charmony));

        // Run the mod.
        charmony.run(Side.Client);
    }
}
