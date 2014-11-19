package tonius.neiintegration.forestry;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import forestry.factory.gadgets.MachineBottler;

public class RecipeHandlerBottler extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    private static List<MachineBottler.Recipe> recipes = new ArrayList<MachineBottler.Recipe>();
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiBottler");
        for (FluidContainerData container : FluidContainerRegistry.getRegisteredFluidContainerData()) {
            MachineBottler.Recipe recipe = MachineBottler.RecipeManager.findMatchingRecipe(container.fluid, container.emptyContainer);
            if (recipe != null) {
                recipes.add(recipe);
            }
        }
    }
    
    public class CachedBottlerRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank fluid;
        public PositionedStack input;
        public PositionedStack output;
        
        public CachedBottlerRecipe(MachineBottler.Recipe recipe) {
            this.fluid = new PositionedFluidTank(recipe.input, 10000, new Rectangle(48, 6, 16, 58), RecipeHandlerBottler.this.getGuiTexture(), new Point(176, 0));
            this.input = new PositionedStack(recipe.can, 111, 8);
            this.output = new PositionedStack(recipe.bottled, 111, 44);
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
            return this.fluid;
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.bottler";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.for.factory.0.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/bottler.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(75, 27, 24, 17), this.getRecipeID()));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(43, 0, 48, 11, 123, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(75, 27, 176, 74, 24, 17, 40, 0);
    }
    
    @Override
    public void loadAllRecipes() {
        for (MachineBottler.Recipe recipe : recipes) {
            this.arecipes.add(new CachedBottlerRecipe(recipe));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MachineBottler.Recipe recipe : recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.bottled, result)) {
                this.arecipes.add(new CachedBottlerRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (MachineBottler.Recipe recipe : recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.can, ingred)) {
                this.arecipes.add(new CachedBottlerRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingred) {
        for (MachineBottler.Recipe recipe : recipes) {
            if (Utils.areFluidsSameType(recipe.input, ingred)) {
                this.arecipes.add(new CachedBottlerRecipe(recipe));
            }
        }
    }
    
}
