package io.github.ashindigo.mail;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;

public class MailCommand extends Command {

    protected MailCommand() {
        super("mail", "Check or send mail", "/mail [check|send]", Lists.newArrayList());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "check":
                    if (args.length > 1) {
                        if(sender.hasPermission(PermissionDefault.OP.name())) {

                        }
                    }
                    break;
                case "send":
                    break;
            }
        }
        return false;
    }

    public static int checkMail(Player targetPlayer) {
        MailBoxContainer mailInfoForPlayer = Mail.getMailInfoForPlayer(targetPlayer);
        Bukkit.createInventory();
        if (mailInfoForPlayer == null) {
            MailDataStorage.getInstance().addItemToMailBox(targetPlayer.getUUID(), ItemStack.EMPTY);
        }
        targetPlayer.openInventory(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return targetPlayer.getDisplayName().copy().append(new TextComponent("'s Mailbox"));
            }
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return ChestMenu.threeRows(i, inventory, mailInfoForPlayer);
            }
        });
        return 0;
    }
}
