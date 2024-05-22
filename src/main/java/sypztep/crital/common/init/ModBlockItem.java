package sypztep.crital.common.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.crital.common.CritalMod;
import sypztep.crital.common.block.GrinderBlock;

public class ModBlockItem {
    public static final Block GRINDER = new GrinderBlock(AbstractBlock.Settings.copy(Blocks.SMITHING_TABLE));
    public static void init() {
        Registry.register(Registries.BLOCK, CritalMod.id("grinder_table"), GRINDER);
    }
}
