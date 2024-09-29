package svenhjol.charmony.scaffold.nano.enums;

import java.util.Locale;

public enum Side {
    Client,
    Common,
    Server;

    public String displayName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
