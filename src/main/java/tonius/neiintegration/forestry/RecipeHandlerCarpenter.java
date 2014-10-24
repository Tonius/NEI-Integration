package tonius.neiintegration.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.Hacks;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import forestry.core.interfaces.IDescriptiveRecipe;
import forestry.factory.gadgets.MachineCarpenter;

public class RecipeHandlerCarpenter extends RecipeHandlerBase {
    
    public static final Rectangle TANK = new Rectangle(145, 3, 16, 58);
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Hacks.getClass("forestry.factory.gui.GuiCarpenter");
        API.setGuiOffset(guiClass, 5, 14);
    }
    
    public class CachedCarpenterRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedFluidTank tank;
        public PositionedStack output;
        
        public CachedCarpenterRecipe(MachineCarpenter.Recipe recipe, boolean genPerms) {
            IDescriptiveRecipe irecipe = (IDescriptiveRecipe) recipe.asIRecipe();
            this.setIngredients(irecipe.getWidth(), irecipe.getHeight(), irecipe.getIngredients());
            if (recipe.getBox() != null) {
                this.inputs.add(new PositionedStack(recipe.getBox(), 78, 6));
            }
            this.tank = new PositionedFluidTank(TANK, 10000, recipe.getLiquid());
            this.output = new PositionedStack(recipe.getCraftingResult(), 75, 37);
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedCarpenterRecipe(MachineCarpenter.Recipe recipe) {
            this(recipe, false);
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null) {
                        continue;
                    }
                    PositionedStack stack = new PositionedStack(items[y * width + x], 5 + x * 18, 6 + y * 18, false);
                    stack.setMaxSize(1);
                    this.inputs.add(stack);
                }
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerCarpenter.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.tank;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.carpenter";
    }
    
    @Override
    public String getRecipeName() {
        return "Carpenter";
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/carpenter.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(93, 36, 4, 18), this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 5, 14, 166, 65);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(145, 3, 176, 0, 16, 58);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (MachineCarpenter.Recipe recipe : MachineCarpenter.RecipeManager.recipes) {
                this.arecipes.add(new CachedCarpenterRecipe(recipe, true));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MachineCarpenter.Recipe recipe : MachineCarpenter.RecipeManager.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getCraftingResult(), result)) {
                this.arecipes.add(new CachedCarpenterRecipe(recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (MachineCarpenter.Recipe recipe : MachineCarpenter.RecipeManager.recipes) {
            CachedCarpenterRecipe crecipe = new CachedCarpenterRecipe(recipe);
            if (crecipe.contains(crecipe.inputs, ingred)) {
                crecipe.generatePermutations();
                crecipe.setIngredientPermutation(crecipe.inputs, ingred);
                this.arecipes.add(crecipe);
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        for (MachineCarpenter.Recipe recipe : MachineCarpenter.RecipeManager.recipes) {
            if (recipe.getLiquid() != null && recipe.getLiquid().getFluid() == ingredient.getFluid()) {
                this.arecipes.add(new CachedCarpenterRecipe(recipe, true));
            }
        }
    }
    
}
