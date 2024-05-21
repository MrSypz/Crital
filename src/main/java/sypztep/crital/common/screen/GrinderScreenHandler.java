package sypztep.crital.common.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import sypztep.crital.common.CritalMod;

public class GrinderScreenHandler extends ScreenHandler {
    private final Inventory inventory = new SimpleInventory(2) {
        @Override
        public void markDirty() {
            super.markDirty();
            GrinderScreenHandler.this.onContentChanged(this);
        }
    };
    private final PlayerEntity player;
    private boolean grindAble;
    private BlockPos pos;
    public GrinderScreenHandler(int syncId, PlayerInventory playerinv) {
        super(CritalMod.GRINDER_SCREEN_HANDLER_TYPE,syncId);
        this.player = playerinv.player;
        this.addSlot(new Slot(this.inventory, 0,45,47));
    }
    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
    }
}
