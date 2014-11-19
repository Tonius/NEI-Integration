package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityGrinder;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerGrinder extends RecipeHandlerBase {
    
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntityGrinder dummy = new TileEntityGrinder();
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedGrinderRecipe extends CachedBaseRecipe {
        
        PositionedFluidTank tank;
        
        public CachedGrinderRecipe() {
            this.tank = new PositionedFluidTank(FluidRegistry.getFluidStack("mobessence", 4000), 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerGrinder.this.getGuiTexture(), new Point(176, 0));
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
        return Utils.translate("tile.mfr.machine.grinder.name", false);
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.grinder";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "grinder.png";
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(74, 25, 0, 0, 22, 15);
        GuiDraw.drawTexturedModalRect(44, 24, 16, 64, 16, 16);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 20, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(44, 24, 16, 16).contains(relMouse)) {
            currenttip.add(Utils.translate("handler.grinder.mobs"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.grinder.mobs.1"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.grinder.mobs.2"));
        } else if (new Rectangle(129, 2, 8, 60).contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedGrinderRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("mobessence")) {
            this.loadAllRecipes();
        }
    }
    
}
