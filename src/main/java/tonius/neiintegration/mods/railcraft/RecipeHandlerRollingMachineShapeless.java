package tonius.neiintegration.mods.railcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import tonius.neiintegration.Utils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerRollingMachineShapeless extends RecipeHandlerRollingMachine {
    
    private static int[][] INPUTS = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };
    
    public class CachedRollingMachineShapelessRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedRollingMachineShapelessRecipe(ItemStack output) {
            this.output = new PositionedStack(output, 88, 16);
        }
        
        public CachedRollingMachineShapelessRecipe(List<?> input, ItemStack output, boolean genPerms) {
            this(output);
            this.setIngredients(input);
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedRollingMachineShapelessRecipe(List<?> input, ItemStack output) {
            this(input, output, false);
        }
        
        public CachedRollingMachineShapelessRecipe(Object[] input, ItemStack output, boolean genPerms) {
            this(Arrays.asList(input), output, genPerms);
        }
        
        public CachedRollingMachineShapelessRecipe(Object[] input, ItemStack output) {
            this(input, output, false);
        }
        
        public void setIngredients(List<?> items) {
            this.inputs.clear();
            for (int ingred = 0; ingred < items.size(); ingred++) {
                Object item = items.get(ingred);
                if (item == null) {
                    continue;
                } else if (item instanceof ItemStack[] && ((ItemStack[]) item).length == 0) {
                    continue;
                } else if (item instanceof List && ((List) item).size() == 0) {
                    continue;
                }
                
                PositionedStack stack = new PositionedStack(item, 25 + RecipeHandlerRollingMachineShapeless.INPUTS[ingred][0] * 18, 6 + RecipeHandlerRollingMachineShapeless.INPUTS[ingred][1] * 18);
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
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeSubName() {
        return Utils.translate("shapeless");
    }
    
    private CachedRollingMachineShapelessRecipe getCachedRecipe(IRecipe irecipe, boolean genPerms) {
        CachedRollingMachineShapelessRecipe recipe = null;
        if (irecipe instanceof ShapelessRecipes) {
            recipe = new CachedRollingMachineShapelessRecipe(((ShapelessRecipes) irecipe).recipeItems, ((ShapelessRecipes) irecipe).getRecipeOutput());
        } else if (irecipe instanceof ShapelessOreRecipe) {
            recipe = this.getCachedOreRecipe((ShapelessOreRecipe) irecipe, genPerms);
        }
        return recipe;
    }
    
    private CachedRollingMachineShapelessRecipe getCachedOreRecipe(ShapelessOreRecipe recipe, boolean genPerms) {
        ArrayList<Object> items = recipe.getInput();
        for (Object item : items) {
            if (item instanceof List && ((List<?>) item).isEmpty()) {
                return null;
            }
        }
        return new CachedRollingMachineShapelessRecipe(items, recipe.getRecipeOutput(), genPerms);
    }
    
    @Override
    public void loadAllRecipes() {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            if (irecipe == null) {
                continue;
            }
            CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe, true);
            if (recipe != null) {
                this.arecipes.add(recipe);
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            if (irecipe == null) {
                continue;
            }
            if (Utils.areStacksSameTypeCraftingSafe(irecipe.getRecipeOutput(), result)) {
                CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe, true);
                if (recipe != null) {
                    this.arecipes.add(recipe);
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IRecipe irecipe : RailcraftCraftingManager.rollingMachine.getRecipeList()) {
            if (irecipe == null) {
                continue;
            }
            CachedRollingMachineShapelessRecipe recipe = this.getCachedRecipe(irecipe, false);
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
