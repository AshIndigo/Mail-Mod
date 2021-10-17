package io.github.ashindigo.mail.fabric;

import io.github.ashindigo.mail.MailExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class MailExpectPlatformImpl {
    /**
     * This is our actual method to {@link MailExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
