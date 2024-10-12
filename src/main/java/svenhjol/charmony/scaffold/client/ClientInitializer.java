package svenhjol.charmony.scaffold.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.client.diagnostics.Diagnostics;
import svenhjol.charmony.scaffold.client.settings.FeaturesScreen;
import svenhjol.charmony.scaffold.enums.Side;

public class ClientInitializer implements ClientModInitializer {
    private static boolean initialized = false;

    private ImageButton settingsButton;
    private Tooltip settingsButtonTooltip;
    private KeyMapping openSettingsKey;

    @Override
    public void onInitializeClient() {
        init();
        
        openSettingsKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.charmony.settings.openSettings",
            GLFW.GLFW_KEY_M,
            "key.categories.misc"));

        ClientTickEvents.END_CLIENT_TICK.register(this::clientTick);

        settingsButton = new ImageButton(0, 0, 20, 20,
            FeaturesScreen.SETTINGS_BUTTON, button -> openSettingsScreen());
    }

    private void clientTick(Minecraft minecraft) {
        if (minecraft.player != null) return;
        var parent = minecraft.screen;

        if (parent instanceof TitleScreen) {
            var children = parent.children();
            if (!children.contains(settingsButton)) {
                int x;
                int y;

                SpriteIconButton accessibilityButton = children.stream()
                    .filter(b -> b instanceof SpriteIconButton s && s.sprite.getPath().equals("icon/accessibility"))
                    .map(b -> (SpriteIconButton)b)
                    .findFirst()
                    .orElse(null);

                if (accessibilityButton != null) {
                    x = accessibilityButton.getX() + 24;
                    y = accessibilityButton.getY();
                } else {
                    x = (parent.width / 2) + 128;
                    y = (parent.height / 4) + 132;
                }

                parent.addRenderableWidget(settingsButton);
                settingsButtonTooltip = Tooltip.create(Component.translatable("gui.charmony.settings.settings"));
                settingsButton.setPosition(x, y);
                settingsButton.setTooltip(settingsButtonTooltip);
            }
        }
    }

    private void openSettingsScreen() {
        var minecraft = Minecraft.getInstance();
        var current = minecraft.screen;
        minecraft.setScreen(new FeaturesScreen(Charmony.ID, current));
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
