package sypztep.crital.common.api.crital;

import java.util.Random;

public interface NewCriticalOverhaul {

    void crital$setCritical(boolean setCrit);

    boolean crital$isCritical();
    default float calculateCritDamage(float amount) {
        float totalCritRate = this.getTotalCritRate();
        float totalCritDMG = this.getTotalCritDamage();
        if (!this.storeCrit().crital$isCritical() && (!(totalCritDMG > 0.0F) || !(totalCritRate > 0.0F) || !(totalCritRate >= 100.0F) && !(this.crital$getRand().nextFloat() < totalCritRate / 100.0F))) {
            return amount;
        } else {
            this.storeCrit().crital$setCritical(true);
            return amount * (100.0F + totalCritDMG) / 100.0F;
        }
    }

    Random crital$getRand();

    default NewCriticalOverhaul storeCrit() {
        return this;
    }
    default float getTotalCritRate() {
        return this.crital$getCritRate() + this.crital$getCritRateFromEquipped();
    }
    default float getTotalCritDamage() {
        return this.crital$getCritDamage() + this.crital$getCritDamageFromEquipped();
    }
    default void crital$setCritRate(float critRate) {
    }
    default void crital$setCritDamage(float critDamage) {
    }
    default float crital$getCritRate() { //Crit rate = 0
        return 0.0F;
    }
    default float crital$getCritDamage() {
        return 50.0F; //Crit damage = 50%
    }
    default float crital$getCritRateFromEquipped() {
        return 0.0F; //Crit rate = 0%
    }
    default float crital$getCritDamageFromEquipped() {
        return 0.0F;
    }
}
