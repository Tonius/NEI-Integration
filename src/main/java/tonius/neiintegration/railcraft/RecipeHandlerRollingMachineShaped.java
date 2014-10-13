package tonius.neiintegration.railcraft;

import java.util.ArrayList;
import java.util.List;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import codechicken.core.ReflectionManager;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerRollingMachineShaped extends RecipeHandlerRollingMachine {
    
    public class CachedRollingMachineShapedRecipe extends CachedBaseRecipe {
        
        public ArrayList<PositionedStack> inputs;
        public PositionedStack output;
        
        public CachedRollingMachineShapedRecipe(int width, int height, Object[] items, ItemStack output) {
            this.output = new PositionedStack(output, 88, 18);
            this.inputs = new ArrayList<PositionedStack>();
            this.setIngredients(width, height, items);
        }
        
        public CachedRollingMachineShapedRecipe(ShapedRecipes recipe) {
            this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null) {
                        continue;
                    }
                    
                    PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 8 + y * 18);
                    stack.setMaxSize(1);
                    this.inputs.add(stack);
                }
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerRollingMachineShaped.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
    }
    
    @Override
    public String getRecipeNameSub() {
        return "Shaped";
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
                CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe);
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
                CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe);
                if (recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe);
            if (recipe != null) {
                if (recipe.contains(recipe.inputs, ingredient)) {
                    recipe.setIngredientPermutation(recipe.inputs, ingredient);
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    private CachedRollingMachineShapedRecipe getCachedRecipe(IRecipe irecipe) {
        CachedRollingMachineShapedRecipe recipe = null;
        if (irecipe instanceof ShapedRecipes) {
            recipe = new CachedRollingMachineShapedRecipe((ShapedRecipes) irecipe);
        } else if (irecipe instanceof ShapedOreRecipe) {
            recipe = this.getCachedOreRecipe((ShapedOreRecipe) irecipe);
        }
        return recipe;
    }
    
    private CachedRollingMachineShapedRecipe getCachedOreRecipe(ShapedOreRecipe recipe) {
        int width;
        int height;
        try {
            width = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4);
            height = ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5);
        } catch (Exception e) {
            return null;
        }
        
        Object[] items = recipe.getInput();
        for (Object item : items) {
            if (item instanceof List && ((List<?>) item).isEmpty()) {
                return null;
            }
        }
        
        return new CachedRollingMachineShapedRecipe(width, height, items, recipe.getRecipeOutput());
    }
    
}
