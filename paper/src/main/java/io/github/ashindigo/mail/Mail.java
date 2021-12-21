package io.github.ashindigo.mail;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mail extends JavaPlugin {

    public static MailBoxContainer getMailInfoForPlayer(Player targetPlayer) {
        if (!MailDataStorage.getInstance().hasMailbox(targetPlayer.getUniqueId())) {
           MailDataStorage.getInstance().createMailBox(targetPlayer.getUniqueId());
        }
        return MailDataStorage.getInstance().getMailBox(targetPlayer.getUniqueId());
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
