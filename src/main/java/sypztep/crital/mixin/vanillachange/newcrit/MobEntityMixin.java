package sypztep.crital.mixin.vanillachange.newcrit;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({MobEntity.class})
public abstract class MobEntityMixin extends LivingEntityMixin {

    protected MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    /**
     * This one apply mob to do crit chance and crit damage
     */
    @Inject(method = {"initialize"}, at = {@At("TAIL")})
    private void crital$init(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        float d = difficulty.getClampedLocalDifficulty();
        boolean bl = this.getWorld().getDifficulty() == Difficulty.HARD;
        d = bl ? 1.5F : d;

        float originalCritRate = this.crital$getCritRate();
        float originalCritDamage = this.crital$getCritDamage();
        float randomFloat = this.random.nextFloat();

        float rawNewCritRate = (originalCritRate + 5.0F) + (bl ? 0.40F + randomFloat : 0.0F + randomFloat) * 50.0F * d;
        float rawNewCritDamage = (originalCritDamage) + (bl ? 0.75F + randomFloat : 0.0F + randomFloat) * 50.0F * d;

        float newCritRate = Math.min(100, Math.round(rawNewCritRate * 100.0F) / 100.0F);
        float newCritDamage = Math.round(rawNewCritDamage * 100.0F) / 100.0F;

        this.crital$setCritRate(newCritRate);
        this.crital$setCritDamage(newCritDamage);
    }
}
