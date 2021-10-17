package io.github.ashindigo.mail;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.hooks.block.BlockEntityHooks;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.networking.simple.SimpleNetworkManager;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import io.github.ashindigo.mail.block.MailBoxBlock;
import io.github.ashindigo.mail.container.MailBoxContainer;
import io.github.ashindigo.mail.entity.MailBoxEntity;
import io.github.ashindigo.mail.network.CheckMailMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class MailMod {

    public static final SimpleNetworkManager NETWORK_MANAGER = SimpleNetworkManager.create(Constants.MODID);
    public static final CreativeModeTab CREATIVE_TAB = CreativeTabRegistry.create(new ResourceLocation(Constants.MODID, "mail"), () -> new ItemStack(MailMod.MAILBOX_ITEM.get()));

    // Blocks
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MODID, Registry.BLOCK_REGISTRY);
    public static final RegistrySupplier<Block> MAILBOX = BLOCKS.register("mailbox", () -> new MailBoxBlock());

    // Block Entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Constants.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static RegistrySupplier<BlockEntityType<? extends MailBoxEntity>> MAILBOX_ENTITY = BLOCK_ENTITIES.register("mailbox", () -> BlockEntityHooks.builder(MailBoxEntity::new, MAILBOX.get()).build(null));

    // Containers
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Constants.MODID, Registry.MENU_REGISTRY);
    public static final RegistrySupplier<MenuType<MailBoxContainer>> MAILBOX_CONTAINER = CONTAINERS.register("mailbox", () -> MenuRegistry.ofExtended(MailBoxContainer::new));

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

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandSelection) -> {
            LiteralArgumentBuilder<CommandSourceStack> mailCommand = LiteralArgumentBuilder.literal("mail");
            mailCommand.then((Commands.literal("check").executes((commandContext) -> checkMail(commandContext.getSource().getPlayerOrException()))).then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> checkMail(EntityArgument.getPlayer(commandContext, "target")))));
            mailCommand.then((Commands.literal("send")).then(Commands.argument("target", EntityArgument.player()).executes((commandContext) -> sendMail(EntityArgument.getPlayer(commandContext, "target")))));
            commandDispatcher.register(mailCommand);
        });
        System.out.println(MailExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    private static int sendMail(ServerPlayer targetPlayer) {

        return 0;
    }

    private static int checkMail(ServerPlayer targetPlayer) {
        return 0;
    }
}
