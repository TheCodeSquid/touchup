package me.codesquid.touchup.registry;

import me.codesquid.touchup.TouchUp;
import me.codesquid.touchup.particle.FlameStreakParticle;
import me.codesquid.touchup.particle.FlameStreakParticleEffect;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class TouchUpParticles {
    private static final List<Entry<? extends ParticleEffect>> FACTORIES = new ArrayList<>();

    public static final ParticleType<FlameStreakParticleEffect> FLAME_STREAK = register(
        "flame_streak",
        FabricParticleTypes.complex(FlameStreakParticleEffect.FACTORY),
        FlameStreakParticle.Factory::new
    );

    public static void init() {
        var registry = ParticleFactoryRegistry.getInstance();
        FACTORIES.forEach(entry -> entry.register(registry));
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleType<T> type, ParticleFactoryRegistry.PendingParticleFactory<T> factory) {
        var particle = Registry.register(Registries.PARTICLE_TYPE, TouchUp.id(name), type);
        FACTORIES.add(new Entry<>(particle, factory));
        return particle;
    }

    private record Entry<T extends ParticleEffect>(ParticleType<T> type, ParticleFactoryRegistry.PendingParticleFactory<T> factory) {
        void register(ParticleFactoryRegistry registry) {
            registry.register(this.type, this.factory);
        }
    }
}
