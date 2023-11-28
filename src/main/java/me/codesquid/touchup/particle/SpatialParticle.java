package me.codesquid.touchup.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaterniond;
import org.joml.Vector2f;
import org.joml.Vector3d;

public abstract class SpatialParticle extends Particle {
    protected float scale = 1F;
    protected Quaterniond prevRotation;
    protected Quaterniond rotation;
    protected boolean backFace = false;

    protected SpatialParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public SpatialParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        var cameraPos = camera.getPos();

        var x = MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.x;
        var y = MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.y;
        var z = MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.z;
        var rotation = this.prevRotation.slerp(this.rotation, tickDelta);

        var minU = this.getMinU();
        var minV = this.getMinV();
        var maxU = this.getMaxU();
        var maxV = this.getMaxV();
        var light = this.getBrightness(tickDelta);
        var size = this.getSize(tickDelta);

        var vertices = new Vector3d[]{
            new Vector3d(-1, -1, 0),
            new Vector3d(-1, 1, 0),
            new Vector3d(1, 1, 0),
            new Vector3d(1, -1, 0)
        };
        var uvs = new Vector2f[]{
            new Vector2f(minU, maxV),
            new Vector2f(minU, minV),
            new Vector2f(maxU, minV),
            new Vector2f(maxU, maxV)
        };

        for (var i = 0; i < vertices.length; i++) {
            var vertex = vertices[i];
            var uv = uvs[i];
            drawVertex(vertexConsumer, vertex, uv, x, y, z, rotation, light, size);
        }
    }

    private void drawVertex(VertexConsumer vertexConsumer, Vector3d vertex, Vector2f uv, double x, double y, double z, Quaterniond rotation, int light, float size) {
        vertex.rotate(rotation);
        vertex.mul(size);
        vertex.add(x, y, z);

        vertexConsumer
            .vertex(vertex.x(), vertex.y(), vertex.z())
            .uv(uv.x(), uv.y())
            .color(this.colorRed, this.colorBlue, this.colorGreen, this.colorAlpha)
            .light(light)
            .next();
    }

    public float getSize(float tickDelta) {
        return this.scale;
    }

    @Override
    public Particle scale(float scale) {
        this.scale *= scale;
        return super.scale(scale);
    }

    protected abstract float getMinU();

    protected abstract float getMinV();

    protected abstract float getMaxU();

    protected abstract float getMaxV();
}
