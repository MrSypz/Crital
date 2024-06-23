package sypztep.crital.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import sypztep.crital.client.particle.util.Easing;

public class CritAttackParticle extends SpriteBillboardParticle {
    protected final SpriteProvider spriteProvider;
    private float maxSize = 0.5F;
    protected CritAttackParticle(ClientWorld clientWorld, double d, double e, double f, SpriteProvider spriteProvider) {
        super(clientWorld, d, e, f);
        this.spriteProvider = spriteProvider;
        this.maxAge = 20;
        this.scale = 0.0F;
        this.setSpriteForAge(spriteProvider);
        this.setSprite(spriteProvider);
        this.maxSize = 0.5F + world.random.nextFloat() / 2.0F;
    }
    @Override
    public void tick() {
        if (this.age++ <= 10) {
            this.scale = MathHelper.lerp(Easing.ELASTIC_OUT.ease((float)this.age / 10.0F, 0.0F, 1.0F, 1.0F), 0.0F, this.maxSize);
        } else {
            this.scale = MathHelper.lerp(Easing.CUBIC_IN.ease(((float)this.age - 10.0F) / 10.0F, 0.0F, 1.0F, 1.0F), this.maxSize, 0.0F);
        }

        if (this.age > this.maxAge && this.scale <= 0.0F) {
            this.markDead();
        }

    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    public record Factory(SpriteProvider sprites) implements ParticleFactory<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            // Example usage
            return new CritAttackParticle(world, x, y, z, sprites);
        }
    }
}
