package svenhjol.charmony.core.common.core;

import svenhjol.charmony.core.Charmony;
import svenhjol.charmony.core.annotations.Configurable;
import svenhjol.charmony.core.annotations.FeatureDefinition;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.enums.Side;

@FeatureDefinition(side = Side.Common, canBeDisabled = false, description = """
    Core functionality and settings.""")
public final class Core extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Networking networking;

    @Configurable(
        name = "Client mode",
        description = "Force client mode. Disables all common and server mixins and features."
    )
    private static boolean clientMode = false;

    @Configurable(
        name = "Debug mode",
        description = "Enable debugging mode. Produces more logging output and adds some testing code."
    )
    private static boolean debugMode = false;

    @Configurable(
        name = "Mixin disable mode",
        description = """
            Enable mixin disable mode. All Charmony mod mixins will be disabled and behavior will be inconsistent.
            Use this only for testing if mods are crashing to determine if Charmony might be the cause."""
    )
    private static boolean mixinsDisableMode = false;

    public Core(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
    }

    public static Core feature() {
        return Charmony.instance().feature(Core.class);
    }

    public boolean clientMode() {
        return clientMode;
    }

    public boolean debugMode() {
        return debugMode;
    }

    public boolean mixinsDisabled() {
        return mixinsDisableMode;
    }

    @Override
    public void run() {
        if (mixinsDisableMode) {
            log().warn("""
            
            
            -------------------------------------------------------------------
                         CHARMONY IS RUNNING IN MIXIN DISABLE MODE
            -------------------------------------------------------------------
            
            No Charmony-related mods will work as expected! This mode is used
            to determine if Charmony mods have mixin conflicts with another mod.
            
            Eliminate mods from your mod pack one by one, testing mixin disable
            mode on and off, to find out where a conflict is happening.
            Then come to Charmony's discord and talk to us.
            
            """);
        }
    }
}
