package svenhjol.charmony.core.client.control_panel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<ControlPanel> {
    public Handlers(ControlPanel feature) {
        super(feature);
    }

    public void clientTick(Minecraft minecraft) {
        if (minecraft.player != null) return;
        var parent = minecraft.screen;

        if (parent instanceof TitleScreen) {
            var button = feature().registers.settingsButton;
            var children = parent.children();
            if (!children.contains(button)) {
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

                parent.addRenderableWidget(button);
                var tooltip = Tooltip.create(Component.translatable("gui.charmony.settings.settings"));
                button.setPosition(x, y);
                button.setTooltip(tooltip);
            }
        }
    }

    public void openSettingsScreen() {
        var minecraft = Minecraft.getInstance();
        var current = minecraft.screen;
        minecraft.setScreen(new ModsScreen(current));
    }
}
