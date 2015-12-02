package tonius.neiintegration.mods.harvestcraft;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;

public abstract class RecipeHandlerHCBase extends RecipeHandlerBase {
    
    public class CachedHCRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        public PositionedStack fuels;
        
        public CachedHCRecipe(ItemStack input, ItemStack output, List<ItemStack> fuels) {
            this.input = new PositionedStack(input, 45, 4);
            this.output = new PositionedStack(output, 105, 22);
            this.fuels = new PositionedStack(fuels, 45, 40);
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
        public PositionedStack getOtherStack() {
            this.randomRenderPermutation(this.fuels, RecipeHandlerHCBase.this.cycleticks / 20);
            return this.fuels;
        }
        
    }
    
    protected abstract String getRecipeSubID();
    
    @Override
    public String getGuiTexture() {
        return String.format("neiintegration:textures/harvestcraft/%s.png", this.getRecipeSubID());
    }
    
    @Override
    public String getRecipeID() {
        return "harvestcraft." + this.getRecipeSubID();
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(68, 21, 24, 17);
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(68, 21, 160, 0, 24, 16, 80, 0);
    }
    
    public abstract Map<ItemStack, ItemStack> getRecipes();
    
    public List<ItemStack> getFuelItems() {
        return Collections.singletonList(this.getFuelItem());
    }
    
    public ItemStack getFuelItem() {
        return null;
    }
    
    @Override
    public void loadAllRecipes() {
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), this.getFuelItems()));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getValue(), result)) {
                this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), this.getFuelItems()));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        ingredient = ingredient.copy();
        ingredient.stackSize = 1;
        
        for (ItemStack stack : this.getFuelItems()) {
            if (Utils.areStacksSameTypeCraftingSafe(stack, ingredient)) {
                for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
                    this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), Collections.singletonList(ingredient)));
                }
                return;
            }
        }
        
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedHCRecipe(ingredient, recipe.getValue(), this.getFuelItems()));
            }
        }
    }
    
}
