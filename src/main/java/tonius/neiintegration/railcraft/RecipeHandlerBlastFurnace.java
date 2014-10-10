package tonius.neiintegration.railcraft;

import java.awt.Rectangle;
import java.util.List;

import mods.railcraft.api.crafting.IBlastFurnaceRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.Hacks;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerBlastFurnace extends RecipeHandlerBase {
    
    private static List<ItemStack> fuels;
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Hacks.getClass("mods.railcraft.client.gui.GuiBlastFurnace");
        fuels = RailcraftCraftingManager.blastFurnace.getFuels();
    }
    
    public class CachedBlastFurnaceRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        public PositionedStack fuel;
        public int cookTime;
        
        public CachedBlastFurnaceRecipe(IBlastFurnaceRecipe recipe) {
            this.input = new PositionedStack(recipe.getInput(), 51, 6);
            this.output = new PositionedStack(recipe.getOutput(), 111, 24);
            this.cookTime = recipe.getCookTime();
        }
        
        public CachedBlastFurnaceRecipe(IBlastFurnaceRecipe recipe, ItemStack fuel) {
            this(recipe);
            this.fuel = new PositionedStack(fuel, 51, 42);
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerBlastFurnace.this.cycleticks / 20);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedStack getOtherStack() {
            if (this.fuel != null) {
                return this.fuel;
            } else if (fuels.size() > 0) {
                return new PositionedStack(fuels.get(RecipeHandlerBlastFurnace.this.cycleticks / 48 % fuels.size()), 51, 42);
            }
            return null;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Blast Furnace";
    }
    
    @Override
    public String getRecipeID() {
        return "railcraft.blastfurnace";
    }
    
    @Override
    public String getGuiTexture() {
        return "textures/gui/container/furnace.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), this.getRecipeID()));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawExtras(int recipe) {
        GuiDraw.drawStringC(((CachedBlastFurnaceRecipe) this.arecipes.get(recipe)).cookTime + " ticks", 120, 6, 0x808080, false);
        this.changeToGuiTexture();
        this.drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (IBlastFurnaceRecipe recipe : RailcraftCraftingManager.blastFurnace.getRecipes()) {
                this.arecipes.add(new CachedBlastFurnaceRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (IBlastFurnaceRecipe recipe : RailcraftCraftingManager.blastFurnace.getRecipes()) {
            if (NEIServerUtils.areStacksSameType(result, recipe.getOutput())) {
                this.arecipes.add(new CachedBlastFurnaceRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (IBlastFurnaceRecipe recipe : RailcraftCraftingManager.blastFurnace.getRecipes()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getInput(), ingredient)) {
                this.arecipes.add(new CachedBlastFurnaceRecipe(recipe));
            }
        }
        for (ItemStack fuel : fuels) {
            if (NEIServerUtils.areStacksSameTypeCrafting(fuel, ingredient)) {
                for (IBlastFurnaceRecipe recipe : RailcraftCraftingManager.blastFurnace.getRecipes()) {
                    this.arecipes.add(new CachedBlastFurnaceRecipe(recipe, ingredient));
                }
            }
        }
    }
    
}
