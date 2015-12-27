package tonius.neiintegration.mods.forestry36;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.nei.PositionedStack;
import forestry.core.utils.ShapedRecipeCustom;

public class RecipeHandlerShapedCustom extends RecipeHandlerBase {
    
    public class CachedShapedCustomRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedShapedCustomRecipe(ShapedRecipeCustom recipe) {
            if (recipe.getIngredients() != null && recipe.getIngredients().length > 0) {
                this.setIngredients(recipe.getWidth(), recipe.getHeight(), recipe.getIngredients());
            }
            if (recipe.getRecipeOutput() != null) {
                this.output = new PositionedStack(recipe.getRecipeOutput(), 119, 24);
            }
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int index = y * width + x;
                    if (index >= items.length) {
                        continue;
                    }
                    
                    Object item = items[index];
                    
                    if (item == null) {
                        continue;
                    } else if (item instanceof ItemStack[] && ((ItemStack[]) item).length == 0) {
                        continue;
                    } else if (item instanceof List && ((List) item).size() == 0) {
                        continue;
                    }
                    
                    PositionedStack stack = new PositionedStack(item, 25 + x * 18, 6 + y * 18);
                    stack.setMaxSize(1);
                    this.inputs.add(stack);
                }
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerShapedCustom.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "crafting";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.forestry.shaped");
    }
    
    @Override
    public String getGuiTexture() {
        return "minecraft:textures/gui/container/crafting_table.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(84, 23, 24, 18);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCrafting.class;
    }
    
    @Override
    public String getOverlayIdentifier() {
        return this.getRecipeID();
    }
    
    @Override
    public void loadAllRecipes() {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipeCustom) {
                this.arecipes.add(new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipeCustom && Utils.areStacksSameTypeCraftingSafe(((ShapedRecipeCustom) recipe).getRecipeOutput(), result)) {
                this.arecipes.add(new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipeCustom) {
                CachedShapedCustomRecipe crecipe = new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe);
                if (crecipe.inputs != null && crecipe.contains(crecipe.inputs, ingredient)) {
                    crecipe.setIngredientPermutationNBT(crecipe.inputs, ingredient);
                    this.arecipes.add(crecipe);
                }
            }
        }
    }
    
}
