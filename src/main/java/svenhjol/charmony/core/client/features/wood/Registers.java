package svenhjol.charmony.core.client.features.wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.client.ClientRegistry;
import svenhjol.charmony.core.common.features.wood.CustomWood;
import svenhjol.charmony.core.common.features.wood.WoodRegistry;

import java.util.ArrayList;
import java.util.List;

public class Registers extends Setup<Wood> {
    public Registers(Wood feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        var clientRegistry = ClientRegistry.forFeature(feature());

        return () -> {
            // Register models for custom boats.
            for (var boat : WoodRegistry.BOATS) {
                var materialName = boat.get().material().getSerializedName();

                clientRegistry.modelLayer(
                    () -> new ModelLayerLocation(feature().id("boat/" + materialName), "main"),
                    BoatModel::createBoatModel);

                clientRegistry.modelLayer(
                    () -> new ModelLayerLocation(feature().id("chest_boat/" + materialName), "main"),
                    BoatModel::createChestBoatModel);
            }

            // Register the woodtype for custom signs.
            for (var sign : WoodRegistry.SIGNS) {
                var woodType = sign.get().material().woodType();

                clientRegistry.signMaterial(() -> woodType);
            }

            // Add all custom items to creative tabs.
            var table = WoodRegistry.ITEM_CREATIVE_TABS;
            var map = table.rowMap();

            for (var item : map.keySet()) {
                for (var entry : map.get(item).entrySet()) {
                    var after = entry.getKey();
                    var customType = entry.getValue();
                    List<ResourceKey<CreativeModeTab>> tabs = new ArrayList<>();

                    if (CustomWood.BUILDING_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.BUILDING_BLOCKS);
                    }
                    if (CustomWood.FUNCTIONAL_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.FUNCTIONAL_BLOCKS);
                    }
                    if (CustomWood.NATURAL_BLOCKS.contains(customType)) {
                        tabs.add(CreativeModeTabs.NATURAL_BLOCKS);
                    }
                    if (CustomWood.TOOLS_AND_UTILITIES.contains(customType)) {
                        tabs.add(CreativeModeTabs.TOOLS_AND_UTILITIES);
                    }

                    for (var tab : tabs) {
                        clientRegistry.itemTab(item.get(), tab, after.get());
                    }
                }
            }
        };
    }
}
