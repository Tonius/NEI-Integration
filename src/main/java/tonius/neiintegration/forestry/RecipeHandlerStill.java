package tonius.neiintegration.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import forestry.factory.gadgets.MachineStill;

public class RecipeHandlerStill extends RecipeHandlerBase {
    
    private static final Rectangle INPUT = new Rectangle(30, 4, 16, 58);
    private static final Rectangle OUTPUT = new Rectangle(120, 4, 16, 58);
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiStill");
    }
    
    public class CachedStillRecipe extends CachedBaseRecipe {
        
        public List<PositionedFluidTank> tanks = new ArrayList<PositionedFluidTank>();
        
        public CachedStillRecipe(MachineStill.Recipe recipe) {
            this.tanks.add(new PositionedFluidTank(INPUT, 10000, recipe.input));
            this.tanks.add(new PositionedFluidTank(OUTPUT, 10000, recipe.output));
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
    public String getRecipeID() {
        return "forestry.still";
    }
    
    @Override
    public String getRecipeName() {
        return "Still";
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/still.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(76, 27, 14, 12), this.getRecipeID()));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(25, 0, 30, 11, 116, 65);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(30, 4, 176, 0, 16, 58);
        GuiDraw.drawTexturedModalRect(120, 4, 176, 0, 16, 58);
        GuiDraw.drawTexturedModalRect(77, 46, 176, 60, 14, 14);
        this.drawProgressBar(79, 6, 176, 74, 4, 18, 80, 11);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (MachineStill.Recipe recipe : MachineStill.RecipeManager.recipes) {
                this.arecipes.add(new CachedStillRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (MachineStill.Recipe recipe : MachineStill.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.output, result)) {
                this.arecipes.add(new CachedStillRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingred) {
        for (MachineStill.Recipe recipe : MachineStill.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.input, ingred)) {
                this.arecipes.add(new CachedStillRecipe(recipe));
            }
        }
    }
    
}
