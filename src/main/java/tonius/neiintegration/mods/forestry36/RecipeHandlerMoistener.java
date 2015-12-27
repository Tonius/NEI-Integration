package tonius.neiintegration.mods.forestry36;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.nei.PositionedStack;
import forestry.api.fuels.FuelManager;
import forestry.api.fuels.MoistenerFuel;
import forestry.factory.gadgets.MachineMoistener;

public class RecipeHandlerMoistener extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiMoistener");
    }
    
    public class CachedMoistenerRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank tank;
        public List<PositionedStack> fuels = new ArrayList<PositionedStack>();
        public PositionedStack input;
        public PositionedStack output;
        
        public CachedMoistenerRecipe(MachineMoistener.Recipe recipe, MoistenerFuel fuel) {
            this.tank = new PositionedFluidTank(FluidRegistry.getFluidStack("water", 10000), 10000, new Rectangle(11, 5, 16, 58), RecipeHandlerMoistener.this.getGuiTexture(), new Point(176, 0));
            this.tank.showAmount = false;
            if (fuel.item != null) {
                this.fuels.add(new PositionedStack(fuel.item, 34, 47));
            }
            if (fuel.product != null) {
                this.fuels.add(new PositionedStack(fuel.product, 100, 26));
            }
            if (recipe.resource != null) {
                this.input = new PositionedStack(recipe.resource, 138, 8);
            }
            if (recipe.product != null) {
                this.output = new PositionedStack(recipe.product, 138, 44);
            }
        }
        
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.fuels;
        }
        
        @Override
        public PositionedStack getIngredient() {
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.tank;
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.moistener";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.for.factory.4.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/moistener.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(138, 27, 16, 14);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        this.drawProgressBar(88, 6, 176, 91, 29, 55, 80, 3);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(119, 25, 176, 74, 16, 15, 160, 0);
    }
    
    @Override
    public void loadAllRecipes() {
        for (MachineMoistener.Recipe recipe : MachineMoistener.RecipeManager.recipes) {
            for (MoistenerFuel fuel : FuelManager.moistenerResource.values()) {
                this.arecipes.add(new CachedMoistenerRecipe(recipe, fuel));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MachineMoistener.Recipe recipe : MachineMoistener.RecipeManager.recipes) {
            for (MoistenerFuel fuel : FuelManager.moistenerResource.values()) {
                if (Utils.areStacksSameTypeCraftingSafe(recipe.product, result) || Utils.areStacksSameTypeCraftingSafe(fuel.product, result)) {
                    this.arecipes.add(new CachedMoistenerRecipe(recipe, fuel));
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (MachineMoistener.Recipe recipe : MachineMoistener.RecipeManager.recipes) {
            for (MoistenerFuel fuel : FuelManager.moistenerResource.values()) {
                if (Utils.areStacksSameTypeCraftingSafe(recipe.resource, ingred) || Utils.areStacksSameTypeCraftingSafe(fuel.item, ingred)) {
                    this.arecipes.add(new CachedMoistenerRecipe(recipe, fuel));
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        if (ingredient.getFluid() == FluidRegistry.WATER) {
            this.loadAllRecipes();
        }
    }
    
}
