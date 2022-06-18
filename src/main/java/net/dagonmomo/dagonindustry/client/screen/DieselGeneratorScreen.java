package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.DieselGeneratorContainer;
import net.dagonmomo.dagonindustry.common.te.DieselGeneratorTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DieselGeneratorScreen extends ContainerScreen<DieselGeneratorContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/diesel_generator.png");

    public DieselGeneratorScreen(DieselGeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        Minecraft mc = this.minecraft;
        DieselGeneratorTileEntity te = (DieselGeneratorTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render progress bar
        if (te.getBurnTimeLeft() > 0)
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            int progress = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 78, this.guiTop + 18, 176, 16, (int) (progress / (20 / 19.0)), 7);
        }

        // Render no item in battery slots
        if (this.container.te.getStackInSlot(0).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 62, this.guiTop + 34, 176, 23, 16, 16);
        }
        if (this.container.te.getStackInSlot(1).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 34, 176, 23, 16, 16);
        }
        if (this.container.te.getStackInSlot(2).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 98, this.guiTop + 34, 176, 23, 16, 16);
        }

        // Render no item in coal slot
        if (this.container.te.getStackInSlot(3).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 57, 176, 0, 16, 16);
        }

        // Render burn progress
        if (te.getBurnTimeLeft() > 0)
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            int burnLeft = te.getBurnTimeLeft();
            int burnTime = te.getBurnTime();
            int burnProgressScaled = (int) (burnLeft / (burnTime / 12d));
            this.blit(matrixStack, this.guiLeft + 62, this.guiTop + 57 + (13 - burnProgressScaled), 176, 39 + (12 - burnProgressScaled), 13, 1 + (int) (burnLeft / (burnTime / 12d)));
        }
    }
}
