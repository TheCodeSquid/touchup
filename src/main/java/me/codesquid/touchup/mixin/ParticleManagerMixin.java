package me.codesquid.touchup.mixin;

import me.codesquid.touchup.util.TouchUpParticleSheets;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Shadow
    @Final
    @Mutable
    private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addTextureSheets(CallbackInfo ci) {
        var sheets = new ArrayList<>(PARTICLE_TEXTURE_SHEETS);
        sheets.add(TouchUpParticleSheets.PARTICLE_SHEET_DOUBLE_SIDED);
        PARTICLE_TEXTURE_SHEETS = sheets;
    }
}
