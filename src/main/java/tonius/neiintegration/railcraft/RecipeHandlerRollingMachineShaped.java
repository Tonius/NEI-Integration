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
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedRollingMachineShapedRecipe(int width, int height, Object[] items, ItemStack output, boolean genPerms) {
            this.setIngredients(width, height, items);
            this.output = new PositionedStack(output, 88, 18);
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedRollingMachineShapedRecipe(int width, int height, Object[] items, ItemStack output) {
            this(width, height, items, output, false);
        }
        
        public CachedRollingMachineShapedRecipe(ShapedRecipes recipe, boolean genPerms) {
            this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput(), genPerms);
        }
        
        public CachedRollingMachineShapedRecipe(ShapedRecipes recipe) {
            this(recipe, false);
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null) {
                        continue;
                    }
                    
                    PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 8 + y * 18, false);
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
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeSubName() {
        return "Shaped";
    }
    
    private CachedRollingMachineShapedRecipe getCachedRecipe(IRecipe irecipe, boolean genPerms) {
        CachedRollingMachineShapedRecipe recipe = null;
        if (irecipe instanceof ShapedRecipes) {
            recipe = new CachedRollingMachineShapedRecipe((ShapedRecipes) irecipe, genPerms);
        } else if (irecipe instanceof ShapedOreRecipe) {
            recipe = this.getCachedOreRecipe((ShapedOreRecipe) irecipe, genPerms);
        }
        return recipe;
    }
    
    private CachedRollingMachineShapedRecipe getCachedOreRecipe(ShapedOreRecipe recipe, boolean genPerms) {
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
        
        return new CachedRollingMachineShapedRecipe(width, height, items, recipe.getRecipeOutput(), genPerms);
    }
    
    @Override
    public void loadAllRecipes() {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe, true);
            if (recipe != null) {
                this.arecipes.add(recipe);
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe, true);
                if (recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            CachedRollingMachineShapedRecipe recipe = this.getCachedRecipe(irecipe, false);
            if (recipe != null) {
                if (recipe.contains(recipe.inputs, ingredient)) {
                    recipe.generatePermutations();
                    recipe.setIngredientPermutation(recipe.inputs, ingredient);
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
}
