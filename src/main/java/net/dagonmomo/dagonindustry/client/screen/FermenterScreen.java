package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.FermenterContainer;
import net.dagonmomo.dagonindustry.common.te.FermenterTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FermenterScreen extends ContainerScreen<FermenterContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/fermenter.png");

    public FermenterScreen(FermenterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        FermenterTileEntity te = (FermenterTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render progress bar
        for (int i = 0; i < 5; i++)
        {
            int progress = te.getProgress(i);
            this.blit(matrixStack, this.guiLeft + 34 + 23 * i, this.guiTop + 44, 176, 0, (int) (progress / Math.max(0.01, FermenterTileEntity.MAX_PROGRESS / 16d)), 4);
        }

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(6).isEmpty())
        {
            this.blit(matrixStack, this.guiLeft + 10, this.guiTop + 35, 176, 20, 16, 16);
        }

        // Render no item in fermenting slots
        for (int i = 0; i < 5; i++)
        {
            if (this.container.te.getStackInSlot(i).isEmpty())
            {
                this.blit(matrixStack, this.guiLeft + 34 + 23 * i, this.guiTop + 23, 176, 4, 16, 16);
            }
        }

        // Render battery bar
        if (te.getTileData().getBoolean("active"))
        {
            int battProg = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 14, this.guiTop + 56, 176, 36, battProg / (20/9), 7);
        }
    }
}
