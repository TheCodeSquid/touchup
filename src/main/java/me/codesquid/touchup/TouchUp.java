package me.codesquid.touchup;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.resource.Material;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class TouchUp implements ClientModInitializer {
    public static final Material FLAME = new Material(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, id("block/flame"));

    @Override
    public void onInitializeClient(ModContainer mod) {
        ModelLoadingPlugin.register(ctx -> ctx.addModels(id("block/flame")));
    }

    private static Identifier id(String path) {
        return new Identifier("touchup", path);
    }

    public static void renderFlame(
        float tickDelta,
        MatrixStack matrices,
        VertexConsumerProvider vertexConsumers,
        PersistentProjectileEntity entity
    ) {
        var sprite = FLAME.getSprite();

        matrices.push();

        matrices.scale(0.7F, 0.7F, 0.7F);
        matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90F));
        matrices.multiply(Axis.Z_POSITIVE.rotationDegrees(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90F));

        var age = ((float) entity.age) * 15F;
        matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(45F - MathHelper.lerp(tickDelta, age - 1F, age)));

        for (var i = 0; i < 4; i++) {
            var minU = sprite.getMinU();
            var minV = sprite.getMinV();
            var maxU = sprite.getMaxU();
            var maxV = sprite.getMaxV();

            matrices.multiply(Axis.Y_POSITIVE.rotationDegrees(90F));

            var vertices = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
            var entry = matrices.peek();
            drawFlameVertex(entry, vertices, 0.5F, 0.0F, 0F, maxU, maxV);
            drawFlameVertex(entry, vertices, -0.5F, 0.0F, 0F, minU, maxV);
            drawFlameVertex(entry, vertices, -0.5F, 1.4F, -0.4F, minU, minV);
            drawFlameVertex(entry, vertices, 0.5F, 1.4F, -0.4F, maxU, minV);

            drawFlameVertex(entry, vertices, 0.5F, 0.0F, 0F, maxU, maxV);
            drawFlameVertex(entry, vertices, -0.5F, 0.0F, 0F, minU, maxV);
            drawFlameVertex(entry, vertices, -0.5F, 1.4F, 0.4F, minU, minV);
            drawFlameVertex(entry, vertices, 0.5F, 1.4F, 0.4F, maxU, minV);
        }

        matrices.pop();
    }

    private static void drawFlameVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(entry.getModel(), x, y, z).color(0xFFFFFF).uv(u, v).overlay(0, 10).light(240).normal(entry.getNormal(), 0F, 1F, 0F).next();
    }
}
