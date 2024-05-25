package sypztep.crital.common.screen;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldEvents;
import sypztep.crital.client.packets2c.GrinderPayloadS2C;
import sypztep.crital.client.packets2c.QualityGrinderPayloadS2C;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.init.ModTag;
import sypztep.crital.common.util.CritalDataUtil;

public class GrinderScreenHandler extends ScreenHandler {
    private final Inventory inventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            GrinderScreenHandler.this.onContentChanged(this);
        }
    };
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private boolean canGrind;
    private boolean canQuality;
    private BlockPos pos;

    public GrinderScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(CritalMod.GRINDER_SCREEN_HANDLER_TYPE, syncId);

        this.context = context;
        this.player = playerInventory.player;
        this.addSlot(new Slot(this.inventory, 0, 9, 34));
        this.addSlot(new Slot(this.inventory, 1, 151, 34));

        int i;
        for (i = 0; i < 3; ++i)
            for (int j = 0; j < 9; ++j)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (i = 0; i < 9; ++i)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));

        this.context.run((world, pos) -> GrinderScreenHandler.this.setPos(pos));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (!player.getWorld().isClient() && inventory == this.inventory) {
            this.updateResult();
        }

    }

    private void updateResult() {
        ItemStack grindItem = this.getSlot(1).getStack();
        // Ensure both slots have stacks
        if (this.getSlot(0).hasStack() && this.getSlot(1).hasStack()) {
            Item item = grindItem.getItem();

            ItemStack material = this.getSlot(0).getStack();
            // Check if the item is a tool or has no custom data component
            if ((item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof TridentItem || item instanceof ShieldItem) && grindItem.get(DataComponentTypes.CUSTOM_DATA) == null) {
                this.canGrind = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL);
                this.canQuality = false;
            } else if (item instanceof ArmorItem && grindItem.get(DataComponentTypes.CUSTOM_DATA) == null) {
                this.canGrind = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL);
                this.canQuality = false;
            } else if (item instanceof ToolItem || item instanceof RangedWeaponItem || item instanceof TridentItem || item instanceof ShieldItem) {
                this.canGrind = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL);
                this.canQuality = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL);
            } else if (item instanceof ArmorItem) {
                this.canGrind = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL);
                this.canQuality = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL);
            }
            // Send the payload if no custom data component is present
            if (grindItem.get(DataComponentTypes.CUSTOM_DATA) == null) {
                GrinderPayloadS2C.send((ServerPlayerEntity) player, new GrinderPayloadS2C(!this.canGrind));
                QualityGrinderPayloadS2C.send((ServerPlayerEntity) player, new QualityGrinderPayloadS2C(!this.canQuality));
                return;
            }
            // Check custom data and tiers
            String tier = CritalDataUtil.getNbtCompound(grindItem).getString(CritData.TIER_FLAG);
            if (this.canGrind && this.canQuality && CritTier.CELESTIAL == CritTier.fromName(tier) ) {
                this.canGrind = false;
                this.canQuality = true;
            }

        } else {
            this.canGrind = false;
            this.canQuality = false;
        }
        // Send the payload based on the canGrind flag
        System.out.println(!this.canQuality);
        GrinderPayloadS2C.send((ServerPlayerEntity) player, new GrinderPayloadS2C(!this.canGrind));
        QualityGrinderPayloadS2C.send((ServerPlayerEntity) player, new QualityGrinderPayloadS2C(!this.canQuality));
    }


    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inventory));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> player.squaredDistanceTo((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5) <= 64.0, true);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot == 0) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slot == 1) {
                if (!this.insertItem(itemStack2, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (isGrinderMaterial(itemStack2)) {
                if (!this.insertItem(itemStack2, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.slots.get(1).hasStack() && this.slots.get(1).canInsert(itemStack2)) {
                ItemStack itemStack3 = itemStack2.copyWithCount(1);
                itemStack2.decrement(1);
                this.slots.get(1).setStack(itemStack3);
            } else {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot2.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    private boolean isGrinderMaterial(ItemStack stack) {
        return stack.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL) || stack.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL);
    }


    public void grinder() {
        ItemStack grindItem = this.getSlot(1).getStack();

        if (grindItem.getItem() instanceof ToolItem toolItem) {
            ToolMaterial material = toolItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getToolCritChance);
        } else if (grindItem.getItem() instanceof RangedWeaponItem || grindItem.getItem() instanceof TridentItem || grindItem.getItem() instanceof ShieldItem) {
            CritalDataUtil.applyCritData(grindItem, ToolMaterials.GOLD, CritData::getToolCritChance);
        } else if (grindItem.getItem() instanceof ArmorItem armorItem) {
            RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getArmorCritChance);
        }
        this.decrementStack(0);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    public void quality_grinder() {
        ItemStack grindItem = this.getSlot(1).getStack();
        CritTier tier = CritalDataUtil.getCritTierFromStack(grindItem);
        if (grindItem.getItem() instanceof ToolItem toolItem) {
            ToolMaterial material = toolItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getToolCritChance, tier);
        } else if (grindItem.getItem() instanceof RangedWeaponItem || grindItem.getItem() instanceof TridentItem) {
            CritalDataUtil.applyCritData(grindItem, ToolMaterials.GOLD, CritData::getToolCritChance, tier);
        } else if (grindItem.getItem() instanceof ArmorItem armorItem) {
            RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getArmorCritChance, tier);
        }
        this.decrementStack(0);
        this.decrementStack(0);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        itemStack.decrement(1);
        this.inventory.setStack(slot, itemStack);
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.inventory && super.canInsertIntoSlot(stack, slot);
    }
}
