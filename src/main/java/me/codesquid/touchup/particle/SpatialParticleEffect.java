package me.codesquid.touchup.particle;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import org.joml.Quaternionf;

public abstract class SpatialParticleEffect implements ParticleEffect {
    private final Quaternionf rotation;

    public SpatialParticleEffect(Quaternionf rotation) {
        this.rotation = rotation;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeQuaternionf(this.rotation);
    }

    @Override
    public String asString() {
        var id = Registries.PARTICLE_TYPE.getId(this.getType());
        assert id != null;
        return id.toString();
    }
}
