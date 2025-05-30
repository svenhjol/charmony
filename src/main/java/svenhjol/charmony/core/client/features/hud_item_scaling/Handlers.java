package svenhjol.charmony.core.client.features.hud_item_scaling;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.core.base.Setup;

import java.util.Map;
import java.util.WeakHashMap;

public class Handlers extends Setup<HudItemScaling> {
    private ItemStackRenderState stateHolder = null;
    private final Map<ItemStackRenderState, ItemStack> targets = new WeakHashMap<>();

    public Handlers(HudItemScaling feature) {
        super(feature);
    }

    public void setStateHolder(ItemStackRenderState state) {
        this.stateHolder = state;
    }

    public void setTarget(ItemStackRenderState state, ItemStack stack) {
        targets.put(state, stack);
    }

    public void tryScaleItem(PoseStack poseStack) {
        if (stateHolder != null && targets.containsKey(stateHolder)) {
            var stack = targets.get(stateHolder);
            HudItemScaling.feature().registers.getHudRenderers().forEach(
                hud -> hud.scaleItem(stack, poseStack));

            targets.remove(stateHolder);
            stateHolder = null;
        }
    }
}
