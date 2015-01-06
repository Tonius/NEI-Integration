package tonius.neiintegration.forestry;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import forestry.api.fuels.FermenterFuel;
import forestry.api.fuels.FuelManager;
import forestry.api.recipes.IVariableFermentable;
import forestry.factory.gadgets.MachineFermenter;

public class RecipeHandlerFermenter extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    private static List<ItemStack> fuels = new ArrayList<ItemStack>();
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiFermenter");
        API.setGuiOffset(guiClass, 5, 15);
        for (FermenterFuel fuel : FuelManager.fermenterFuel.values()) {
            fuels.add(fuel.item);
        }
    }
    
    public class CachedFermenterRecipe extends CachedBaseRecipe {
        
        public List<PositionedFluidTank> tanks = new ArrayList<PositionedFluidTank>();
        public PositionedStack resource;
        public List<PositionedStack> inputItems = new ArrayList<PositionedStack>();
        
        public CachedFermenterRecipe(MachineFermenter.Recipe recipe, ItemStack fermentable, boolean genPerms) {
            if (recipe.liquid != null) {
                FluidStack input = recipe.liquid.copy();
                input.amount = recipe.fermentationValue;
                this.tanks.add(new PositionedFluidTank(input, 10000, new Rectangle(30, 4, 16, 58), RecipeHandlerFermenter.this.getGuiTexture(), new Point(176, 0)));
            }
            if (recipe.output != null) {
                FluidStack output = recipe.output.copy();
                if (fermentable.getItem() instanceof IVariableFermentable) {
                    output.amount = (int) (recipe.fermentationValue * recipe.modifier * ((IVariableFermentable) fermentable.getItem()).getFermentationModifier(fermentable));
                } else {
                    output.amount = (int) (recipe.fermentationValue * recipe.modifier);
                }
                this.tanks.add(new PositionedFluidTank(output, 10000, new Rectangle(120, 4, 16, 58), RecipeHandlerFermenter.this.getGuiTexture(), new Point(176, 0)));
            }
            
            this.inputItems.add(new PositionedStack(fermentable, 80, 8));
            this.inputItems.add(new PositionedStack(RecipeHandlerFermenter.fuels, 70, 42));
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedFermenterRecipe(MachineFermenter.Recipe recipe, ItemStack fermentable) {
            this(recipe, fermentable, false);
        }
        
        public CachedFermenterRecipe(MachineFermenter.Recipe recipe) {
            this(recipe, recipe.resource, true);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerFermenter.this.cycleticks / 40, this.inputItems);
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
        @Override
        public List<PositionedFluidTank> getFluidTanks() {
            return this.tanks;
        }
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputItems) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.fermenter";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.for.factory.3.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/fermenter.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(76, 27, 14, 12);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(25, 0, 30, 15, 116, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(69, 17, 176, 60, 4, 18, 40, 11);
        this.drawProgressBar(93, 31, 176, 78, 4, 18, 80, 11);
    }
    
    private List<CachedFermenterRecipe> getCachedRecipes(MachineFermenter.Recipe recipe, boolean generatePermutations) {
        if (recipe.resource != null && recipe.resource.getItem() instanceof IVariableFermentable) {
            List<CachedFermenterRecipe> crecipes = new ArrayList<CachedFermenterRecipe>();
            for (ItemStack stack : Utils.getItemVariations(recipe.resource)) {
                crecipes.add(new CachedFermenterRecipe(recipe, stack, generatePermutations));
            }
            return crecipes;
        }
        return Collections.singletonList(new CachedFermenterRecipe(recipe, recipe.resource, generatePermutations));
    }
    
    @Override
    public void loadAllRecipes() {
        for (MachineFermenter.Recipe recipe : MachineFermenter.RecipeManager.recipes) {
            this.arecipes.addAll(this.getCachedRecipes(recipe, true));
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (MachineFermenter.Recipe recipe : MachineFermenter.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.output, result)) {
                this.arecipes.addAll(this.getCachedRecipes(recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (MachineFermenter.Recipe recipe : MachineFermenter.RecipeManager.recipes) {
            if (recipe.resource != null) {
                for (ItemStack stack : Utils.getItemVariations(recipe.resource)) {
                    if (stack.hasTagCompound() && NEIServerUtils.areStacksSameType(stack, ingred) || !stack.hasTagCompound() && Utils.areStacksSameTypeCraftingSafe(stack, ingred)) {
                        CachedFermenterRecipe crecipe = new CachedFermenterRecipe(recipe, stack, true);
                        crecipe.setIngredientPermutationNBT(crecipe.inputItems, ingred);
                        this.arecipes.add(crecipe);
                    }
                }
            }
            for (ItemStack stack : fuels) {
                if (Utils.areStacksSameTypeCraftingSafe(stack, ingred)) {
                    for (CachedFermenterRecipe crecipe : this.getCachedRecipes(recipe, true)) {
                        crecipe.setIngredientPermutation(crecipe.inputItems, ingred);
                        this.arecipes.add(crecipe);
                    }
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingred) {
        for (MachineFermenter.Recipe recipe : MachineFermenter.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.liquid, ingred)) {
                this.arecipes.addAll(this.getCachedRecipes(recipe, true));
            }
        }
    }
    
}
