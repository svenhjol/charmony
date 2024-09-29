package svenhjol.charmony.scaffold.nano.client.compasses_show_position;

import svenhjol.charmony.scaffold.nano.Mod;
import svenhjol.charmony.scaffold.nano.ModFeature;
import svenhjol.charmony.scaffold.nano.annotations.Configurable;
import svenhjol.charmony.scaffold.nano.annotations.Feature;
import svenhjol.charmony.scaffold.nano.enums.Side;

@Feature(side = Side.Client, description = """
    Shows the direction that the player is facing, the position coordinates and current biome when holding a compass.""")
public class CompassesShowPosition extends ModFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    @Configurable(
        name = "Show facing",
        description = "If true, shows the cardinal direction that the player is facing.",
        requireRestart = false
    )
    private static boolean showFacing = true;

    @Configurable(
        name = "Show co-ordinates",
        description = "If true, shows the player's XYZ coordinates.",
        requireRestart = false
    )
    private static boolean showCoords = true;

    @Configurable(
        name = "Show biome",
        description = "If true, shows the player's biome.",
        requireRestart = false
    )
    private static boolean showBiome = true;

    @Configurable(
        name = "Only show X and Z",
        description = "If true, only show the player's X and Z coordinates (not their height/depth).",
        requireRestart = false
    )
    private static boolean onlyShowXZ = false;

    @Configurable(
        name = "Only show when sneaking",
        description = "If true, only show the compass overlay if the player is sneaking.",
        requireRestart = false
    )
    private static boolean onlyShowWhenSneaking = false;

    @Configurable(
        name = "Always show",
        description = "If true, the overlay will always be shown even if the player is not holding a compass.",
        requireRestart = false
    )
    private static boolean alwaysShow = false;

    public boolean showFacing() {
        return showFacing;
    }

    public boolean showCoords() {
        return showCoords;
    }

    public boolean showBiome() {
        return showBiome;
    }

    public boolean onlyShowXZ() {
        return onlyShowXZ;
    }

    public boolean onlyShowWhenSneaking() {
        return onlyShowWhenSneaking;
    }

    public boolean alwaysShow() {
        return alwaysShow;
    }

    public CompassesShowPosition(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }
}
