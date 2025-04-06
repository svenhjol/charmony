package svenhjol.charmony.core.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.common.ContainerMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
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

    public <BE extends BlockEntity> Registerable<Void> blockEntityRenderer(Supplier<BlockEntityType<BE>> supplier, Supplier<BlockEntityRendererProvider<BE>> provider) {
        return new Registerable<>(feature, () -> {
            BlockEntityRenderers.register(supplier.get(), provider.get());
            return null;
        });
    }

    public <B extends Block> Registerable<Void> blockRenderType(Supplier<B> block, Supplier<RenderType> renderType) {
        return new Registerable<>(feature, () -> {
            BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderType.get());
            return null;
        });
    }

    public void blockColor(BlockColor blockColor, List<? extends Block> blocks) {
        ColorProviderRegistry.BLOCK.register(blockColor, blocks.stream().toList().toArray(Block[]::new));
    }

    public <E extends Entity> Registerable<Void> entityRenderer(EntityType<? extends E> entity, EntityRendererProvider<E> provider) {
        return new Registerable<>(feature, () -> {
            EntityRendererRegistry.register(entity, provider);
            return null;
        });
    }

    /**
     * May be run late. Use this to conditionally add and item to the creative menu if the feature is enabled.
     */
    public <I extends ItemLike> void itemTab(I item, ResourceKey<CreativeModeTab> key, ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.addAfter(showAfter, item));
        } else {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.accept(item));
        }
    }

    public <M extends ContainerMenu, S extends Screen & MenuAccess<M>> Registerable<?> menuScreen(Supplier<MenuType<M>> menu,
                                                                                                  Supplier<MenuScreens.ScreenConstructor<M, S>> screen) {
        return new Registerable<>(feature, () -> {
            MenuScreens.register(menu.get(), screen.get());
            return null;
        });
    }

    public Registerable<ModelLayerLocation> modelLayer(Supplier<ModelLayerLocation> location, Supplier<LayerDefinition> definition) {
        return new Registerable<>(feature, () -> {
            EntityModelLayerRegistry.registerModelLayer(location.get(), definition::get);
            return location.get();
        });
    }

    /**
     * Register a callback when the client receives a packet from the server.
     * @param type Packet type.
     * @param handler Callback.
     * @return Empty registerable.
     * @param <P> Payload class.
     */
    public <P extends CustomPacketPayload> Registerable<Void> packetReceiver(CustomPacketPayload.Type<P> type, Supplier<BiConsumer<Player, P>> handler) {
        return new Registerable<>(feature, () -> {
            ClientPlayNetworking.registerGlobalReceiver(type,
                (payload, context) -> context.client().execute(
                    () -> handler.get().accept(context.player(), payload)));
            return null;
        });
    }

    public DeferredParticle particle(SimpleParticleType type, ParticleEngine.SpriteParticleRegistration<SimpleParticleType> registration) {
        var deferred = new DeferredParticle(type, registration);
        PARTICLES.add(deferred);
        return deferred;
    }
}
