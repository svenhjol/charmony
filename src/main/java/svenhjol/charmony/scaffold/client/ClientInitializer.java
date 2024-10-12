package svenhjol.charmony.scaffold.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.client.diagnostics.Diagnostics;
import svenhjol.charmony.scaffold.client.settings.FeaturesScreen;
import svenhjol.charmony.scaffold.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    private static boolean initialized = false;

    private boolean hasRenderedSettingsButton = false;
    private Button settingsButton;
    private KeyMapping openSettingsKey;

    @Override
    public void onInitializeClient() {
        init();
        
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.charmony.settings.openSettings",
            GLFW.GLFW_KEY_M,
            "key.categories.misc"));

        ClientTickEvents.END_CLIENT_TICK.register(this::clientTick);
    }

    private void clientTick(Minecraft minecraft) {
        if (minecraft.player != null) return;
        var parent = minecraft.screen;

        if (parent instanceof TitleScreen && !hasRenderedSettingsButton) {
            settingsButton = Button.builder(Component.translatable("key.charmony.settings.openSettings"), b -> {
                minecraft.setScreen(new FeaturesScreen(Charmony.ID, parent));
            }).build();
            parent.addRenderableWidget(settingsButton);
        }

    }

    /**
     * We expose init() so that child mods can ensure that Charmony gets launched first.
     */
    public static void init() {
        if (initialized) return;

        // Setup and run the mod.
        var charmony = Charmony.instance();
        charmony.addFeature(Diagnostics.class);
        charmony.run(Side.Client);

        initialized = true;
    }
}
