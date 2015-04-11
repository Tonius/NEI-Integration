package tonius.neiintegration.mods.electricalage;

import mods.eln.misc.Recipe;
import mods.eln.misc.RecipesList;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;

public abstract class RecipeHandlerElnBase extends RecipeHandlerBase {
    
    public class CachedElnRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        
        public CachedElnRecipe(Recipe recipe) {
            if (recipe.input != null) {
                this.input = new PositionedStack(recipe.input, 44, 22);
            }
            if (recipe.output != null) {
                this.output = new PositionedStack(recipe.output, 105, 22);
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/basicProcessing.png";
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
    
    public abstract RecipesList getRecipes();
    
    @Override
    public void loadAllRecipes() {
        for (Recipe recipe : this.getRecipes().getRecipes()) {
            this.arecipes.add(new CachedElnRecipe(recipe));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Recipe recipe : this.getRecipes().getRecipes()) {
            if (recipe.output != null && recipe.output.length > 0 && Utils.areStacksSameTypeCraftingSafe(recipe.output[0], result)) {
                this.arecipes.add(new CachedElnRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (Recipe recipe : this.getRecipes().getRecipes()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.input, ingred)) {
                this.arecipes.add(new CachedElnRecipe(recipe));
            }
        }
    }
    
}
