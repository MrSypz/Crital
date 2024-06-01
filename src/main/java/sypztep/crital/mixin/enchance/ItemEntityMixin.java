package sypztep.crital.mixin.enchance;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Unique
    private static final TrackedData<Integer> TRANSFORM_PROGRESS = DataTracker.registerData(ItemEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final int CONCRETE_TRANSFORM_TIME = 2; // 2 seconds for transformation
//    @Unique
//    private static final int COPPER_TRANSFORM_TIME = 20; // 20 seconds for transformation
    @Unique
    private static final Map<Block, Block> CONCRETE_MAP = new HashMap<>();
    @Unique
    private static final Map<Block, Block> COPPER_MAP = new HashMap<>();

    static {
        CONCRETE_MAP.put(Blocks.WHITE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE);
        CONCRETE_MAP.put(Blocks.ORANGE_CONCRETE_POWDER, Blocks.ORANGE_CONCRETE);
        CONCRETE_MAP.put(Blocks.MAGENTA_CONCRETE_POWDER, Blocks.MAGENTA_CONCRETE);
        CONCRETE_MAP.put(Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.LIGHT_BLUE_CONCRETE);
        CONCRETE_MAP.put(Blocks.YELLOW_CONCRETE_POWDER, Blocks.YELLOW_CONCRETE);
        CONCRETE_MAP.put(Blocks.LIME_CONCRETE_POWDER, Blocks.LIME_CONCRETE);
        CONCRETE_MAP.put(Blocks.PINK_CONCRETE_POWDER, Blocks.PINK_CONCRETE);
        CONCRETE_MAP.put(Blocks.GRAY_CONCRETE_POWDER, Blocks.GRAY_CONCRETE);
        CONCRETE_MAP.put(Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.LIGHT_GRAY_CONCRETE);
        CONCRETE_MAP.put(Blocks.CYAN_CONCRETE_POWDER, Blocks.CYAN_CONCRETE);
        CONCRETE_MAP.put(Blocks.PURPLE_CONCRETE_POWDER, Blocks.PURPLE_CONCRETE);
        CONCRETE_MAP.put(Blocks.BLUE_CONCRETE_POWDER, Blocks.BLUE_CONCRETE);
        CONCRETE_MAP.put(Blocks.BROWN_CONCRETE_POWDER, Blocks.BROWN_CONCRETE);
        CONCRETE_MAP.put(Blocks.GREEN_CONCRETE_POWDER, Blocks.GREEN_CONCRETE);
        CONCRETE_MAP.put(Blocks.RED_CONCRETE_POWDER, Blocks.RED_CONCRETE);
        CONCRETE_MAP.put(Blocks.BLACK_CONCRETE_POWDER, Blocks.BLACK_CONCRETE);

        COPPER_MAP.put(Blocks.COPPER_BLOCK, Blocks.EXPOSED_COPPER);
        COPPER_MAP.put(Blocks.EXPOSED_COPPER, Blocks.WEATHERED_COPPER);
        COPPER_MAP.put(Blocks.WEATHERED_COPPER, Blocks.OXIDIZED_COPPER);
    }

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void onTick(CallbackInfo ci) {
        ItemEntity itemEntity = (ItemEntity) (Object) this;
        ItemStack stack = itemEntity.getStack();
        if (isInCauldronWithWaterAndCampfire(itemEntity)) {
            if (isConcretePowder(stack))
                handleTransformation(itemEntity, CONCRETE_TRANSFORM_TIME, this::ConcreteTransfrom);
//             else if (isCopperBlock(stack))
//                handleTransformation(itemEntity, COPPER_TRANSFORM_TIME, this::CopperTransform);
        } else {
            this.dataTracker.set(TRANSFORM_PROGRESS, 0); // Reset if conditions are not met
        }
    }

    @Unique
    private void handleTransformation(ItemEntity itemEntity, int transformTime, Consumer<ItemEntity> transformHandler) {
        if (itemEntity.age % 20 == 0) { // Check every second (20 ticks)
            int itemCount = itemEntity.getStack().getCount();
            int progress = this.dataTracker.get(TRANSFORM_PROGRESS);

            if (progress >= transformTime * itemCount)
                transformHandler.accept(itemEntity);
            else
                addParticle(itemEntity, itemCount);
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(TRANSFORM_PROGRESS, 0);
    }

    //todo add to packet to send only player can see
    @Unique
    private void addParticle(ItemEntity itemEntity, int count) {
        World world = itemEntity.getWorld();
        BlockPos pos = itemEntity.getBlockPos();
        double x = pos.getX();
        double y = pos.getY() + 0.5;
        double z = pos.getZ();
        world.playSound(null, pos, SoundEvents.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 2.5F, 1.0F);
        for (int i = 0; i < Math.min(16, count); i++)
            world.addParticle(ParticleTypes.POOF, x + random.nextFloat(), y + random.nextFloat(), z + random.nextFloat(), 0.0, 0.05, 0.0);
        this.dataTracker.set(TRANSFORM_PROGRESS, this.dataTracker.get(TRANSFORM_PROGRESS) + 1);
    }

    @Unique
    private boolean isConcretePowder(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof ConcretePowderBlock;
    }

    @Unique
    private boolean isCopperBlock(ItemStack stack) {
        return stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock() instanceof OxidizableBlock;
    }

    @Unique
    private boolean isInCauldronWithWaterAndCampfire(ItemEntity itemEntity) {
        BlockPos itemPos = itemEntity.getBlockPos();
        BlockState cauldronState = itemEntity.getWorld().getBlockState(itemPos);

        if (cauldronState.getBlock() != Blocks.WATER_CAULDRON || cauldronState.get(Properties.LEVEL_3) < 1) {
            return false;
        }

        BlockPos belowCauldronPos = itemPos.down();
        BlockState belowCauldronState = itemEntity.getWorld().getBlockState(belowCauldronPos);

        if (belowCauldronState.getBlock() instanceof CampfireBlock) {
            return belowCauldronState.get(CampfireBlock.LIT);
        }
        return false;
    }

    @Unique
    private void ConcreteTransfrom(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getStack();
        Block block = ((BlockItem) stack.getItem()).getBlock();
        Block concreteBlock = CONCRETE_MAP.get(block);

        if (concreteBlock != null) {
            ItemStack concreteStack = new ItemStack(concreteBlock, stack.getCount());
            ItemEntity concreteEntity = new ItemEntity(itemEntity.getWorld(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), concreteStack);

            itemEntity.discard();
            itemEntity.getWorld().spawnEntity(concreteEntity);
            World world = itemEntity.getWorld();
            BlockPos pos = itemEntity.getBlockPos();

            playersound(world, pos);
        }
    }

    @Unique
    private void CopperTransform(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getStack();
        Block block = ((BlockItem) stack.getItem()).getBlock();
        Block copper_block = COPPER_MAP.get(block);

        if (copper_block != null) {
            ItemStack copperStack = new ItemStack(copper_block, stack.getCount());
            ItemEntity copperEntity = new ItemEntity(itemEntity.getWorld(), itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), copperStack);

            itemEntity.discard();
            itemEntity.getWorld().spawnEntity(copperEntity);
            World world = itemEntity.getWorld();
            BlockPos pos = itemEntity.getBlockPos();

            playersound(world, pos);
        }
    }

    @Unique
    private void playersound(World world, BlockPos pos) {
        world.addParticle(ParticleTypes.EXPLOSION,true,pos.getX(),pos.getY(),pos.getZ(),0,0,0);
        world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.5F, 1.0F);
    }
}




