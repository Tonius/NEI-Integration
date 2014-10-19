package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLavaFabricator;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerLavaFabricator extends RecipeHandlerBase {
    
    private static final Rectangle LAVA = new Rectangle(111, 2, 16, 60);
    private static final Rectangle ENERGY = new Rectangle(129, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(139, 2, 8, 60);
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
            this.lavaOutput = new PositionedFluidTank(LAVA, 4000, FluidRegistry.getFluidStack("lava", lavaPerOperation));
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
        return "Lava Fabricator";
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
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(111, 2, 176, 0, 16, 60);
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 10, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (ENERGY.contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedLavaFabricatorRecipe());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("lava")) {
            this.arecipes.add(new CachedLavaFabricatorRecipe());
        }
    }
    
}
