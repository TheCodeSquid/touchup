package me.codesquid.touchup.mixin;

import me.codesquid.touchup.TouchUp;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow
    protected abstract void renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity);

    @Inject(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderFire(" +
            "Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/VertexConsumerProvider;" +
            "Lnet/minecraft/entity/Entity;" +
            ")V"
    ))
    private <E extends Entity> void renderFlame(
        E entity,
        double x,
        double y,
        double z,
        float yaw,
        float tickDelta,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        int light,
        CallbackInfo ci
    ) {
        final var threshold = 0.75;

        var isArrow = entity instanceof ArrowEntity || entity instanceof SpectralArrowEntity;
        if (!isArrow) {
            this.renderFire(matrices, vertexConsumers, entity);
            return;
        }

        var speedSquared = entity.getVelocity().lengthSquared();
        var thresholdSquared = Math.pow(threshold, 2.0);

        var shouldReplace = !((PersistentProjectileEntityAccessor) entity).getInGround() && speedSquared >= thresholdSquared;

        if (shouldReplace)
            TouchUp.renderFlame(tickDelta, matrices, vertexConsumers, (PersistentProjectileEntity) entity);
        else
            this.renderFire(matrices, vertexConsumers, entity);
    }

    @Redirect(method = "render", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;renderFire(" +
            "Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/VertexConsumerProvider;" +
            "Lnet/minecraft/entity/Entity;" +
            ")V"
    ))
    private void skipRenderFire(
        EntityRenderDispatcher dispatcher,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        Entity entity
    ) {}
}
