package me.codesquid.touchup;

import me.codesquid.touchup.registry.TouchUpParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class TouchUp implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        TouchUpParticles.init();
    }

    public static Identifier id(String path) {
        return new Identifier("touchup", path);
    }

    public static boolean shouldShowFlameStreak(Entity entity) {
        if (!(entity instanceof PersistentProjectileEntity)) return false;

        return entity.isOnFire() && ((PersistentProjectileEntity) entity).isCritical();
    }
}
