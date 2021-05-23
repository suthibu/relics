package it.hurts.sskirillss.relics.tiles.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.hurts.sskirillss.relics.items.RuneItem;
import it.hurts.sskirillss.relics.tiles.RunicAltarTile;
import it.hurts.sskirillss.relics.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class RunicAltarTileRenderer extends TileEntityRenderer<RunicAltarTile> {
    private int iteration;

    public RunicAltarTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(RunicAltarTile tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        for (Direction direction : RunicAltarTile.runeDirections) {
            ItemStack rune = tileEntity.getStack(direction);
            if (rune == null || rune.isEmpty() || !(rune.getItem() instanceof RuneItem)) continue;
            iteration++;
            matrixStack.pushPose();
            matrixStack.translate(0.5F, 1.25F, 0.5F);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees((tileEntity.ticksExisted / 40.0F) * (180.0F / (float) Math.PI)
                    + (iteration * (360.0F / tileEntity.getRunes().size()))));
            matrixStack.translate(-0.5F, 0.0F, -1.0F);
            matrixStack.translate(0.0F, MathHelper.sin(tileEntity.ticksExisted / 15.0F) * 0.1F, 0.0F);
            matrixStack.mulPose(Vector3f.YP.rotation(6.75F));
            matrixStack.scale(0.35F, 0.35F, 0.35F);
            itemRenderer.render(rune, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLight,
                    combinedOverlay, itemRenderer.getModel(rune, tileEntity.getLevel(), null));
            matrixStack.mulPose(Vector3f.XP.rotation(90.0F + MathHelper.sin(tileEntity.ticksExisted / 15.0F) * 0.1F));
            RenderUtils.renderBeam(matrixStack, buffer, partialTicks, 2.5F, ((RuneItem) rune.getItem()).getColor());
            matrixStack.popPose();
        }
        iteration = 0;
        ItemStack relic = tileEntity.getStack(Direction.UP);
        if (relic == null || relic.isEmpty()) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5F, 0.775F, 0.5F);
        matrixStack.mulPose(Direction.NORTH.getRotation());
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        itemRenderer.render(relic, ItemCameraTransforms.TransformType.FIXED, true, matrixStack, buffer, combinedLight,
                combinedOverlay, itemRenderer.getModel(relic, tileEntity.getLevel(), null));
        matrixStack.popPose();
    }
}