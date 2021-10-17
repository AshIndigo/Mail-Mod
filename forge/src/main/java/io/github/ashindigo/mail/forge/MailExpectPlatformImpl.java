package io.github.ashindigo.mail.forge;

import io.github.ashindigo.mail.MailExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class MailExpectPlatformImpl {
    /**
     * This is our actual method to {@link MailExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
