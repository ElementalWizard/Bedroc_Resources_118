package com.alexvr.bedres.blocks;

import com.alexvr.bedres.utils.BedrockReferences;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ScraperMotor extends Block {

    public ScraperMotor(BlockBehaviour.Properties props) {
        super(props);

    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }


}
