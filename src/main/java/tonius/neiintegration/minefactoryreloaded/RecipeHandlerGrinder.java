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
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerGrinder extends RecipeHandlerBase {
    
    private static final Rectangle MOBS = new Rectangle(44, 24, 16, 16);
    private static final Rectangle ESSENCE = new Rectangle(111, 2, 16, 60);
    private static final Rectangle ENERGY = new Rectangle(129, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(139, 2, 8, 60);
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
            this.tank = new PositionedFluidTank(ESSENCE, 4000, FluidRegistry.getFluidStack("mobessence", 4000));
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
        return "Grinder";
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
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(111, 2, 176, 0, 16, 60);
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 20, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (MOBS.contains(relMouse)) {
            currenttip.add("Mobs");
            currenttip.add(EnumChatFormatting.GRAY + "Mobs that drop more XP");
            currenttip.add(EnumChatFormatting.GRAY + "produce more Essence.");
        } else if (ENERGY.contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedGrinderRecipe());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("mobessence")) {
            this.arecipes.add(new CachedGrinderRecipe());
        }
    }
    
}
