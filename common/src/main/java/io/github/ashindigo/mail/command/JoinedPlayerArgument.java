package io.github.ashindigo.mail.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.utils.GameInstance;
import io.github.ashindigo.mail.mixin.MinecraftServerAccessor;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

@Deprecated
public class JoinedPlayerArgument implements ArgumentType<JoinedPlayerArgument.Result> {

// Need to get the player name/uuid so i can modify player data

    @Override // PlayerDataStorage
    public Result parse(StringReader reader) throws CommandSyntaxException {

        //new File(Arrays.stream(((MinecraftServerAccessor) GameInstance.getServer()).getPlayerDataStorage().getSeenPlayers()).findFirst().get());
        return null;
    }

    public static JoinedPlayerArgument joinedPlayerArgument() {
        return  new JoinedPlayerArgument();
    }

    public static ServerPlayer getPlayer(CommandContext<CommandSourceStack> commandContext, String target) {
        return commandContext.getArgument(target, PlayerDataSelector.class).findPlayer(commandContext.getSource());
    }

    @FunctionalInterface
    public interface Result {
        Collection<GameProfile> findPlayer(CommandSourceStack commandSourceStack) throws CommandSyntaxException;
    }

//
//    public static class SelectorResult implements Result {
//        private final EntitySelector selector;
//
//        public SelectorResult(EntitySelector entitySelector) {
//            this.selector = entitySelector;
//        }
//
//        public Collection<GameProfile> getNames(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
//            List<ServerPlayer> list = this.selector.findPlayers(commandSourceStack);
//            if (list.isEmpty()) {
//                throw EntityArgument.NO_PLAYERS_FOUND.create();
//            } else {
//                List<GameProfile> list2 = Lists.newArrayList();
//                Iterator var4 = list.iterator();
//
//                while(var4.hasNext()) {
//                    ServerPlayer serverPlayer = (ServerPlayer)var4.next();
//                    list2.add(serverPlayer.getGameProfile());
//                }
//
//                return list2;
//            }
//        }
//    }
}
