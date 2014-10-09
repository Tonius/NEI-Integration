package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySlaughterhouse;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerSlaughterhouse extends RecipeHandlerBase {
    
    private static final Rectangle ANIMALS = new Rectangle(28, 24, 16, 16);
    private static final Rectangle MEAT = new Rectangle(111, 2, 16, 60);
    private static final Rectangle PINKSLIME = new Rectangle(91, 2, 16, 60);
    private static final Rectangle ENERGY = new Rectangle(129, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(139, 2, 8, 60);
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntitySlaughterhouse dummy = new TileEntitySlaughterhouse();
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedSlaughterhouseRecipe extends CachedBaseRecipe {
        
        public List<FluidTankElement> tanks = new ArrayList<FluidTankElement>();
        
        public CachedSlaughterhouseRecipe() {
            FluidTankElement tank = new FluidTankElement(MEAT, 4000, FluidRegistry.getFluidStack("meat", 4000));
            tank.showAmount = false;
            this.tanks.add(tank);
            tank = new FluidTankElement(PINKSLIME, 4000, FluidRegistry.getFluidStack("pinkslime", 4000));
            tank.showAmount = false;
            this.tanks.add(tank);
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public List<FluidTankElement> getFluidTanks() {
            return this.tanks;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Slaughterhouse";
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.slaughterhouse";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "slaughterhouse.png";
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(56, 25, 0, 0, 22, 15);
        GuiDraw.drawTexturedModalRect(28, 24, 0, 64, 16, 16);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(91, 2, 176, 0, 16, 60);
        GuiDraw.drawTexturedModalRect(111, 2, 176, 0, 16, 60);
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 20, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (ANIMALS.contains(relMouse)) {
            currenttip.add("Animals");
            currenttip.add(EnumChatFormatting.GRAY + "Larger animals produce more meat.");
        } else if (ENERGY.contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedSlaughterhouseRecipe());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("meat") || result.getFluid().getName().equals("pinkslime")) {
            this.arecipes.add(new CachedSlaughterhouseRecipe());
        }
    }
    
}
