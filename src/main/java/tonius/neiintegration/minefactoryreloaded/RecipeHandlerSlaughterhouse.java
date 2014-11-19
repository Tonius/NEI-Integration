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
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerSlaughterhouse extends RecipeHandlerBase {
    
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        TileEntitySlaughterhouse dummy = new TileEntitySlaughterhouse();
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedSlaughterhouseRecipe extends CachedBaseRecipe {
        
        public List<PositionedFluidTank> tanks = new ArrayList<PositionedFluidTank>();
        
        public CachedSlaughterhouseRecipe() {
            PositionedFluidTank tank = new PositionedFluidTank(FluidRegistry.getFluidStack("meat", 4000), 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerSlaughterhouse.this.getGuiTexture(), new Point(176, 0));
            tank.showAmount = false;
            this.tanks.add(tank);
            tank = new PositionedFluidTank(FluidRegistry.getFluidStack("pinkslime", 4000), 4000, new Rectangle(91, 2, 16, 60), RecipeHandlerSlaughterhouse.this.getGuiTexture(), new Point(176, 0));
            tank.showAmount = false;
            this.tanks.add(tank);
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public List<PositionedFluidTank> getFluidTanks() {
            return this.tanks;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.slaughterhouse.name", false);
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
    public void drawExtras(int recipe) {
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 20, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(28, 24, 16, 16).contains(relMouse)) {
            currenttip.add(Utils.translate("handler.slaughterhouse.animals"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.slaughterhouse.animals.1"));
        } else if (new Rectangle(129, 2, 8, 60).contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedSlaughterhouseRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName().equals("meat") || result.getFluid().getName().equals("pinkslime")) {
            this.loadAllRecipes();
        }
    }
    
}
