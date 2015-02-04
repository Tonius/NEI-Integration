package tonius.neiintegration.harvestcraft;

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
        public PositionedStack output;
        
        public CachedPresserRecipe(ItemStack inputStack, ItemStack outputStack) {
            this.input = new PositionedStack(inputStack, 75, 12);
            this.output = new PositionedStack(outputStack, 57, 43);
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
    public String getRecipeID() {
        return "harvestcraft.presser";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.presser.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "harvestcraft:textures/gui/presser.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(71, 0, 25, 9);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPamPresser.class;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(-2, -5, 3, 6, 170, 68);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(71, -3, 176, 18, 24, 13, 80, 0);
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }
    
    @Override
    public void loadAllRecipes() {
        Map<ItemStack, ItemStack> recipes = PresserRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            this.arecipes.add(new CachedPresserRecipe(recipe.getKey(), recipe.getValue()));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Map<ItemStack, ItemStack> recipes = PresserRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getValue(), result)) {
                this.arecipes.add(new CachedPresserRecipe(recipe.getKey(), recipe.getValue()));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        Map<ItemStack, ItemStack> recipes = PresserRecipes.smelting().getSmeltingList();
        for (Entry<ItemStack, ItemStack> recipe : recipes.entrySet()) {
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getKey(), ingredient)) {
                this.arecipes.add(new CachedPresserRecipe(recipe.getKey(), recipe.getValue()));
            }
        }
    }
    
}
