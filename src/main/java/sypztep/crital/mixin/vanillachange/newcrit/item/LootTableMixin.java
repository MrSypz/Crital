package sypztep.crital.mixin.vanillachange.newcrit.item;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.init.ModConfig;
import sypztep.crital.common.util.CritalDataUtil;

import java.util.List;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin {
    @Inject(method = "method_331", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 0))
    private static void processStacksMixin(ServerWorld serverWorld, Consumer<ItemStack>  consumer, ItemStack stack, CallbackInfo ci) {
        if (!serverWorld.isClient() && ModConfig.CONFIG.genCritData) {
            if (stack.getItem() instanceof ToolItem toolItem) {
                ToolMaterial material = toolItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof RangedWeaponItem) {
                CritalDataUtil.applyCritData(stack, ToolMaterials.GOLD, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getArmorCritChance);
            }
        }
    }

    @Inject(method = "method_331", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void processStacksMixin(ServerWorld world, Consumer<ItemStack> lootConsumer, ItemStack stack, CallbackInfo info, int i, ItemStack itemStack2) {
        if (!world.isClient() && ModConfig.CONFIG.genCritData) {
            if (stack.getItem() instanceof ToolItem toolItem) {
                ToolMaterial material = toolItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof RangedWeaponItem) {
                CritalDataUtil.applyCritData(stack,ToolMaterials.GOLD, CritData::getToolCritChance);
            } else if (stack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                CritalDataUtil.applyCritData(stack, material, CritData::getArmorCritChance);
            }
        }
    }

    @Inject(method = "supplyInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V", ordinal = 1), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void supplyInventoryMixin(Inventory inventory, LootContextParameterSet parameters, long seed, CallbackInfo ci, LootContext lootContext, ObjectArrayList<ItemStack> objectArrayList, Random random, List<Integer> list, ObjectListIterator var9, ItemStack itemStack) {
        if (!lootContext.getWorld().isClient() && ModConfig.CONFIG.genCritData) {
            if (itemStack.getItem() instanceof ToolItem toolItem) {
                ToolMaterial material = toolItem.getMaterial();
                CritalDataUtil.applyCritData(itemStack, material, CritData::getToolCritChance);
            } else if (itemStack.getItem() instanceof RangedWeaponItem) {
                CritalDataUtil.applyCritData(itemStack,ToolMaterials.GOLD, CritData::getToolCritChance);
            } else if (itemStack.getItem() instanceof ArmorItem armorItem) {
                RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
                CritalDataUtil.applyCritData(itemStack, material, CritData::getArmorCritChance);
            }
        }
    }
}
