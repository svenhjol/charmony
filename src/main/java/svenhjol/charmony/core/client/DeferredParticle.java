package svenhjol.charmony.core.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;

public record DeferredParticle(
    SimpleParticleType type,
    ParticleEngine.SpriteParticleRegistration<SimpleParticleType> registration
) { }
