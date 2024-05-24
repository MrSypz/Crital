package sypztep.crital.common.api.crital;

@FunctionalInterface
public interface MaterialCritChanceProvider<T> {
    float getCritChance(T material);
}