package io.github.ashindigo.mail.block;

import dev.architectury.registry.menu.MenuRegistry;
import io.github.ashindigo.mail.entity.MailBoxEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@Deprecated
public class MailBoxBlock extends BaseEntityBlock {

    public MailBoxBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide) {
//            MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) world.getBlockEntity(pos), packetBuffer -> {
//
//            });
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof MailBoxEntity mailBoxEntity && placer instanceof Player player) {
                mailBoxEntity.setPlayer(player);
            }
        }
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MailBoxEntity(blockPos, blockState);
    }
}
