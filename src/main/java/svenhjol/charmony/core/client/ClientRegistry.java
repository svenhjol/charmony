package svenhjol.charmony.core.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.ArrayList;
import java.util.List;

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

    public List<DeferredParticle> particles() {
        return PARTICLES;
    }

    public DeferredParticle particle(SimpleParticleType type, ParticleEngine.SpriteParticleRegistration<SimpleParticleType> registration) {
        var deferred = new DeferredParticle(type, registration);
        PARTICLES.add(deferred);
        return deferred;
    }
}
