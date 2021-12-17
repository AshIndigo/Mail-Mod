package io.github.ashindigo.mail.container;

import io.github.ashindigo.mail.MailMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MailBoxMenu extends AbstractContainerMenu {

    protected MailBoxMenu(MenuType<?> menuType, int i) {
        super(menuType, i);
    }

    public MailBoxMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(windowId, playerInv, buf.readBlockPos());
    }

    public MailBoxMenu(int windowId, Inventory playerInv, BlockPos blockPos) {
        super(MailMod.MAILBOX_CONTAINER.get(), windowId);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
