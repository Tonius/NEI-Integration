package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityHarvester;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerHarvester extends RecipeHandlerBase {
    
    private static final Rectangle PLANTS = new Rectangle(48, 24, 16, 16);
    private static final Rectangle SLUDGE = new Rectangle(111, 2, 16, 60);
    private static final Rectangle ENERGY = new Rectangle(129, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(139, 2, 8, 60);
    private static int sludgePerOperation = 10;
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntityHarvester dummy = new TileEntityHarvester();
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedHarvesterRecipe extends CachedBaseRecipe {
        
        public FluidTankElement sludgeOutput;
        
        public CachedHarvesterRecipe() {
            this.sludgeOutput = new FluidTankElement(SLUDGE, 4000, FluidRegistry.getFluidStack("sludge", sludgePerOperation));
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public FluidTankElement getFluidTank() {
            return this.sludgeOutput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Harvester";
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.harvester";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "harvester.png";
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(48, 24, 0, 48, 16, 16);
        GuiDraw.drawTexturedModalRect(76, 25, 0, 0, 22, 15);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
        } else if (PLANTS.contains(relMouse)) {
            currenttip.add("Harvestable blocks");
            currenttip.add(EnumChatFormatting.GRAY + "Sludge is a byproduct of harvesting.");
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedHarvesterRecipe());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("sludge")) {
            this.arecipes.add(new CachedHarvesterRecipe());
        }
    }
    
}
