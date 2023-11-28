package me.codesquid.touchup.particle;

import me.codesquid.touchup.util.TouchUpParticleSheets;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class FlameStreakParticle extends SpriteSpatialParticle {
    private final SpriteProvider sprites;

    FlameStreakParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider sprites, Quaterniond rotation) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.scale = 0.3F;
//        this.maxAge = 10;
        this.maxAge = (int) (world.random.nextFloat() * 5F + 2F);

        var velocity = new Vector3d(0, 0, 0.05).rotate(rotation);

        this.velocityX = velocity.x;
        this.velocityY = velocity.y;
        this.velocityZ = velocity.z;

        this.backFace = true;
        this.sprites = sprites;
        this.setSpriteForAge(sprites);

        this.rotation = rotation;
        this.prevRotation = rotation;
    }

    @Override
    public ParticleTextureSheet getType() {
        return TouchUpParticleSheets.PARTICLE_SHEET_DOUBLE_SIDED;
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.sprites);
        this.scale *= 0.9F;
    }

    @Override
    protected int getBrightness(float tint) {
        return 240;
    }

    public record Factory(SpriteProvider sprites) implements ParticleFactory<FlameStreakParticleEffect> {
        @Override
        public Particle createParticle(FlameStreakParticleEffect effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            var rotation = new Quaterniond(effect.getRotation());
            return  new FlameStreakParticle(world, x, y, z, velocityX, velocityY, velocityZ, sprites, rotation);
        }
    }
}
