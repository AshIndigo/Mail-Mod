package io.github.ashindigo.mail;

import dev.architectury.hooks.LevelResourceHooks;
import dev.architectury.utils.GameInstance;
import io.github.ashindigo.mail.container.MailBoxContainer;
import io.github.ashindigo.mail.mixin.MinecraftServerAccessor;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.UUID;

/**
 * Class for managing player mailbox's
 */
public class MailDataStorage {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final MailDataStorage INSTANCE = new MailDataStorage();
    private final File mailDir;
    private final HashMap<UUID, MailBoxContainer> mailBoxes = new HashMap<>();

    private MailDataStorage() {
        LevelStorageSource.LevelStorageAccess levelStorageAccess = ((MinecraftServerAccessor) GameInstance.getServer()).getStorageSource();
        this.mailDir = levelStorageAccess.getLevelPath(LevelResourceHooks.create(Constants.MAILBOX)).toFile();
        this.mailDir.mkdirs();
    }

    public static MailDataStorage getInstance() {
        return INSTANCE;
    }

    /**
     * Returns false if item failed to be added (typically because of full mailbox)
     */
    public boolean addItemToMailBox(UUID uuid, ItemStack stack) {
        MailBoxContainer container = createMailBox(uuid);
        if (container.canAddItem(stack)) {
            container.addItem(stack);
            return true;
        }
        return false;
    }

    public MailBoxContainer createMailBox(UUID uuid) {
        if (!mailBoxes.containsKey(uuid)) {
            mailBoxes.put(uuid, new MailBoxContainer());
        }
        return getMailBox(uuid);
    }

    public void save() {
        for (UUID uuid : mailBoxes.keySet()) {
            try {
                File file = File.createTempFile(uuid + "-", ".dat", this.mailDir);
                NbtIo.writeCompressed(mailBoxes.get(uuid).createCompoundTag(), file);
                File file2 = new File(this.mailDir, uuid + ".dat");
                File file3 = new File(this.mailDir, uuid + ".dat_old");
                Util.safeReplaceFile(file2, file, file3);
            } catch (Exception var6) {
                LOGGER.warn("Failed to save mailbox data for {}", uuid.toString());
            }
        }
    }

    public void load() {
        mailBoxes.clear();
        try {
            Files.walk(mailDir.toPath(), 1, FileVisitOption.FOLLOW_LINKS).forEach(path -> {
                CompoundTag compoundTag = null;
                if (FilenameUtils.isExtension(path.getFileName().toString(), ".dat")) { // Only load .dat not dat_old
                    String name = FilenameUtils.removeExtension(path.getFileName().toString());
                    try {
                        File file = path.toFile();
                        if (file.exists() && file.isFile()) {
                            compoundTag = NbtIo.readCompressed(file);
                        }
                    } catch (IOException e) {
                        LOGGER.warn("Failed to load mail box data for {}", name);
                        LOGGER.debug(e.getMessage());
                    }
                    if (compoundTag != null) {
                        mailBoxes.put(UUID.fromString(name), new MailBoxContainer());
                        mailBoxes.get(UUID.fromString(name)).fromTag(compoundTag);
                    }
                }
            });
        } catch (IOException var4) {
            LOGGER.warn("Failed to load mail box data");
            LOGGER.debug(var4.getMessage());
        }
    }

    public boolean hasMailbox(UUID uuid) {
        return mailBoxes.containsKey(uuid);
    }

    public MailBoxContainer getMailBox(UUID uuid) {
        return mailBoxes.get(uuid);
    }
}
