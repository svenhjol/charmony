package svenhjol.charmony.core.client.control_panel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import svenhjol.charmony.core.base.Mod;

import java.util.ArrayList;
import java.util.List;

public class ModsList extends AbstractSelectionList<ModsList.Entry> {
    private final ModsScreen parent;
    private final List<Entry> entries = new ArrayList<>();
    private final List<Mod> mods = new ArrayList<>();

    public ModsList(Minecraft minecraft, ModsScreen parent) {
        super(minecraft, parent.width, parent.layout().getContentHeight(), parent.layout().getHeaderHeight(), 25);
        this.parent = parent;

        for (var mod : mods()) {
            // Don't show a button for the mod if it has no features.
            if (mod.features().isEmpty()) continue;

            var entry = new Entry(mod);
            entries.add(entry);
            addEntry(entry); // The vanilla method.
        }
    }

    public void refreshState() {
        entries.forEach(Entry::refreshState);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        // no op
    }

    @Override
    protected int getMaxPosition() {
        return super.getMaxPosition() + SettingsScreen.CONTENT_BOTTOM_MARGIN;
    }

    @Override
    public int getRowWidth() {
        return SettingsScreen.CONTENT_ROW_WIDTH;
    }

    protected List<Mod> mods() {
        if (mods.isEmpty()) {
            mods.addAll(Mod.all());
        }
        return mods;
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Mod mod;
        private final Button modButton;
        private final Tooltip modTooltip;

        public Entry(Mod mod) {
            this.mod = mod;
            this.modButton = Button.builder(Component.literal(mod.name()),
                button -> configure()).build();
            this.modTooltip = Tooltip.create(Component.literal(mod.description()));
            this.modButton.setTooltip(modTooltip);
        }

        public void refreshState() {
            // no op, yet
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            y += SettingsScreen.CONTENT_TOP_MARGIN;

//            var font = ModsList.this.minecraft.font;
            int buttonX = ModsList.this.width / 2 - (modButton.getWidth() / 2);
            int buttonY = y - 2;
            modButton.setPosition(buttonX, buttonY);
            modButton.render(guiGraphics, mouseX, mouseY, tickDelta);
        }

        /**
         * We must implement our own behavior here or the scrolling causes erroneous button clicks.
         */
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (modButton.isMouseOver(mouseX, mouseY)) {
                modButton.mouseClicked(mouseX, mouseY, button);
                return false;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void configure() {
            minecraft.setScreen(new FeaturesScreen(mod, parent));
        }
    }
}
