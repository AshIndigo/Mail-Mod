package io.github.ashindigo.mail.container;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

@Deprecated
public class MailBoxMenu extends AbstractContainerMenu {

    protected MailBoxMenu(int windowId) {
        //super(MailMod.MAILBOX_CONTAINER.get(), windowId);
        super(null, windowId);
    }

    public MailBoxMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(windowId, playerInv, buf.readBlockPos());
    }

    public MailBoxMenu(int windowId, Inventory playerInv, BlockPos blockPos) {
        this(windowId);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
