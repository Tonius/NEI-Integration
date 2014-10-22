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
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerSewer extends RecipeHandlerBase {
    
    private static final Rectangle MOBS = new Rectangle(48, 24, 16, 16);
    private static final Rectangle SEWAGE = new Rectangle(141, 2, 16, 60);
    private static final Rectangle ESSENCE = new Rectangle(121, 2, 16, 60);
    
    public class CachedSewerRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank tank;
        public boolean essenceRecipe;
        
        public CachedSewerRecipe(boolean essenceRecipe) {
            this.essenceRecipe = essenceRecipe;
            if (!essenceRecipe) {
                this.tank = new PositionedFluidTank(SEWAGE, 4000, FluidRegistry.getFluidStack("sewage", 4000));
            } else {
                this.tank = new PositionedFluidTank(ESSENCE, 4000, FluidRegistry.getFluidStack("mobessence", 4000));
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
        return "Sewer";
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
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(121, 2, 176, 0, 16, 60);
        GuiDraw.drawTexturedModalRect(141, 2, 176, 0, 16, 60);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (MOBS.contains(relMouse)) {
            if (!((CachedSewerRecipe) crecipe).essenceRecipe) {
                currenttip.add("Animals or villagers");
                currenttip.add(EnumChatFormatting.GRAY + "Larger animals produce more sewage.");
            } else {
                currenttip.add("Experience Orbs");
                currenttip.add(EnumChatFormatting.GRAY + "XP gets converted into Essence.");
            }
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedSewerRecipe(false));
            this.arecipes.add(new CachedSewerRecipe(true));
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
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
