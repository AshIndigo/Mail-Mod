package io.github.ashindigo.mail;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.ashindigo.mail.block.MailBoxBlock;
import io.github.ashindigo.mail.container.MailBoxContainer;
import io.github.ashindigo.mail.container.MailBoxMenu;
import io.github.ashindigo.mail.entity.MailBoxEntity;
import io.github.ashindigo.mail.network.CheckMailMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MailMod {

    public static final SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(Constants.MODID);
    public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(Constants.MODID, "mail"), () -> new ItemStack(MailMod.MAILBOX_ITEM.get()));

    // Blocks
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MODID, Registry.BLOCK_REGISTRY);
    public static final RegistrySupplier<Block> MAILBOX = BLOCKS.register("mailbox", MailBoxBlock::new);

    // Block Entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final RegistrySupplier<BlockEntityType<? extends MailBoxEntity>> MAILBOX_ENTITY = BLOCK_ENTITIES.register("mailbox", () -> BlockEntityHooks.builder(MailBoxEntity::new, MAILBOX.get()).build(null));

    // Containers
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Constants.MODID, Registry.MENU_REGISTRY);
    public static final RegistrySupplier<MenuType<MailBoxMenu>> MAILBOX_CONTAINER = CONTAINERS.register("mailbox", () -> MenuRegistry.ofExtended(MailBoxMenu::new));

    // Items
    private static final Item.Properties DEF_PROPS = new Item.Properties().tab(CREATIVE_TAB);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MODID, Registry.ITEM_REGISTRY);
    public static final RegistrySupplier<Item> MAILBOX_ITEM = ITEMS.register("mailbox", () -> new BlockItem(MAILBOX.get(), DEF_PROPS));

    // Packets
    public static final MessageType CHECK_MAIL = NETWORK_MANAGER.registerS2C("check_mail", CheckMailMessage::new);


    public static void init() {
        BLOCKS.register();
        ITEMS.register();
        BLOCK_ENTITIES.register();
        CONTAINERS.register();
        LifecycleEvent.SERVER_LEVEL_SAVE.register(serverLevel -> MailDataStorage.getInstance().save());
        LifecycleEvent.SERVER_LEVEL_LOAD.register(serverLevel -> MailDataStorage.getInstance().load());

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandSelection) -> {
            LiteralArgumentBuilder<CommandSourceStack> mailCommand = LiteralArgumentBuilder.literal("mail");
            mailCommand.then((Commands.literal("check").executes((commandContext) -> checkMail(commandContext.getSource().getServer(), commandContext.getSource().getPlayerOrException()))).then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> checkMail(commandContext.getSource().getServer(), EntityArgument.getPlayer(commandContext, "target")))));
            mailCommand.then((Commands.literal("send")).then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> sendMail(EntityArgument.getPlayer(commandContext, "target")))));
            commandDispatcher.register(mailCommand);
        });
    }

    private static int sendMail(ServerPlayer targetPlayer) { // TODO Add amount arg
        ItemStack toSend = targetPlayer.getItemInHand(InteractionHand.MAIN_HAND).copy();
        toSend.setCount(1);
        if (!targetPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && MailDataStorage.getInstance().addItemToMailBox(targetPlayer.getUUID(), toSend)) {
            targetPlayer.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
        }
        return 0;
    }

    private static int checkMail(MinecraftServer server, ServerPlayer targetPlayer) {
        MailBoxContainer mailInfoForPlayer = Helpers.getMailInfoForPlayer(server, targetPlayer);
        if (mailInfoForPlayer != null) {
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
        }
        return 0;
    }
}
