package svenhjol.charmony.core.client;

import net.minecraft.network.chat.Component;

public final class CoreResources {
    public static final Component CLOSE = Component.translatable("gui.charmony.labels.close");
    public static final Component BACK = Component.translatable("gui.charmony.labels.back");
    public static final Component SAVE = Component.translatable("gui.charmony.labels.save");
    public static final Component CANCEL = Component.translatable("gui.charmony.labels.cancel");
    public static final Component DELETE = Component.translatable("gui.charmony.labels.delete");

    /** @deprecated */
    @Deprecated public static final Component NEXT_PAGE = Component.translatable("gui.charmony.labels.nextPage");

    /** @deprecated */
    @Deprecated public static final Component PREVIOUS_PAGE = Component.translatable("gui.charmony.labels.previousPage");
}
