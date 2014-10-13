package tonius.neiintegration.railcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerRollingMachineShapeless extends RecipeHandlerRollingMachine {
    
    public int[][] INPUTS = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };
    
    public class CachedRollingMachineShapelessRecipe extends CachedBaseRecipe {
        
        public ArrayList<PositionedStack> inputs;
        public PositionedStack output;
        
        public CachedRollingMachineShapelessRecipe(ItemStack output) {
            this.inputs = new ArrayList<PositionedStack>();
            this.output = new PositionedStack(output, 88, 16);
        }
        
        public CachedRollingMachineShapelessRecipe(Object[] input, ItemStack output) {
            this(Arrays.asList(input), output);
        }
        
        public CachedRollingMachineShapelessRecipe(List<?> input, ItemStack output) {
            this(output);
            this.setIngredients(input);
        }
        
        public void setIngredients(List<?> items) {
            this.inputs.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                PositionedStack stack = new PositionedStack(items.get(ingred), 25 + RecipeHandlerRollingMachineShapeless.this.INPUTS[ingred][0] * 18, 6 + RecipeHandlerRollingMachineShapeless.this.INPUTS[ingred][1] * 18);
                stack.setMaxSize(1);
                this.inputs.add(stack);
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerRollingMachineShapeless.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
    }
    
    @Override
    public String getRecipeNameSub() {
        return "Shapeless";
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
                CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe);
                if (recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe);
                if (recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe);
            if (recipe != null) {
                if (recipe.contains(recipe.inputs, ingredient)) {
                    recipe.setIngredientPermutation(recipe.inputs, ingredient);
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    private CachedRollingMachineShapelessRecipe getCachedRecipe(IRecipe irecipe) {
        CachedRollingMachineShapelessRecipe recipe = null;
        if (irecipe instanceof ShapelessRecipes) {
            recipe = new CachedRollingMachineShapelessRecipe(((ShapelessRecipes) irecipe).recipeItems, ((ShapelessRecipes) irecipe).getRecipeOutput());
        } else if (irecipe instanceof ShapelessOreRecipe) {
            recipe = this.getCachedOreRecipe((ShapelessOreRecipe) irecipe);
        }
        return recipe;
    }
    
    private CachedRollingMachineShapelessRecipe getCachedOreRecipe(ShapelessOreRecipe recipe) {
        ArrayList<Object> items = recipe.getInput();
        for (Object item : items) {
            if (item instanceof List && ((List<?>) item).isEmpty()) {
                return null;
            }
        }
        return new CachedRollingMachineShapelessRecipe(items, recipe.getRecipeOutput());
    }
    
}
