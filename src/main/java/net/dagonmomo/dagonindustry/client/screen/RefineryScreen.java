package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.RefineryContainer;
import net.dagonmomo.dagonindustry.common.te.RefineryTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class RefineryScreen extends ContainerScreen<RefineryContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/refinery.png");

    public RefineryScreen(RefineryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        RefineryTileEntity te = (RefineryTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render progress bar
        mc.getTextureManager().bindTexture(TEXTURE);
        int progress = te.getProgress();
        this.blit(matrixStack, this.guiLeft + 57, this.guiTop + 17, 176, 42, (int) (progress / (RefineryTileEntity.MAX_PROGRESS / 62d)), 13);

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(0).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 17, this.guiTop + 41, 176, 16, 16, 16);
        }

        // Render no item in input
        if (this.container.te.getStackInSlot(1).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 80, this.guiTop + 61, 176, 0, 16, 16);
        }

        // Render no item in output slots
        for (int i = 0; i < 3; i++)
        {
            if (this.container.te.getStackInSlot(i + 2).isEmpty())
            {
                mc.getTextureManager().bindTexture(TEXTURE);
                this.blit(matrixStack, this.guiLeft + 62 + i * 18, this.guiTop + 32, 176, 0, 16, 16);
            }
        }

        // Render battery bar
        if (this.container.te.getTileData().getBoolean("active"))
        {
            int battProg = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 21, this.guiTop + 62, 176, 32, battProg / (20/9), 7);
        }
    }
}
