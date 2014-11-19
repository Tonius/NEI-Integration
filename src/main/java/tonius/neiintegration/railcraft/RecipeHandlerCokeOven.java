package tonius.neiintegration.railcraft;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.List;

import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;

public class RecipeHandlerCokeOven extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("mods.railcraft.client.gui.GuiCokeOven");
        API.setGuiOffset(guiClass, -6, 11);
    }
    
    public class CachedCokeOvenRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> input;
        public PositionedStack output;
        public PositionedFluidTank fluidOutput;
        public int cookTime;
        
        public CachedCokeOvenRecipe(ICokeOvenRecipe recipe) {
            this.input = Collections.singletonList(new PositionedStack(recipe.getInput(), 21, 32));
            this.output = new PositionedStack(recipe.getOutput(), 67, 32);
            this.fluidOutput = new PositionedFluidTank(recipe.getFluidOutput(), 64000, new Rectangle(95, 13, 48, 47), RecipeHandlerCokeOven.this.getGuiTexture(), new Point(176, 0));
            this.cookTime = recipe.getCookTime();
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerCokeOven.this.cycleticks / 20, this.input);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.fluidOutput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("railcraft.gui.coke.oven", false);
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
        this.transferRects.add(new RecipeTransferRect(new Rectangle(39, 32, 22, 16), this.getRecipeID()));
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
    public void drawExtras(int recipe) {
        this.drawProgressBar(40, 32, 177, 61, 21, 16, 100, 0);
        this.drawProgressBar(21, 15, 176, 47, 14, 14, 100, 11);
        GuiDraw.drawStringC(((CachedCokeOvenRecipe) this.arecipes.get(recipe)).cookTime + " " + Utils.translate("ticks"), 64, 12, 0x808080, false);
    }
    
    @Override
    public void loadAllRecipes() {
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            this.arecipes.add(new CachedCokeOvenRecipe(recipe));
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
                CachedCokeOvenRecipe crecipe = new CachedCokeOvenRecipe(recipe);
                crecipe.setIngredientPermutation(crecipe.input, ingred);
                this.arecipes.add(crecipe);
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (Utils.areFluidsSameType(recipe.getFluidOutput(), result)) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }
    
}
