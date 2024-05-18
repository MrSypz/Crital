package sypztep.crital.common.data;

public enum CritTier {
    COMMON("Common", 1.25),
    UNCOMMON("Uncommon", 1.5),
    RARE("Rare", 1.75),
    EPIC("Epic", 2),
    LEGENDARY("Legendary", 2.5),
    MYTHIC("Mythic", 3),
    CELESTIAL("Celestial", 4);

    private final String name;
    private final double multiplier;

    CritTier(String name, double multiplier) {
        this.name = name;
        this.multiplier = multiplier;
    }

    public String getName() {
        return name;
    }

    public double getMultiplier() {
        return multiplier;
    }
}

