package svenhjol.charmony.scaffold.client;

import svenhjol.charmony.scaffold.Charmony;
import svenhjol.charmony.scaffold.base.Log;

public class ClientEvents {
    private static final Log LOGGER = new Log(Charmony.ID, "ClientEvents");
    private static boolean initialized = false;

    private ClientEvents() {}

    public static void init() {
        if (initialized) {
            throw new RuntimeException("Trying to initialize client events more than once");
        }

        // FUTURE: add one-pass client event registering here.

        LOGGER.info("Finished initialising client events.");
        initialized = true;
    }
}
