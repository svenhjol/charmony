package svenhjol.charmony.core.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<Advancements> {
    public final ActionPerformed actionPerformed;

    public Registers(Advancements feature) {
        super(feature);
        actionPerformed = CriteriaTriggers.register("charmony_action_performed", new ActionPerformed());
    }
}
