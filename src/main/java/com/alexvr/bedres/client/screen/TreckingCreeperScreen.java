package com.alexvr.bedres.client.screen;

import com.alexvr.bedres.BedrockResources;
import com.alexvr.bedres.entities.treckingcreeper.TreckingCreeperEntity;
import com.alexvr.bedres.entities.treckingcreeper.TreckingCreeperMenu;
import com.alexvr.bedres.utils.BedrockReferences;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TreckingCreeperScreen extends AbstractContainerScreen<TreckingCreeperMenu> {
    /** The ResourceLocation containing the chest GUI texture. */
    public static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(BedrockResources.MODID, BedrockReferences.TRRECKING_CREEPER_GUI_TEXTURE_RESOURCE);
    /** Window height is calculated with these values" the more rows, the higher */
    private final TreckingCreeperMenu container;
    private final TreckingCreeperEntity TC;
    public TreckingCreeperScreen(TreckingCreeperMenu pMenu, Inventory pPlayerInventory, TreckingCreeperEntity tc) {
        super(pMenu, pPlayerInventory, tc.getDisplayName());
        this.passEvents = false;
        this.imageHeight = 166 + 18;
        this.inventoryLabelY = this.imageHeight - 115;
        this.container = pMenu;
        this.TC= tc;
        this.passEvents = false;

    }

    public void render(PoseStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
    }

    protected void renderBg(PoseStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int relX = (this.width - this.imageWidth)/2;
        int relY = (this.height - this.imageHeight)/2;
        this.blit(pMatrixStack, relX, relY, 0, 0, this.getXSize(), this.getYSize());

        //System.out.println(container.tile.getItem(0).getCount());


    }
}