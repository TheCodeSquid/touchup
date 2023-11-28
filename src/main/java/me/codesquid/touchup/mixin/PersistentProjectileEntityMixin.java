package me.codesquid.touchup.mixin;

import me.codesquid.touchup.TouchUp;
import me.codesquid.touchup.util.PersistentProjectileEntityExt;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin implements PersistentProjectileEntityExt {
    @Shadow
    private boolean inGround;
    @Unique
    private boolean hadFireTrail = false;

    @Override
    public boolean inGround() {
        return this.inGround;
    }

    @Override
    public boolean hadFireTrail() {
        return this.hadFireTrail;
    }

    @Override
    public void setHadFireTrail(boolean value) {
        this.hadFireTrail = value;
    }

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        var entity = (PersistentProjectileEntity) (Object) this;

        if (TouchUp.shouldRenderFireTrail(entity))
            TouchUp.renderFireTrail(entity);
    }
}
