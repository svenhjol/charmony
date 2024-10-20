package svenhjol.charmony.core.client.control_panel;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import svenhjol.charmony.core.base.Feature;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.helper.TextHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeaturesList extends AbstractSelectionList<FeaturesList.Entry> {
    private static final MutableComponent FEATURE_IS_DISABLED = Component.translatable("gui.charmony.settings.feature.disabled");
    private static final MutableComponent NOT_USING_DEFAULTS = Component.translatable("gui.charmony.settings.not_using_defaults");
    private final FeaturesScreen parent;
    private final Mod mod;
    private final List<Entry> entries = new ArrayList<>();
    private final List<Feature> features = new ArrayList<>();

    public FeaturesList(Minecraft minecraft, Mod mod, FeaturesScreen parent) {
        super(minecraft, parent.width, parent.layout().getContentHeight(), parent.layout().getHeaderHeight(), 25);
        this.parent = parent;
        this.mod = mod;

        for (var feature : features()) {
            var entry = new Entry(feature);
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
    public int getRowWidth() {
        return SettingsScreen.CONTENT_ROW_WIDTH;
    }

    @Override
    protected int getMaxPosition() {
        return super.getMaxPosition() + SettingsScreen.CONTENT_BOTTOM_MARGIN;
    }

    protected List<Feature> features() {
        if (features.isEmpty()) {
            features.addAll(mod.features());
        }
        return features;
    }

    public class Entry extends AbstractSelectionList.Entry<Entry> {
        private final Feature feature;
        private final ImageButton enableButton;
        private final ImageButton disableButton;
        private final ImageButton configureButton;
        private final Tooltip enableButtonTooltip;
        private final Tooltip disableButtonTooltip;
        private final Tooltip configureButtonTooltip;

        private boolean hasDefaultValues = false;

        public Entry(Feature feature) {
            this.feature = feature;

            this.enableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.ENABLE_BUTTON, button -> enable());

            this.disableButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.DISABLE_BUTTON, button -> disable());

            this.configureButton = new ImageButton(0, 0, 20, 20,
                FeaturesScreen.CONFIG_BUTTON, button -> configure());

            this.enableButtonTooltip = Tooltip.create(Component.translatable("gui.charmony.settings.enable_feature", this.feature.name()));
            this.disableButtonTooltip = Tooltip.create(Component.translatable("gui.charmony.settings.disable_feature", this.feature.name()));
            this.configureButtonTooltip = Tooltip.create(Component.translatable("gui.charmony.settings.configure_feature", this.feature.name()));

            refreshState();
        }

        public void refreshState() {
            configureButton.visible = true;
            configureButton.active = false;
            disableButton.visible = false;
            disableButton.active = false;
            enableButton.visible = false;
            enableButton.active = false;

            if (!feature.canBeDisabled()) {
                disableButton.visible = true;
                disableButton.active = false;
                enableButton.active = false;
            } else if (feature.enabled()) {
                disableButton.visible = true;
                disableButton.active = true;
            } else {
                enableButton.visible = true;
                enableButton.active = true;
            }

            if (feature.enabled() && feature.mod().config().hasConfiguration(feature))  {
                configureButton.active = true;
            }

            hasDefaultValues = feature.mod().config().hasDefaultValues(feature);
        }

        private void setStateAndUpdate(boolean state) {
            feature.enabled(state);
            writeConfig();
            refreshState();
            FeaturesList.this.parent.requiresRestart();
        }

        private void enable() {
            setStateAndUpdate(true);
        }

        private void disable() {
            setStateAndUpdate(false);
        }

        private void configure() {
            minecraft.setScreen(new FeatureConfigScreen(feature, parent));
        }

        private void writeConfig() {
            feature.mod().config().write();
        }

        /**
         * We must implement our own behavior here or the scrolling causes erroneous button clicks.
         */
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (enableButton.isMouseOver(mouseX, mouseY)) {
                enableButton.mouseClicked(mouseX, mouseY, button);
                return false;
            }
            if (disableButton.isMouseOver(mouseX, mouseY)) {
                disableButton.mouseClicked(mouseX, mouseY, button);
                return false;
            }
            if (configureButton.isMouseOver(mouseX, mouseY)) {
                configureButton.mouseClicked(mouseX, mouseY, button);
                return false;
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int i, int y, int offsetX, int l, int m, int mouseX, int mouseY, boolean bl, float tickDelta) {
            y += SettingsScreen.CONTENT_TOP_MARGIN;

            int enableX = FeaturesList.this.getScrollbarPosition() - enableButton.getWidth() - 10;
            int moreX = enableX - 4 - configureButton.getWidth();
            int buttonY = y - 2;

            var font = FeaturesList.this.minecraft.font;
            var color = feature.enabled() ? 0xffffff : 0x808080; // Mute feature name color if disabled
            var name = Component.literal(feature.name());
            var descriptionLines = TextHelper.toComponents(feature.description(), 48);
            var nameWidth = font.width(name);
            var textLeft = offsetX + 5;
            var textTop = y + 2;

            // Show that the feature is not using default values.
            if (feature.enabled() && !hasDefaultValues) {
                name = name.withStyle(ChatFormatting.YELLOW);
                descriptionLines.add(NOT_USING_DEFAULTS.withStyle(ChatFormatting.YELLOW));
            }

            // Add message to tooltip if feature is disabled.
            if (!feature.enabled()) {
                descriptionLines.add(FEATURE_IS_DISABLED.withStyle(ChatFormatting.DARK_GRAY));
            }

            // Prepend the feature name to the description lines.
            descriptionLines.addFirst(name.copy()
                .withStyle(ChatFormatting.BOLD)
                .withStyle(feature.enabled() ? ChatFormatting.GOLD : ChatFormatting.GRAY));

            guiGraphics.drawString(font, name, offsetX + 5, y + 2, color);

            if (mouseX >= textLeft && mouseX <= textLeft + nameWidth
                && mouseY >= textTop && mouseY <= textTop + 6) {
                guiGraphics.renderTooltip(font, descriptionLines, Optional.empty(), mouseX, mouseY);
            }

            disableButton.setPosition(enableX, buttonY);
            disableButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            enableButton.setPosition(enableX, buttonY);
            enableButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            configureButton.setPosition(moreX, buttonY);
            configureButton.render(guiGraphics, mouseX, mouseY, tickDelta);

            if (disableButton.active) {
                disableButton.setTooltip(disableButtonTooltip);
            }

            if (enableButton.active) {
                enableButton.setTooltip(enableButtonTooltip);
            }

            if (configureButton.active) {
                configureButton.setTooltip(configureButtonTooltip);
            }
        }
    }
}
