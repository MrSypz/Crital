package sypztep.crital.common.data;

public record CritalItemData(
String itemId,
float baseCritChance,
float baseCritDamage,
int healthExtra,
float minCritChanceMultiply,
float maxCritChanceMultiply,
float minCritDamageMultiply,
float maxCritDamageMultiply
) {
}
