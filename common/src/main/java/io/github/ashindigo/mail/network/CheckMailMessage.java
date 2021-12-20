package io.github.ashindigo.mail.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import io.github.ashindigo.mail.MailMod;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

@Deprecated
public class CheckMailMessage extends BaseS2CMessage {

    final UUID playerUUID;

    public CheckMailMessage(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public CheckMailMessage(FriendlyByteBuf friendlyByteBuf) {
        this.playerUUID = friendlyByteBuf.readUUID();
    }

    @Override
    public MessageType getType() {
        return null; //MailMod.CHECK_MAIL;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        //Minecraft.getInstance().setScreen(new CheckMailScreen());
    }
}