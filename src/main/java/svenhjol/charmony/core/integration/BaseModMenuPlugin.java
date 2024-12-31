package svenhjol.charmony.core.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.client.features.control_panel.FeaturesScreen;

public abstract class BaseModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new FeaturesScreen(mod(), parent);
    }

    public abstract Mod mod();
}
