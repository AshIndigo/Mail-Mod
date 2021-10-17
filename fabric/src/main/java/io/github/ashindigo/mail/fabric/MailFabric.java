package io.github.ashindigo.mail.fabric;

import io.github.ashindigo.mail.MailMod;
import net.fabricmc.api.ModInitializer;

public class MailFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MailMod.init();
    }
}
