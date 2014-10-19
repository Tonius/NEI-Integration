package tonius.neiintegration;

import java.awt.Rectangle;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;

public class PositionedFluidTank {
    
    public Rectangle position;
    public FluidTank tank;
    public boolean flowingTexture = false;
    public boolean showAmount = true;
    public boolean perTick = false;
    
    public PositionedFluidTank(Rectangle position, FluidTank tank) {
        this.position = position;
        this.tank = tank;
    }
    
    public PositionedFluidTank(Rectangle position, int capacity, FluidStack fluid) {
        this(position, new FluidTank(fluid, capacity));
    }
    
    public List<String> handleTooltip(List<String> currenttip) {
        if (this.tank == null || this.tank.getFluid() == null || this.tank.getFluid().getFluid() == null || this.tank.getFluid().amount <= 0) {
            return currenttip;
        }
        currenttip.add(this.tank.getFluid().getLocalizedName());
        if (this.showAmount) {
            currenttip.add(EnumChatFormatting.GRAY.toString() + this.tank.getFluid().amount + (this.perTick ? " mB/t" : " mB"));
        }
        return currenttip;
    }
    
    public boolean transfer(boolean usage) {
        if (tank.getFluid() != null && tank.getFluid().amount > 0) {
            if (usage) {
                if (!GuiUsageRecipe.openRecipeGui("liquid", new Object[] { tank.getFluid() })) {
                    return false;
                }
            } else {
                if (!GuiCraftingRecipe.openRecipeGui("liquid", new Object[] { tank.getFluid() })) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    public void draw() {
        if (this.tank == null || this.tank.getFluid() == null || this.tank.getFluid().getFluid() == null || this.tank.getFluid().amount <= 0) {
            return;
        }
        IIcon fluidIcon = null;
        if (this.flowingTexture && this.tank.getFluid().getFluid().getFlowingIcon() != null) {
            fluidIcon = this.tank.getFluid().getFluid().getFlowingIcon();
        } else if (this.tank.getFluid().getFluid().getStillIcon() != null) {
            fluidIcon = this.tank.getFluid().getFluid().getStillIcon();
        } else {
            return;
        }
        
        GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
        int color = this.tank.getFluid().getFluid().getColor(this.tank.getFluid());
        GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
        GL11.glDisable(GL11.GL_BLEND);
        
        int amount = Math.max(Math.min(this.position.height, this.tank.getFluid().amount * this.position.height / this.tank.getCapacity()), 1);
        int posY = this.position.y + this.position.height - amount;
        
        for (int i = 0; i < this.position.width; i += 16) {
            for (int j = 0; j < amount; j += 16) {
                int drawWidth = Math.min(this.position.width - i, 16);
                int drawHeight = Math.min(amount - j, 16);
                
                int drawX = this.position.x + i;
                int drawY = posY + j;
                
                double minU = fluidIcon.getMinU();
                double maxU = fluidIcon.getMaxU();
                double minV = fluidIcon.getMinV();
                double maxV = fluidIcon.getMaxV();
                
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(drawX, drawY + drawHeight, 0, minU, minV + (maxV - minV) * drawHeight / 16F);
                tessellator.addVertexWithUV(drawX + drawWidth, drawY + drawHeight, 0, minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F);
                tessellator.addVertexWithUV(drawX + drawWidth, drawY, 0, minU + (maxU - minU) * drawWidth / 16F, minV);
                tessellator.addVertexWithUV(drawX, drawY, 0, minU, minV);
                tessellator.draw();
            }
        }
        
        GL11.glEnable(GL11.GL_BLEND);
    }
    
}
