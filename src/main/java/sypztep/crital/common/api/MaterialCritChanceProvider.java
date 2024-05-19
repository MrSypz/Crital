package sypztep.crital.common.api;

@FunctionalInterface
public interface MaterialCritChanceProvider<T> {
    float getCritChance(T material);
}