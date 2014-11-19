package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerSewer extends RecipeHandlerBase {
    
    public class CachedSewerRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank tank;
        public boolean essenceRecipe;
        
        public CachedSewerRecipe(boolean essenceRecipe) {
            this.essenceRecipe = essenceRecipe;
            if (!essenceRecipe) {
                this.tank = new PositionedFluidTank(FluidRegistry.getFluidStack("sewage", 4000), 4000, new Rectangle(141, 2, 16, 60), RecipeHandlerSewer.this.getGuiTexture(), new Point(176, 0));
            } else {
                this.tank = new PositionedFluidTank(FluidRegistry.getFluidStack("mobessence", 4000), 4000, new Rectangle(121, 2, 16, 60), RecipeHandlerSewer.this.getGuiTexture(), new Point(176, 0));
            }
            this.tank.showAmount = false;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.tank;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.sewer.name", false);
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.sewer";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "sewer.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(76, 25, 22, 15), this.getRecipeID()));
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(76, 25, 0, 0, 22, 15);
        
        if (!((CachedSewerRecipe) this.arecipes.get(recipe)).essenceRecipe) {
            GuiDraw.drawTexturedModalRect(48, 24, 0, 64, 16, 16);
        } else {
            GuiDraw.drawTexturedModalRect(48, 24, 0, 80, 16, 16);
        }
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(48, 24, 16, 16).contains(relMouse)) {
            if (!((CachedSewerRecipe) crecipe).essenceRecipe) {
                currenttip.add(Utils.translate("handler.sewer.animals"));
                currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.sewer.animals.1"));
            } else {
                currenttip.add(Utils.translate("handler.sewer.xp"));
                currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.sewer.xp.1"));
            }
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedSewerRecipe(false));
        this.arecipes.add(new CachedSewerRecipe(true));
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("sewage")) {
            this.arecipes.add(new CachedSewerRecipe(false));
        } else if (result.getFluid().getName().equals("mobessence")) {
            this.arecipes.add(new CachedSewerRecipe(true));
        }
    }
    
}
