package svenhjol.charmony.scaffold.client.diagnostics;

import svenhjol.charmony.scaffold.annotations.Configurable;
import svenhjol.charmony.scaffold.annotations.Feature;
import svenhjol.charmony.scaffold.base.Mod;
import svenhjol.charmony.scaffold.base.ModFeature;
import svenhjol.charmony.scaffold.enums.Side;

@Feature(side = Side.Client, canBeDisabled = false, description = """
    Diagnostic tools for Charmony client.""")
public class Diagnostics extends ModFeature {
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

    public Diagnostics(Mod mod) {
        super(mod);
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
