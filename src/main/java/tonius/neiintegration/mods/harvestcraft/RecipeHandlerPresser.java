package tonius.neiintegration.mods.harvestcraft;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;

import com.pam.harvestcraft.GuiPamPresser;
import com.pam.harvestcraft.PresserRecipes;

public class RecipeHandlerPresser extends RecipeHandlerBase {
    
    public class CachedPresserRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack outputPrimary;
        public PositionedStack outputSecondary;
        
        public CachedPresserRecipe(ItemStack input, ItemStack outputPrimary, ItemStack outputSecondary) {
            this.input = new PositionedStack(input, 72, 9);
            this.outputPrimary = new PositionedStack(outputPrimary, 54, 40);
            if (outputSecondary != null) {
                this.outputSecondary = new PositionedStack(outputSecondary, 90, 40);
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerPresser.this.cycleticks / 40);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.outputPrimary;
        }
        
        @Override
        public PositionedStack getOtherStack() {
            return this.outputSecondary;
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "harvestcraft.presser";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.presser.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/harvestcraft/presser.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(59, 27, 42, 11);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPamPresser.class;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(59, 27, 160, 0, 42, 11, 40, 1);
    }
    
    @Override
    public void loadAllRecipes() {
        Map<ItemStack, ItemStack[]> recipes = PresserRecipes.pressing().getPressingList();
        for (Entry<ItemStack, ItemStack[]> recipe : recipes.entrySet()) {
            ItemStack[] outputs = recipe.getValue();
            if (outputs.length != 2) {
                continue;
            }
            this.arecipes.add(new CachedPresserRecipe(recipe.getKey(), outputs[0], outputs[1]));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map<ItemStack, ItemStack[]> recipes = PresserRecipes.pressing().getPressingList();
        for (Entry<ItemStack, ItemStack[]> recipe : recipes.entrySet()) {
            ItemStack[] outputs = recipe.getValue();
            if (outputs.length != 2) {
                continue;
            }
            if (Utils.areStacksSameTypeCraftingSafe(outputs[0], result) || Utils.areStacksSameTypeCraftingSafe(outputs[1], result)) {
                this.arecipes.add(new CachedPresserRecipe(recipe.getKey(), outputs[0], outputs[1]));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map<ItemStack, ItemStack[]> recipes = PresserRecipes.pressing().getPressingList();
        for (Entry<ItemStack, ItemStack[]> recipe : recipes.entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getKey(), ingredient)) {
                ItemStack[] outputs = recipe.getValue();
                if (outputs.length != 2) {
                    continue;
                }
                ingredient = ingredient.copy();
                ingredient.stackSize = 1;
                this.arecipes.add(new CachedPresserRecipe(ingredient, outputs[0], outputs[1]));
            }
        }
    }
    
}
