package tonius.neiintegration.mods.harvestcraft;

import java.awt.Point;
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
        public PositionedStack fuel;
        
        public CachedHCRecipe(ItemStack input, ItemStack output, ItemStack fuel) {
            Point inputStackPos = RecipeHandlerHCBase.this.getInputStackPos();
            Point outputStackPos = RecipeHandlerHCBase.this.getOutputStackPos();
            Point fuelStackPos = RecipeHandlerHCBase.this.getFuelStackPos();
            
            this.input = new PositionedStack(input, inputStackPos.x, inputStackPos.y);
            this.output = new PositionedStack(output, outputStackPos.x, outputStackPos.y);
            if (fuel != null) {
                this.fuel = new PositionedStack(fuel, fuelStackPos.x, fuelStackPos.y);
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerHCBase.this.cycleticks / 40);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedStack getOtherStack() {
            return this.fuel;
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
    
    public Point getInputStackPos() {
        return new Point(45, 4);
    }
    
    public Point getOutputStackPos() {
        return new Point(105, 22);
    }
    
    public Point getFuelStackPos() {
        return new Point(45, 40);
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
    
    public ItemStack getFuelItem() {
        return null;
    }
    
    @Override
    public void loadAllRecipes() {
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), this.getFuelItem()));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getValue(), result)) {
                this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), this.getFuelItem()));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        ingredient = ingredient.copy();
        ingredient.stackSize = 1;
        
        if (Utils.areStacksSameTypeCraftingSafe(this.getFuelItem(), ingredient)) {
            for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
                this.arecipes.add(new CachedHCRecipe(recipe.getKey(), recipe.getValue(), ingredient));
            }
            return;
        }
        
        for (Entry<ItemStack, ItemStack> recipe : this.getRecipes().entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedHCRecipe(ingredient, recipe.getValue(), this.getFuelItem()));
            }
        }
    }
    
}
