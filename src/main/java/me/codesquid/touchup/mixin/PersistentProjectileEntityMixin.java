package me.codesquid.touchup.mixin;

import me.codesquid.touchup.TouchUp;
import me.codesquid.touchup.particle.FlameStreakParticleEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        if (!entity.getWorld().isClient()) return;

        if (TouchUp.shouldShowFlameStreak(entity))
            this.flameStreakParticles();
    }

    @Unique
    private void flameStreakParticles() {
        var entity = (PersistentProjectileEntity) (Object) this;
        var world = entity.getWorld();
        var pos = entity.getPos().toVector3f();

        var velocity = entity.getVelocity().toVector3f();
        var direction = new Vector3f(velocity).normalize();
        var pitch = Math.atan2(direction.y, Math.sqrt(Math.pow(direction.x, 2) + Math.pow(direction.z, 2)));
        var yaw = Math.atan2(direction.z, direction.x);

        // TODO: use config
        final var count = 10;
        for (var i = 0; i < count; i++) {
            var rotation = new Quaternionf()
                .rotateY((float) -yaw)
                .rotateZ((float) pitch)
                .rotateX(world.getRandom().nextFloat() * (float) Math.PI * 2);

            var offset = new Vector3f(pos)
                .add(
                    new Vector3f(1F, 0F, 0F).mul(-velocity.length() * ((float) i / count))
                        .add(new Vector3f(0F, 0F, 0.1F))
                        .rotate(rotation)
                );

            var particle = new FlameStreakParticleEffect(rotation);
            world.addParticle(particle, offset.x, offset.y, offset.z, 0, 0, 0);
        }
    }
}
