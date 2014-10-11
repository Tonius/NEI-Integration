package tonius.neiintegration.railcraft;

import java.awt.Rectangle;

import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.Hacks;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;

public class RecipeHandlerCokeOven extends RecipeHandlerBase {
    
    private static final Rectangle CREOSOTE = new Rectangle(95, 13, 48, 47);
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Hacks.getClass("mods.railcraft.client.gui.GuiCokeOven");
        API.setGuiOffset(guiClass, -6, 11);
    }
    
    public class CachedCokeOvenRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        public FluidTankElement fluidOutput;
        public int cookTime;
        
        public CachedCokeOvenRecipe(ICokeOvenRecipe recipe) {
            this.input = new PositionedStack(recipe.getInput(), 21, 32);
            this.output = new PositionedStack(recipe.getOutput(), 67, 32);
            this.fluidOutput = new FluidTankElement(CREOSOTE, 64000, recipe.getFluidOutput());
            this.cookTime = recipe.getCookTime();
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerCokeOven.this.cycleticks / 20);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public FluidTankElement getFluidTank() {
            return this.fluidOutput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Coke Oven";
    }
    
    @Override
    public String getRecipeID() {
        return "railcraft.cokeoven";
    }
    
    @Override
    public String getGuiTexture() {
        return "railcraft:textures/gui/gui_coke_oven.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(39, 32, 22, 16), this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(10, 0, 5, 11, 137, 64);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(95, 13, 176, 0, 48, 47);
        this.drawProgressBar(40, 32, 177, 61, 21, 16, 100, 0);
        this.drawProgressBar(21, 15, 176, 47, 14, 14, 100, 11);
        GuiDraw.drawStringC(((CachedCokeOvenRecipe) this.arecipes.get(recipe)).cookTime + " ticks", 64, 12, 0x808080, false);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        super.loadCraftingRecipes(result);
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (NEIServerUtils.areStacksSameType(result, recipe.getOutput())) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getInput(), ingred)) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (RecipeHandlerBase.areFluidsEqual(recipe.getFluidOutput(), result)) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }
    
}
