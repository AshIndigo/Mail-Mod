package io.github.ashindigo.mail.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ashindigo.mail.container.MailBoxContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class CheckMailScreen extends AbstractContainerScreen<MailBoxContainer> {

    public CheckMailScreen(MailBoxContainer mailBoxContainer, Inventory playerInv, Component component) {
        super(mailBoxContainer, playerInv, new TranslatableComponent("text.mail.mail"));
    }

    @Override
    protected void renderBg(PoseStack poseStack, float f, int i, int j) {

    }
}
