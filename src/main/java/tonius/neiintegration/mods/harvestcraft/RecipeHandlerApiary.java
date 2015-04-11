package tonius.neiintegration.mods.harvestcraft;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.PositionedStackAdv;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;

import com.pam.harvestcraft.GuiPamApiary;
import com.pam.harvestcraft.ItemRegistry;

public class RecipeHandlerApiary extends RecipeHandlerBase {
    
    private static final Map<Item, Float> BEE_PRODUCTS = new LinkedHashMap<Item, Float>();
    static {
        BEE_PRODUCTS.put(ItemRegistry.waxcombItem, 0.5F);
        BEE_PRODUCTS.put(ItemRegistry.honeycombItem, 0.45F);
        BEE_PRODUCTS.put(ItemRegistry.grubItem, 0.05F);
    }
    
    public class CachedApiaryRecipe extends CachedBaseRecipe {
        
        private PositionedStack input;
        private List<PositionedStack> outputs = new ArrayList<PositionedStack>();
        
        public CachedApiaryRecipe() {
            this.input = new PositionedStack(new ItemStack(ItemRegistry.queenbeeItem), 21, 24);
            
            int i = 0;
            for (Entry<Item, Float> e : BEE_PRODUCTS.entrySet()) {
                this.outputs.add(new PositionedStackAdv(new ItemStack(e.getKey()), 57 + 18 * i, 6).setChance(e.getValue()));
                i++;
            }
        }
        
        @Override
        public PositionedStack getIngredient() {
            return this.input;
        }
        
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "harvestcraft.apiary";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.apiary.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "harvestcraft:textures/gui/apiary.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(3, 44, 49, 11);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPamApiary.class;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(-2, -3, 3, 8, 170, 66);
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedApiaryRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (BEE_PRODUCTS.containsKey(result.getItem())) {
            this.arecipes.add(new CachedApiaryRecipe());
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        if (ingred.getItem() == ItemRegistry.queenbeeItem) {
            this.arecipes.add(new CachedApiaryRecipe());
        }
    }
    
}
