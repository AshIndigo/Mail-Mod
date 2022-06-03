package io.github.ashindigo.mail.forge;

import dev.architectury.platform.forge.EventBuses;
import io.github.ashindigo.mail.Constants;
import io.github.ashindigo.mail.MailMod;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;

@Mod(Constants.MODID)
public class MailForge {

    public MailForge() {
        EventBuses.registerModEventBus(Constants.MODID, FMLJavaModLoadingContext.get().getModEventBus());
        MailMod.init();
        // So mod isn't needed on client when connecting to server
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }
}
