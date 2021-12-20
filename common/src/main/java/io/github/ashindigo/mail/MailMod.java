package io.github.ashindigo.mail;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import io.github.ashindigo.mail.container.MailBoxContainer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;

public class MailMod {

    public static void init() {
        LifecycleEvent.SERVER_LEVEL_SAVE.register(serverLevel -> MailDataStorage.getInstance().save());
        LifecycleEvent.SERVER_LEVEL_LOAD.register(serverLevel -> MailDataStorage.getInstance().load());

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandSelection) -> {
            LiteralArgumentBuilder<CommandSourceStack> mailCommand = LiteralArgumentBuilder.literal("mail");
            // Check
            mailCommand.then((Commands.literal("check").executes((commandContext) -> checkMail(commandContext.getSource().getPlayerOrException())))
                    .then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> checkMail(EntityArgument.getPlayer(commandContext, "target")))));
            // Send
            mailCommand.then((Commands.literal("send"))
                    .then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> sendMail(EntityArgument.getPlayer(commandContext, "target"), 1))
                    .then(Commands.argument("amount", IntegerArgumentType.integer(1, 64)).executes((commandContext) -> sendMail(EntityArgument.getPlayer(commandContext, "target"), IntegerArgumentType.getInteger(commandContext, "amount"))))));
            commandDispatcher.register(mailCommand);
        });
    }

    private static int sendMail(ServerPlayer targetPlayer, int amount) { // TODO Add amount arg
        ItemStack toSend = targetPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (toSend.getCount() >= amount) {
            toSend = toSend.copy();
            toSend.setCount(amount);
            if (!targetPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && MailDataStorage.getInstance().addItemToMailBox(targetPlayer.getUUID(), toSend)) {
                targetPlayer.getItemInHand(InteractionHand.MAIN_HAND).shrink(amount);
            }
        }
        return 0;
    }

    public static int checkMail(ServerPlayer targetPlayer) {
        MailBoxContainer mailInfoForPlayer = Helpers.getMailInfoForPlayer(targetPlayer);
        if (mailInfoForPlayer == null) {
            MailDataStorage.getInstance().addItemToMailBox(targetPlayer.getUUID(), ItemStack.EMPTY);
        }
        MenuRegistry.openMenu(targetPlayer, new MenuProvider() {
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
