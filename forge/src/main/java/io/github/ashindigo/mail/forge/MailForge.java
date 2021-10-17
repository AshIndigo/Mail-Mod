package io.github.ashindigo.mail.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.ashindigo.mail.Constants;
import io.github.ashindigo.mail.MailMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class MailForge {
    public MailForge() {
        EventBuses.registerModEventBus(Constants.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        MailMod.init();
    }
}
