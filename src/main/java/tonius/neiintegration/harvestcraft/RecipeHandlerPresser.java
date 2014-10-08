package tonius.neiintegration.harvestcraft;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.Hacks;
import tonius.neiintegration.Hacks.MethodInvoker;
import tonius.neiintegration.NEIIntegration;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerPresser extends RecipeHandlerBase {
    
    private static Map<ItemStack, ItemStack> recipes = new HashMap<ItemStack, ItemStack>();
    
    @Override
    public void prepare() {
        Class clazz = Hacks.getClass("com.pam.harvestcraft.PresserRecipes");
        if (clazz != null) {
            MethodInvoker smelting = Hacks.getMethodInvoker(clazz, "smelting", null, new Class[0]);
            if (smelting != null) {
                Object instance = smelting.invoke(new Object[0]);
                if (instance != null) {
                    MethodInvoker getSmeltingList = Hacks.getMethodInvoker(clazz, "getSmeltingList", instance, new Class[0]);
                    if (getSmeltingList != null) {
                        recipes = (Map<ItemStack, ItemStack>) getSmeltingList.invoke(new Object[0]);
                    }
                }
            }
        }
    }
    
    public class CachedPresserRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        
        public CachedPresserRecipe(ItemStack input, ItemStack output) {
            this.input = new PositionedStack(input, 44, 22);
            this.output = new PositionedStack(output, 105, 22);
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
    public String getRecipeName() {
        return "Presser";
    }
    
    @Override
    public String getRecipeID() {
        return "harvestcraft.presser";
    }
    
    @Override
    public String getGuiTexture() {
        return NEIIntegration.RESOURCE_PREFIX + "textures/basicProcessing.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(68, 21, 24, 17), this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(68, 21, 160, 0, 24, 16, 48, 0);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
                this.arecipes.add(new CachedPresserRecipe(entry.getKey(), entry.getValue()));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(entry.getValue(), result)) {
                this.arecipes.add(new CachedPresserRecipe(entry.getKey(), entry.getValue()));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (Entry<ItemStack, ItemStack> entry : recipes.entrySet()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(entry.getKey(), ingred)) {
                this.arecipes.add(new CachedPresserRecipe(entry.getKey(), entry.getValue()));
            }
        }
    }
    
}
