package io.github.ashindigo.mail;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class Helpers {

    @Deprecated
    public static Inventory getInvForOfflinePlayer(MinecraftServer server, String user) {
        GameProfile profile = server.getProfileCache().get(user).get();
        ServerPlayer entity = new ServerPlayer(server, server.getLevel(Level.OVERWORLD), profile);
        System.out.println(entity);
        return entity.getInventory();
    }

    public static void getMailInfoForPlayer(MinecraftServer server, ServerPlayer targetPlayer) {
        if (MailDataStorage.getInstance().hasMailbox(targetPlayer.getUUID())) {
            System.out.println(MailDataStorage.getInstance().getMailBox(targetPlayer.getUUID()));
        }
    }
}
