package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.BioRefineryContainer;
import net.dagonmomo.dagonindustry.common.te.BioRefineryTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BioRefineryScreen extends ContainerScreen<BioRefineryContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/bio_refinery.png");

    public BioRefineryScreen(BioRefineryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        BioRefineryTileEntity te = (BioRefineryTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render no item in biofuel slots
        for (int i = 0; i < 5; i++)
        {
            if (this.container.te.getStackInSlot(i).isEmpty())
            {
                mc.getTextureManager().bindTexture(TEXTURE);
                this.blit(matrixStack, this.guiLeft + 44 + 18 * i, this.guiTop + 28, 176, 30, 16, 16);
            }
        }

        // Render no item in bucket slot
        if (this.container.te.getStackInSlot(5).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 62, 176, 46, 16, 16);
        }

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(6).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 14, this.guiTop + 28, 176, 0, 16, 16);
        }

        // Render burn progress
        if (te.getProgress() > 0)
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            int timeLeft = te.getProgress();
            int maxTime = BioRefineryTileEntity.MAX_PROGRESS;
            int progressScaled = (int) (timeLeft / (maxTime / 14d));
            this.blit(matrixStack, this.guiLeft + 82, this.guiTop + 59 - progressScaled, 176, 29 - progressScaled, 11, 1 + (int) (timeLeft / (maxTime / 13d)));
        }

        // Render battery bar
        if (this.container.te.getTileData().getBoolean("active"))
        {
            int progress = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 18, this.guiTop + 49, 176, 62, progress / (20/9), 7);
        }
    }
}
