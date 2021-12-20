package io.github.ashindigo.mail.entity;

import io.github.ashindigo.mail.MailMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

@Deprecated
public class MailBoxEntity extends BlockEntity {

    public MailBoxEntity(BlockPos blockPos, BlockState blockState) {
        super(null, blockPos, blockState); //super(MailMod.MAILBOX_ENTITY.get(), blockPos, blockState);
    }

    public void setPlayer(Player placer) {

    }
}
