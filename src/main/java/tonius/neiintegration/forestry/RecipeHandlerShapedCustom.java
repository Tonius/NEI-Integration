package tonius.neiintegration.forestry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import forestry.core.utils.ShapedRecipeCustom;

public class RecipeHandlerShapedCustom extends RecipeHandlerBase {
    
    public class CachedShapedCustomRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedShapedCustomRecipe(ShapedRecipeCustom recipe, boolean genPerms) {
            this.setIngredients(recipe.getWidth(), recipe.getHeight(), recipe.getIngredients());
            this.output = new PositionedStack(recipe.getRecipeOutput(), 119, 24);
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedShapedCustomRecipe(ShapedRecipeCustom recipe) {
            this(recipe, false);
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null) {
                        continue;
                    }
                    PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
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
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "crafting";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.forestryShaped");
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
                this.arecipes.add(new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe, true));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipeCustom && NEIServerUtils.areStacksSameTypeCrafting(((ShapedRecipeCustom) recipe).getRecipeOutput(), result)) {
                this.arecipes.add(new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof ShapedRecipeCustom) {
                CachedShapedCustomRecipe crecipe = new CachedShapedCustomRecipe((ShapedRecipeCustom) recipe);
                if (crecipe.contains(crecipe.inputs, ingredient)) {
                    crecipe.generatePermutations();
                    crecipe.setIngredientPermutationNBT(crecipe.inputs, ingredient);
                    this.arecipes.add(crecipe);
                }
            }
        }
    }
    
}
