package tonius.neiintegration.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import forestry.core.interfaces.IDescriptiveRecipe;

public class RecipeHandlerShapedCustom extends RecipeHandlerBase {
    
    public class CachedShapedCustomRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedShapedCustomRecipe(int width, int height, Object[] items, ItemStack out) {
            this.setIngredients(width, height, items);
            this.output = new PositionedStack(out, 119, 24);
        }
        
        public CachedShapedCustomRecipe(IDescriptiveRecipe recipe) {
            this(recipe, false);
        }
        
        public CachedShapedCustomRecipe(IDescriptiveRecipe recipe, boolean genPerms) {
            this(recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getRecipeOutput());
            if (genPerms) {
                this.generatePermutations();
            }
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
        return "Forestry Shaped";
    }
    
    @Override
    public String getGuiTexture() {
        return "minecraft:textures/gui/container/crafting_table.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "crafting"));
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
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe instanceof IDescriptiveRecipe) {
                    this.arecipes.add(new CachedShapedCustomRecipe((IDescriptiveRecipe) recipe, true));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof IDescriptiveRecipe && NEIServerUtils.areStacksSameTypeCrafting(((IDescriptiveRecipe) recipe).getRecipeOutput(), result)) {
                this.arecipes.add(new CachedShapedCustomRecipe((IDescriptiveRecipe) recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
            if (recipe instanceof IDescriptiveRecipe) {
                CachedShapedCustomRecipe crecipe = new CachedShapedCustomRecipe((IDescriptiveRecipe) recipe);
                if (crecipe.contains(crecipe.inputs, ingredient)) {
                    crecipe.generatePermutations();
                    crecipe.setIngredientPermutationNBT(crecipe.inputs, ingredient);
                    this.arecipes.add(crecipe);
                }
            }
        }
    }
    
}
