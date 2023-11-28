package me.codesquid.touchup.particle;

import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;

public abstract class SpriteSpatialParticle extends SpatialParticle {
    protected Sprite sprite;

    public SpriteSpatialParticle(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public SpriteSpatialParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
    }

    protected void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void setSprite(SpriteProvider provider) {
        this.setSprite(provider.getRandom(this.random));
    }

    public void setSpriteForAge(SpriteProvider provider) {
        if (!this.dead) {
            this.setSprite(provider.getSprite(this.age, this.maxAge));
        }
    }

    @Override
    protected float getMinU() {
        return this.sprite.getMinU();
    }

    @Override
    protected float getMinV() {
        return this.sprite.getMinV();
    }

    @Override
    protected float getMaxU() {
        return this.sprite.getMaxU();
    }

    @Override
    protected float getMaxV() {
        return this.sprite.getMaxV();
    }
}
