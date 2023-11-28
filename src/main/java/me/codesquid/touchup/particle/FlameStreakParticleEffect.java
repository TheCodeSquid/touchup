package me.codesquid.touchup.particle;

import com.mojang.brigadier.StringReader;
import me.codesquid.touchup.registry.TouchUpParticles;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import org.joml.Quaternionf;

public class FlameStreakParticleEffect extends SpatialParticleEffect {
    @SuppressWarnings("deprecation")
    public static final ParticleEffect.Factory<FlameStreakParticleEffect> FACTORY = new ParticleEffect.Factory<>() {
        @Override
        public FlameStreakParticleEffect read(ParticleType<FlameStreakParticleEffect> type, StringReader reader) {
            return null;
        }

        @Override
        public FlameStreakParticleEffect read(ParticleType<FlameStreakParticleEffect> type, PacketByteBuf buf) {
            return null;
        }
    };

    public FlameStreakParticleEffect(Quaternionf rotation) {
        super(rotation);
    }

    @Override
    public ParticleType<?> getType() {
        return TouchUpParticles.FLAME_STREAK;
    }
}
