package svenhjol.charmony.core.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.base.SidedFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ClientRegistry {
    public static final List<DeferredParticle> PARTICLES = new ArrayList<>();
    private final SidedFeature feature;

    private ClientRegistry(SidedFeature feature) {
        this.feature = feature;
    }

    public static ClientRegistry forFeature(SidedFeature feature) {
        return new ClientRegistry(feature);
    }

    public <T extends BlockEntity> void blockEntityRenderer(Supplier<BlockEntityType<T>> supplier, Supplier<BlockEntityRendererProvider<T>> provider) {
        BlockEntityRenderers.register(supplier.get(), provider.get());
    }

    public <T extends Block> void blockRenderType(Supplier<T> block, Supplier<RenderType> renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType.get());
    }

    /**
     * May be run late. Use this to conditionally add and item to the creative menu if the feature is enabled.
     */
    public <T extends ItemLike> void itemTab(T item, ResourceKey<CreativeModeTab> key, ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.addAfter(showAfter, item));
        } else {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.accept(item));
        }
    }

    public DeferredParticle particle(SimpleParticleType type, ParticleEngine.SpriteParticleRegistration<SimpleParticleType> registration) {
        var deferred = new DeferredParticle(type, registration);
        PARTICLES.add(deferred);
        return deferred;
    }
}
