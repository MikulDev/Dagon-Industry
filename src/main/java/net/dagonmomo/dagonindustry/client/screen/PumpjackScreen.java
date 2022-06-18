package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.PumpjackContainer;
import net.dagonmomo.dagonindustry.common.te.PumpjackTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PumpjackScreen extends ContainerScreen<PumpjackContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/pumpjack.png");

    public PumpjackScreen(PumpjackContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        PumpjackTileEntity te = (PumpjackTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(0).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 17, this.guiTop + 41, 176, 16, 16, 16);
        }

        // Render no item in bucket slots
        for (int i = 0; i < 5; i++)
        {
            if (this.container.te.getStackInSlot(i + 1).isEmpty())
            {
                mc.getTextureManager().bindTexture(TEXTURE);
                this.blit(matrixStack, this.guiLeft + 53 + i * 18, this.guiTop + 41, 176, 0, 16, 16);
            }
        }

        // Render battery bar
        if (this.container.te.getTileData().getBoolean("isPumping"))
        {
            int battProg = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 21, this.guiTop + 62, 176, 32, battProg / (20/9), 7);
        }
    }
}
