package it.hurts.sskirillss.relics.client.screen.description.ability.widgets.base;

import com.mojang.blaze3d.systems.RenderSystem;
import it.hurts.sskirillss.relics.client.screen.base.IHoverableWidget;
import it.hurts.sskirillss.relics.client.screen.base.IRelicScreenProvider;
import it.hurts.sskirillss.relics.client.screen.base.ITickingWidget;
import it.hurts.sskirillss.relics.client.screen.description.general.widgets.base.AbstractDescriptionWidget;
import it.hurts.sskirillss.relics.client.screen.description.misc.DescriptionTextures;
import it.hurts.sskirillss.relics.client.screen.description.ability.AbilityDescriptionScreen;
import it.hurts.sskirillss.relics.client.screen.description.relic.particles.ExperienceParticleData;
import it.hurts.sskirillss.relics.client.screen.utils.ParticleStorage;
import it.hurts.sskirillss.relics.network.NetworkHandler;
import it.hurts.sskirillss.relics.network.packets.leveling.PacketRelicTweak;
import it.hurts.sskirillss.relics.utils.Reference;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.awt.*;
import java.util.Locale;

public abstract class AbstractActionWidget extends AbstractDescriptionWidget implements IHoverableWidget, ITickingWidget {
    @Getter
    private final PacketRelicTweak.Operation operation;
    @Getter
    private final IRelicScreenProvider provider;
    @Getter
    private final String ability;

    public AbstractActionWidget(int x, int y, PacketRelicTweak.Operation operation, AbilityDescriptionScreen screen, String ability) {
        super(x, y, 14, 13);

        this.operation = operation;
        this.provider = screen;
        this.ability = ability;
    }

    @Override
    public abstract boolean isLocked();

    @Override
    public void onPress() {
        if (!isLocked())
            NetworkHandler.sendToServer(new PacketRelicTweak(getProvider().getContainer(), getProvider().getSlot(), getAbility(), operation, Screen.hasShiftDown()));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        String actionId = operation.toString().toLowerCase(Locale.ROOT);

        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath(Reference.MODID, "textures/gui/description/ability/" + actionId + "_button_" + (isLocked() ? "inactive" : "active") + ".png"), getX(), getY(), 0, 0, width, height, width, height);

        if (isHovered)
            guiGraphics.blit(DescriptionTextures.ACTION_BUTTON_OUTLINE, getX(), getY(), 0, 0, width, height, width, height);
    }

    @Override
    public void onTick() {
        if (minecraft.player == null)
            return;

        RandomSource random = minecraft.player.getRandom();

        if (!isHovered() || minecraft.player.tickCount % 5 != 0)
            return;

        ParticleStorage.addParticle((Screen) provider, new ExperienceParticleData(
                new Color(200 + random.nextInt(50), 150 + random.nextInt(100), 0),
                getX() + random.nextInt(width), getY() + random.nextInt(height / 4),
                1F + (random.nextFloat() * 0.25F), 50 + random.nextInt(50)));
    }
}