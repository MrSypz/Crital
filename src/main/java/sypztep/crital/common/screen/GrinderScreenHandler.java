package sypztep.crital.common.screen;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.WorldEvents;
import sypztep.crital.client.payload.GrinderPayloadS2C;
import sypztep.crital.client.payload.QualityGrinderPayloadS2C;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.data.CritalData;
import sypztep.crital.common.data.CritTier;
import sypztep.crital.common.init.ModItem;
import sypztep.crital.common.util.CritalDataUtil;
import sypztep.tyrannus.common.util.ItemStackHelper;

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
                return CritalDataUtil.matchesItemData(stack);
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
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (!player.getWorld().isClient() && inventory == this.inventory) {
            this.doGrindTask();
        }
    }

    private void doGrindTask() {
        ItemStack slotOutput = this.getSlot(1).getStack();

        boolean allSlotInsert = this.getSlot(0).hasStack() && this.getSlot(1).hasStack();

        boolean canGrind = false;
        boolean canQuality = false;
        if (allSlotInsert && CritalDataUtil.matchesItemData(slotOutput)) {
            ItemStack material = this.getSlot(0).getStack();

            boolean additionmaterial = this.getSlot(2).getStack().isOf(Items.COPPER_INGOT);
            boolean isArmor = slotOutput.getItem() instanceof ArmorItem;
            boolean isGrindable = slotOutput.get(DataComponentTypes.CUSTOM_DATA) != null;

            if (!isGrindable && !isArmor) { // handle null and not armor
                canGrind = material.isOf(ModItem.COPPERAL_WEAPON) && additionmaterial;
            } else if (!isGrindable) { // handle null and armor item
                canGrind = material.isOf(ModItem.COPPERAL_ARMOR) && additionmaterial;
            } else if (!isArmor) { // not armor
                canGrind = material.isOf(ModItem.COPPERAL_WEAPON) && additionmaterial;
                canQuality = material.isOf(ModItem.COPPERAL_WEAPON) && additionmaterial;
            } else { //armor
                canGrind = material.isOf(ModItem.COPPERAL_ARMOR) && additionmaterial;
                canQuality = material.isOf(ModItem.COPPERAL_ARMOR) && additionmaterial;
            }
            String tier = ItemStackHelper.getNbtCompound(slotOutput).getString(CritalData.TIER_FLAG);
            if (canGrind && canQuality && CritTier.CELESTIAL == CritTier.fromName(tier)) {
                canGrind = false;
            }
        }
        GrinderPayloadS2C.send((ServerPlayerEntity) player, !canGrind);
        QualityGrinderPayloadS2C.send((ServerPlayerEntity) player, !canQuality);
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

    private boolean isGrinderMaterial(ItemStack stack) {
        return stack.isOf(ModItem.COPPERAL_ARMOR) || stack.isOf(ModItem.COPPERAL_WEAPON);
    }

    public void grinder() {
        ItemStack grindItem = this.getSlot(1).getStack();
        CritalDataUtil.applyCritData(grindItem);
        this.decrementStack(0);
        this.decrementStack(2);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    public void quality_grinder() {
        ItemStack grindItem = this.getSlot(1).getStack();
        CritTier tier = CritalDataUtil.getCritTierFromStack(grindItem);
        CritalDataUtil.applyCritData(grindItem,tier);
        this.decrementStack(0);
        this.decrementStack(2);
        this.context.run((world, pos) -> world.syncWorldEvent(WorldEvents.SMITHING_TABLE_USED, pos, 0));
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.inventory.getStack(slot);
        itemStack.decrement(1);
        this.inventory.setStack(slot, itemStack);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasStack()) {
            ItemStack slotStack = slot.getStack();
            stack = slotStack.copy();

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
                } else if (CritalDataUtil.matchesItemData(slotStack)) {
                    if (!insertItem(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotStack.isOf(Items.COPPER_INGOT)) {
                    if (!insertItem(slotStack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (index < 30) { // Player main inventory (excluding hotbar)
                        if (!insertItem(slotStack, 30, 39, false)) { // Try hotbar
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 39) { // Hotbar
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

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.inventory && super.canInsertIntoSlot(stack, slot);
    }
}
