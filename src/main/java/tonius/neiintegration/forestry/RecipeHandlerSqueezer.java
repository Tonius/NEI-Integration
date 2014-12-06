package tonius.neiintegration.forestry;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.PositionedStackAdv;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import forestry.factory.gadgets.MachineSqueezer;

public class RecipeHandlerSqueezer extends RecipeHandlerBase {
    
    private static final int[][] INPUTS = new int[][] { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiSqueezer");
    }
    
    public class CachedSqueezerRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedFluidTank tank;
        public PositionedStackAdv remnants = null;
        
        public CachedSqueezerRecipe(MachineSqueezer.Recipe recipe, boolean genPerms) {
            if (recipe.resources != null) {
                this.setIngredients(recipe.resources);
            }
            if (recipe.liquid != null) {
                this.tank = new PositionedFluidTank(recipe.liquid, 10000, new Rectangle(80, 4, 16, 58), RecipeHandlerSqueezer.this.getGuiTexture(), new Point(176, 0));
            }
            if (recipe.remnants != null) {
                this.remnants = new PositionedStackAdv(recipe.remnants, 118, 8).setChance(recipe.chance / 100F);
            }
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedSqueezerRecipe(MachineSqueezer.Recipe recipe) {
            this(recipe, false);
        }
        
        public void setIngredients(ItemStack[] inputs) {
            int i = 0;
            for (ItemStack stack : inputs) {
                if (i >= INPUTS.length) {
                    return;
                }
                this.inputs.add(new PositionedStack(stack, 14 + INPUTS[i][0] * 18, 7 + INPUTS[i][1] * 18, false));
                i++;
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerSqueezer.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.tank;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.remnants;
        }
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.squeezer";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.for.factory.5.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/squeezer.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(98, 9, 15, 15);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(70, 8, 176, 60, 43, 18, 80, 0);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void loadAllRecipes() {
        for (MachineSqueezer.Recipe recipe : MachineSqueezer.RecipeManager.recipes) {
            this.arecipes.add(new CachedSqueezerRecipe(recipe, true));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        super.loadCraftingRecipes(result);
        for (MachineSqueezer.Recipe recipe : MachineSqueezer.RecipeManager.recipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.remnants, result)) {
                this.arecipes.add(new CachedSqueezerRecipe(recipe, true));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (MachineSqueezer.Recipe recipe : MachineSqueezer.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.liquid, result)) {
                this.arecipes.add(new CachedSqueezerRecipe(recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        for (MachineSqueezer.Recipe recipe : MachineSqueezer.RecipeManager.recipes) {
            CachedSqueezerRecipe crecipe = new CachedSqueezerRecipe(recipe);
            if (crecipe.inputs != null && crecipe.contains(crecipe.inputs, ingred)) {
                crecipe.generatePermutations();
                crecipe.setIngredientPermutation(crecipe.inputs, ingred);
                this.arecipes.add(crecipe);
            }
        }
    }
    
}
