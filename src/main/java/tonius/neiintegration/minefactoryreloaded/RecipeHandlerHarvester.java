package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityHarvester;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerHarvester extends RecipeHandlerBase {
    
    private static int sludgePerOperation = 10;
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntityHarvester dummy = new TileEntityHarvester();
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedHarvesterRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank sludgeOutput;
        
        public CachedHarvesterRecipe() {
            this.sludgeOutput = new PositionedFluidTank(FluidRegistry.getFluidStack("sludge", sludgePerOperation), 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerHarvester.this.getGuiTexture(), new Point(176, 0));
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.sludgeOutput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.harvester.name", false);
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
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(48, 24, 0, 48, 16, 16);
        GuiDraw.drawTexturedModalRect(76, 25, 0, 0, 22, 15);
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
        } else if (new Rectangle(48, 24, 16, 16).contains(relMouse)) {
            currenttip.add(Utils.translate("handler.harvester.harvestables"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.harvester.harvestables.1"));
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedHarvesterRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName() != null && result.getFluid().getName().equals("sludge")) {
            this.loadAllRecipes();
        }
    }
    
}
