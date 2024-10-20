package svenhjol.charmony.core.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum Side implements StringRepresentable {
    Client,
    Common,
    Server;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
