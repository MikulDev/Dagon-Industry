package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.AlloyFurnaceContainer;
import net.dagonmomo.dagonindustry.common.te.AlloyFurnaceTileEntity;
import net.dagonmomo.dagonindustry.common.te.ArcFurnaceTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;

public class AlloyFurnaceScreen extends ContainerScreen<AlloyFurnaceContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/alloy_furnace.png");

    public AlloyFurnaceScreen(AlloyFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
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
        AlloyFurnaceTileEntity te = (AlloyFurnaceTileEntity) this.container.te;

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render progress bar
        mc.getTextureManager().bindTexture(TEXTURE);
        int progress = te.getProgress();
        this.blit(matrixStack, this.guiLeft + 78, this.guiTop + 40, 176, 16, (int) (progress / Math.max(0.01, AlloyFurnaceTileEntity.MAX_PROGRESS / 23d)), 16);

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(0).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 17, this.guiTop + 33, 176, 33, 16, 16);
        }

        // Render no item in ingot slot
        if (this.container.te.getStackInSlot(1).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 55, this.guiTop + 41, 176, 0, 16, 16);
        }

        // Render no item in coal slot
        if (this.container.te.getStackInSlot(2).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 55, this.guiTop + 21, 176, 49, 16, 16);
        }

        // Render battery bar
        if (this.container.te.getTileData().getBoolean("active"))
        {
            int battProg = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 21, this.guiTop + 53, 176, 65, battProg / (20/9), 7);
        }
    }
}
