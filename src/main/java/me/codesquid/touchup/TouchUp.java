package me.codesquid.touchup;

import me.codesquid.touchup.particle.FlameStreakParticleEffect;
import me.codesquid.touchup.registry.TouchUpParticles;
import me.codesquid.touchup.util.PersistentProjectileEntityExt;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchUp implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        TouchUpParticles.init();
    }

    public static Identifier id(String path) {
        return new Identifier("touchup", path);
    }

    public static boolean shouldRenderFireTrail(PersistentProjectileEntity entity) {
        final var threshold = 0.75;

        var world = entity.getWorld();
        var velocitySq = entity.getVelocity().lengthSquared();
        var mixin = (PersistentProjectileEntityExt) entity;

        return world.isClient
            && !mixin.inGround()
            && entity.isOnFire()
            && (mixin.hadFireTrail() || velocitySq >= Math.pow(threshold, 2));
    }

    public static void renderFireTrail(PersistentProjectileEntity entity) {
        if (!shouldRenderFireTrail(entity)) {
            ((PersistentProjectileEntityExt) entity).setHadFireTrail(false);
            return;
        }
        ((PersistentProjectileEntityExt) entity).setHadFireTrail(true);

        var world = entity.getWorld();
        var tickDelta = MinecraftClient.getInstance().getTickDelta();
        var pos = entity.getLerpedPos(tickDelta).toVector3f();
        var direction = entity.getVelocity().toVector3f().normalize();

        float pitch = direction.length() == 0F ? 0F : (float) Math.asin(direction.y / direction.length());
        float yaw = (direction.x == 0F || direction.z == 0F) ? 0F : (float) Math.atan2(direction.z, direction.x);

        var rotation = new Quaternionf();

        rotation.rotateY(-yaw);
        rotation.rotateZ(pitch);

        var count = world.random.nextInt(3) + (int) entity.getVelocity().length() * 2;

        for (var i = 0; i < count; i++) {
            var angle = world.random.nextFloat() * (float) Math.PI * 2F;

            var spin = new Quaternionf(rotation).rotateX(angle);
            var offset = new Vector3f(0, 0, 0.2F)
                .rotate(spin)
                .add(pos);

            var particle = new FlameStreakParticleEffect(spin);
            world.addParticle(particle, true, offset.x, offset.y, offset.z, 0, 0, 0);
        }
    }
}
