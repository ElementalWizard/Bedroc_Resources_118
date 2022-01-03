package com.alexvr.bedres.blocks.gravityBubble;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class FluxedGravityBubble extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Block.box(4.5D, 4.0D, 4.5D, 11.5D, 11.5D, 11.5D);

    public FluxedGravityBubble(BlockBehaviour.Properties props) {
        super(props);

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new FluxedGravityBubbleTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide()) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FluxedGravityBubbleTile generator) {
                ItemStack itemInHand = player.getItemInHand(hand);
                level.getBlockEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                    if(player.isShiftKeyDown() && itemInHand.isEmpty()){
                        generator.setAreaVisible(!generator.isAreaVisible());
                    }else if(itemInHand.isEmpty()){
                        if (h.getStackInSlot(0).isEmpty()){
                            generator.setAreaVisible(false);
                        }else{
                            boolean extracted = player.addItem(h.getStackInSlot(0));
                            if (extracted) {
                                h.insertItem(0,ItemStack.EMPTY,false);
                            }
                        }


                    }else{
                        ItemStack remainder = ItemHandlerHelper.insertItem(h, itemInHand, false);
                        if (remainder.isEmpty()) {
                            player.setItemInHand(hand,ItemStack.EMPTY);
                        } else {
                            player.setItemInHand(hand,remainder);
                        }


                    }
                    generator.setChanged();
                    generator.sendUpdates();
                    level.sendBlockUpdated(pos, generator.getBlockState(), generator.getBlockState(), Block.UPDATE_ALL);
                });
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return (lvl, pos, stt, te) -> {
                if (te instanceof FluxedGravityBubbleTile generator) generator.tickClient();
            };
        }else {
            return (lvl, pos, stt, te) -> {
                if (te instanceof FluxedGravityBubbleTile generator) generator.tickServer();
            };
        }
    }
}