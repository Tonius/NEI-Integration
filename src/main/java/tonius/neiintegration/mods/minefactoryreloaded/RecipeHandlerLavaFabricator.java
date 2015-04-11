package tonius.neiintegration.mods.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLavaFabricator;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerLavaFabricator extends RecipeHandlerBase {
    
    private static int lavaPerOperation = 20;
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntityLavaFabricator dummy = new TileEntityLavaFabricator();
        energyPerOperation = dummy.getActivationEnergy();
        dummy = null;
    }
    
    public class CachedLavaFabricatorRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank lavaOutput;
        
        public CachedLavaFabricatorRecipe() {
            this.lavaOutput = new PositionedFluidTank(FluidRegistry.getFluidStack("lava", lavaPerOperation), 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerLavaFabricator.this.getGuiTexture(), new Point(176, 0));
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.lavaOutput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.lavafabricator.name", false);
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.lavafabricator";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "lavafabricator.png";
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 10, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(129, 2, 8, 60).contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedLavaFabricatorRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName() != null && result.getFluid().getName().equals("lava")) {
            this.loadAllRecipes();
        }
    }
    
}
