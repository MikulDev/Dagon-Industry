package net.dagonmomo.dagonindustry.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.dagonmomo.dagonindustry.common.container.WorkbenchContainer;
import net.dagonmomo.dagonindustry.common.te.WorkbenchTileEntity;
import net.dagonmomo.dagonindustry.core.network.PacketHandler;
import net.dagonmomo.dagonindustry.core.network.packet.SetRecipeMessage;
import net.dagonmomo.dagonindustry.core.util.maps.Recipes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class WorkbenchScreen extends ContainerScreen<WorkbenchContainer>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("dagon_industry:textures/gui/workbench.png");
    ItemStack recipeItem = ItemStack.EMPTY;

    public WorkbenchScreen(WorkbenchContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.guiLeft = 0;
        this.guiTop = 0;
        this.xSize = 176;
        this.ySize = 188;
        this.playerInventoryTitleY += 22;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    int middle = this.xSize / 2;

    ImageButton prevRecipeButton = new ImageButton(this.guiLeft + middle - 32, this.guiTop + 4, 14, 16, 228, 0, 16,
                                   new ResourceLocation("dagon_industry:textures/gui/workbench.png"), button ->
                                   {
                                       WorkbenchTileEntity te = (WorkbenchTileEntity) this.container.te;
                                       int newIndex = Math.max(te.getRecipeIndex() - 1, 0);
                                       te.setRecipeIndex(newIndex);
                                       PacketHandler.INSTANCE.sendToServer(new SetRecipeMessage(te.getPos(), newIndex));
                                   });
    ImageButton nextRecipeButton = new ImageButton(this.guiLeft + middle + 32, this.guiTop + 4, 14, 16, 242, 0, 16,
                                   new ResourceLocation("dagon_industry:textures/gui/workbench.png"), button ->
                                   {
                                       WorkbenchTileEntity te = (WorkbenchTileEntity) this.container.te;
                                       int newIndex = Math.min(te.getRecipeIndex() + 1, WorkbenchTileEntity.ORDERED_RECIPES.size() - 1);
                                       te.setRecipeIndex(newIndex);
                                       PacketHandler.INSTANCE.sendToServer(new SetRecipeMessage(te.getPos(), newIndex));
                                   });

    @Override
    protected void init()
    {
        super.init();
        this.addButton(nextRecipeButton);
        this.addButton(prevRecipeButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y)
    {
        prevRecipeButton.setPosition(this.guiLeft + 6, this.guiTop + 17);
        nextRecipeButton.setPosition(this.guiLeft + this.xSize - 20, this.guiTop + 17);
        Minecraft mc = this.minecraft;
        WorkbenchTileEntity te = (WorkbenchTileEntity) this.container.te;

        // Update recipe item
        if (te.ticksExisted % 5 == 0)
        {
            recipeItem = Recipes.WORKBENCH.get(WorkbenchTileEntity.ORDERED_RECIPES.get(te.getRecipeIndex()));
        }

        // Render GUI background
        mc.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        // Render progress bar
        mc.getTextureManager().bindTexture(TEXTURE);
        int progress = te.getProgress();
        this.blit(matrixStack, this.guiLeft + 117, this.guiTop + 56, 176, 16, (int) ((progress / (double) te.getCraftingTime()) * 23), 16);

        // Render no item in battery slot
        if (this.container.te.getStackInSlot(0).isEmpty())
        {
            mc.getTextureManager().bindTexture(TEXTURE);
            this.blit(matrixStack, this.guiLeft + 17, this.guiTop + 56, 176, 0, 16, 16);
        }

        // Render battery bar
        if (this.container.te.getTileData().getBoolean("active"))
        {
            int battProg = te.ticksExisted % 20;
            this.blit(matrixStack, this.guiLeft + 21, this.guiTop + 77, 176, 32, (int) ((battProg / 20d) * 9), 7);
        }

        // Render recipe item
        String itemName = recipeItem.getDisplayName().getString();
        int stringPos = this.guiLeft + middle - font.getStringWidth(itemName) / 2;

        itemRenderer.renderItemIntoGUI(recipeItem, stringPos - 20, this.guiTop + 18);
        this.font.drawStringWithShadow(matrixStack, itemName, stringPos, this.guiTop + 21, 16777215);
    }
}
