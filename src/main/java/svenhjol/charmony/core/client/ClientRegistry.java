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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ClientRegistry {
    private static final List<DeferredParticle> PARTICLES = new ArrayList<>();

    private static ClientRegistry instance;

    public static ClientRegistry instance() {
        if (instance == null) {
            instance = new ClientRegistry();
        }
        return instance;
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

    public List<DeferredParticle> particles() {
        return PARTICLES;
    }

    public DeferredParticle particle(SimpleParticleType type, ParticleEngine.SpriteParticleRegistration<SimpleParticleType> registration) {
        var deferred = new DeferredParticle(type, registration);
        PARTICLES.add(deferred);
        return deferred;
    }
}
