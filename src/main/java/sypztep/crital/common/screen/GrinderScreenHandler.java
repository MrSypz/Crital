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
import sypztep.crital.client.payload.GrinderPayloadS2C;
import sypztep.crital.client.payload.QualityGrinderPayloadS2C;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.init.ModTag;
import sypztep.crital.common.util.CritalDataUtil;

public class GrinderScreenHandler extends ScreenHandler {
    private final Inventory inventory = new SimpleInventory(3) {
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
        addSlot(new Slot(this.inventory, 0, 9, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isGrinderMaterial(stack);
            }
        });
        addSlot(new Slot(this.inventory, 1, 151, 34) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return isGrindableItem(stack);
            }
        });
        addSlot(new Slot(this.inventory, 2, 29, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.COPPER_INGOT);
            }
        });
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
        ItemStack slotOutput = this.getSlot(1).getStack();
        if (this.getSlot(0).hasStack() && this.getSlot(1).hasStack() && this.getSlot(2).hasStack()) {
            Item GrindItem = slotOutput.getItem();

            ItemStack material = this.getSlot(0).getStack();
            ItemStack additionmaterial = this.getSlot(2).getStack();
            if ((GrindItem instanceof ToolItem || GrindItem instanceof RangedWeaponItem || GrindItem instanceof TridentItem || GrindItem instanceof ShieldItem) && slotOutput.get(DataComponentTypes.CUSTOM_DATA) == null) {
                this.canGrind = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
                this.canQuality = false;
            } else if (GrindItem instanceof ArmorItem && slotOutput.get(DataComponentTypes.CUSTOM_DATA) == null) {
                this.canGrind = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
                this.canQuality = false;
            } else if (GrindItem instanceof ToolItem || GrindItem instanceof RangedWeaponItem || GrindItem instanceof TridentItem || GrindItem instanceof ShieldItem) {
                this.canGrind = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
                this.canQuality = material.isIn(ModTag.Items.WEAPON_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
            } else if (GrindItem instanceof ArmorItem) {
                this.canGrind = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
                this.canQuality = material.isIn(ModTag.Items.ARMOR_GRINDER_MATERIAL) && additionmaterial.isOf(Items.COPPER_INGOT);
            }
            if (slotOutput.get(DataComponentTypes.CUSTOM_DATA) == null) {
                GrinderPayloadS2C.send((ServerPlayerEntity) player, !this.canGrind);
                QualityGrinderPayloadS2C.send((ServerPlayerEntity) player, !this.canQuality);
                return;
            }
            String tier = CritalDataUtil.getNbtCompound(slotOutput).getString(CritData.TIER_FLAG);
            if (this.canGrind && this.canQuality && CritTier.CELESTIAL == CritTier.fromName(tier)) {
                this.canGrind = false;
                this.canQuality = true;
            }

        } else {
            this.canGrind = false;
            this.canQuality = false;
        }
        GrinderPayloadS2C.send((ServerPlayerEntity) player, !this.canGrind);
        QualityGrinderPayloadS2C.send((ServerPlayerEntity) player, !this.canQuality);
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
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

            // If the slot clicked is one of the container's slots
            if (index < 3) { // 0, 1, 2 are container slots
                if (!insertItem(slotStack, 3, 39, true)) { // Player inventory slots: 3 to 38 (hotbar included)
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(slotStack, stack);
            } else {
                // If the slot clicked is one of the player inventory slots
                if (isGrinderMaterial(slotStack)) {
                    if (!insertItem(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isGrindableItem(slotStack)) {
                    if (!insertItem(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.isOf(Items.COPPER_INGOT)) {
                    if (!insertItem(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (index >= 3 && index < 30) { // Player main inventory (excluding hotbar)
                        if (!insertItem(slotStack, 30, 39, false)) { // Try hotbar
                            return ItemStack.EMPTY;
                        }
                    } else if (index >= 30 && index < 39) { // Hotbar
                        if (!insertItem(slotStack, 3, 30, false)) { // Try main inventory
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (slotStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (slotStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, slotStack);
        }
        return stack;
    }


    private boolean isGrindableItem(ItemStack stack) {
        return stack.isIn(ModTag.Items.GRINDABLE_ITEM);
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
        this.decrementStack(2);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    public void quality_grinder() {
        ItemStack grindItem = this.getSlot(1).getStack();

        CritTier tier = CritalDataUtil.getCritTierFromStack(grindItem);

        if (grindItem.getItem() instanceof ToolItem toolItem) {
            ToolMaterial material = toolItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getToolCritChance, tier);
        } else if (grindItem.getItem() instanceof RangedWeaponItem || grindItem.getItem() instanceof TridentItem || grindItem.getItem() instanceof ShieldItem) {
            CritalDataUtil.applyCritData(grindItem, ToolMaterials.GOLD, CritData::getToolCritChance, tier);
        } else if (grindItem.getItem() instanceof ArmorItem armorItem) {
            RegistryEntry<ArmorMaterial> material = armorItem.getMaterial();
            CritalDataUtil.applyCritData(grindItem, material, CritData::getArmorCritChance, tier);
        }
        this.decrementStack(0);
        this.decrementStack(2);
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
