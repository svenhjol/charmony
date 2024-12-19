package svenhjol.charmony.core.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public abstract class BaseToast implements Toast {
    private static final Component DEFAULT_DESCRIPTION = Component.empty();
    private static final ItemStack DEFAULT_ICON = ItemStack.EMPTY;
    private static final ResourceLocation DEFAULT_BACKGROUND = 
        ResourceLocation.withDefaultNamespace("toast/advancement");
    private static final int DEFAULT_COLOR = 0xffffff;
    
    private boolean hasPlayedSound = false;
    private Toast.Visibility wantedVisibility = Visibility.HIDE;

    protected abstract Component title();
    
    protected Component description() {
        return DEFAULT_DESCRIPTION;
    }

    @Override
    public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    protected ItemStack icon() {
        return DEFAULT_ICON;
    }

    /**
     * Override for custom background texture.
     */
    protected ResourceLocation background() {
        return DEFAULT_BACKGROUND;
    }

    protected int color() {
        return DEFAULT_COLOR;
    }
    
    protected Optional<SoundEvent> sound() {
        return Optional.empty();
    }
    
    protected long duration() {
        return 5000L;
    }

    @Override
    public void update(ToastManager toastManager, long ticks) {
        var opt = sound();
        if (opt.isPresent() && !this.hasPlayedSound && ticks > 0L) {
            toastManager.getMinecraft().getSoundManager().play(SimpleSoundInstance.forUI(opt.get(), 1.0f, 1.0f));
            this.hasPlayedSound = true;
        }

        this.wantedVisibility = ticks >= (duration() * toastManager.getNotificationDisplayTimeMultiplier()) ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long ticks) {
        var color = color();
        var title = title();
        var description = description();
        var icon = icon();
        guiGraphics.blitSprite(RenderType::guiTextured, background(), 0, 0, this.width(), this.height());

        List<FormattedCharSequence> list = font.split(description, 125);
        if (list.size() == 1) {
            guiGraphics.drawString(font, title, 30, 7, color, false);
            guiGraphics.drawString(font, description, 30, 18, -1, false);
        } else {
            if (ticks < 1500L) {
                int k = Mth.floor(Mth.clamp((float)(1500L - ticks) / 300.0f, 0.0f, 1.0f) * 255.0f) << 24 | 0x4000000;
                guiGraphics.drawString(font, title, 30, 11, color | k);
            } else {
                int k = Mth.floor(Mth.clamp((float)(ticks - 1500L) / 300.0f, 0.0f, 1.0f) * 252.0f) << 24 | 0x4000000;
                int m = this.height() / 2 - list.size() * font.lineHeight / 2;
                for (FormattedCharSequence formattedCharSequence : list) {
                    guiGraphics.drawString(font, formattedCharSequence, 30, m, 0xffffff | k, false);
                    m += font.lineHeight;
                }
            }
        }

        guiGraphics.renderFakeItem(icon, 8, 8);
    }
}
