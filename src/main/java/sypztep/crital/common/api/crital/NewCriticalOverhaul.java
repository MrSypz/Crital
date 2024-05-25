package sypztep.crital.common.api.crital;

import java.util.Random;

public interface NewCriticalOverhaul {

    void setCritical(boolean setCrit);

    boolean isCritical();
    default float calculateCritDamage(float amount) {
        float totalCritRate = this.getTotalCritRate();
        float totalCritDMG = this.getTotalCritDamage();
        if (!this.storeCrit().isCritical() && (!(totalCritDMG > 0.0F) || !(totalCritRate > 0.0F) || !(totalCritRate >= 100.0F) && !(this.getRand().nextFloat() < totalCritRate / 100.0F))) {
            return amount;
        } else {
            this.storeCrit().setCritical(true);
            return amount * (100.0F + totalCritDMG) / 100.0F;
        }
    }

    Random getRand();

    default NewCriticalOverhaul storeCrit() {
        return this;
    }
    default float getTotalCritRate() {
        return this.getCritRate() + this.getCritRateFromEquipped();
    }
    default float getTotalCritDamage() {
        return this.getCritDamage() + this.getCritDamageFromEquipped();
    }
    default void setCritRate(float critRate) {
    }
    default void setCritDamage(float critDamage) {
    }
    default float getCritRate() { //Crit rate = 0
        return 0.0F;
    }
    default float getCritDamage() {
        return 50.0F; //Crit damage = 50%
    }
    default float getCritRateFromEquipped() {
        return 0.0F; //Crit rate = 0%
    }
    default float getCritDamageFromEquipped() {
        return 0.0F;
    }
}
