package tonius.neiintegration.forestry;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.nei.PositionedStack;
import forestry.core.utils.ShapedRecipeCustom;
import forestry.factory.gadgets.MachineFabricator;

public class RecipeHandlerFabricator extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("forestry.factory.gui.GuiFabricator");
    }
    
    public class CachedFabricatorRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> smeltingInput = new ArrayList<PositionedStack>();
        public PositionedFluidTank tank;
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedFabricatorRecipe(MachineFabricator.Recipe recipe, boolean genPerms) {
            if (recipe.getLiquid() != null) {
                this.tank = new PositionedFluidTank(recipe.getLiquid(), 2000, new Rectangle(21, 37, 16, 16));
                List<ItemStack> smeltingInput = getSmeltingInputs().get(recipe.getLiquid().getFluid());
                if (smeltingInput != null && !smeltingInput.isEmpty()) {
                    this.smeltingInput.add(new PositionedStack(smeltingInput, 21, 10));
                }
            }
            
            ShapedRecipeCustom irecipe = (ShapedRecipeCustom) recipe.asIRecipe();
            if (irecipe != null) {
                if (irecipe.getIngredients() != null) {
                    this.setIngredients(irecipe.getWidth(), irecipe.getHeight(), irecipe.getIngredients());
                }
                if (recipe.getPlan() != null) {
                    this.inputs.add(new PositionedStack(recipe.getPlan(), 134, 6));
                }
                
                if (irecipe.getRecipeOutput() != null) {
                    this.output = new PositionedStack(irecipe.getRecipeOutput(), 134, 42);
                }
            }
            
            if (genPerms) {
                this.generatePermutations();
            }
        }
        
        public CachedFabricatorRecipe(MachineFabricator.Recipe recipe) {
            this(recipe, false);
        }
        
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null) {
                        continue;
                    }
                    PositionedStack stack = new PositionedStack(items[y * width + x], 62 + x * 18, 6 + y * 18, false);
                    stack.setMaxSize(1);
                    this.inputs.add(stack);
                }
            }
        }
        
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.getCycledIngredients(RecipeHandlerFabricator.this.cycleticks / 40, this.smeltingInput);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerFabricator.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.tank;
        }
        
        public void generatePermutations() {
            for (PositionedStack p : this.inputs) {
                p.generatePermutations();
            }
        }
        
    }
    
    @Override
    public String getRecipeID() {
        return "forestry.fabricator";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.for.factory2.0.name", false);
    }
    
    @Override
    public String getGuiTexture() {
        return "forestry:textures/gui/fabricator.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(117, 44, 14, 13);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void loadAllRecipes() {
        for (MachineFabricator.Recipe recipe : MachineFabricator.RecipeManager.recipes) {
            this.arecipes.add(new CachedFabricatorRecipe(recipe, true));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MachineFabricator.Recipe recipe : MachineFabricator.RecipeManager.recipes) {
            if (recipe.asIRecipe() != null && Utils.areStacksSameTypeCraftingSafe(recipe.asIRecipe().getRecipeOutput(), result)) {
                this.arecipes.add(new CachedFabricatorRecipe(recipe, true));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (MachineFabricator.Recipe recipe : MachineFabricator.RecipeManager.recipes) {
            CachedFabricatorRecipe crecipe = new CachedFabricatorRecipe(recipe);
            if (crecipe.inputs != null && crecipe.contains(crecipe.inputs, ingred) || crecipe.smeltingInput != null && crecipe.contains(crecipe.smeltingInput, ingred)) {
                crecipe.generatePermutations();
                crecipe.setIngredientPermutation(crecipe.inputs, ingred);
                crecipe.setIngredientPermutation(crecipe.smeltingInput, ingred);
                this.arecipes.add(crecipe);
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        for (MachineFabricator.Recipe recipe : MachineFabricator.RecipeManager.recipes) {
            if (Utils.areFluidsSameType(recipe.getLiquid(), ingredient)) {
                this.arecipes.add(new CachedFabricatorRecipe(recipe, true));
            }
        }
    }
    
    private static Map<Fluid, List<ItemStack>> getSmeltingInputs() {
        Map<Fluid, List<ItemStack>> smeltingInputs = new HashMap<Fluid, List<ItemStack>>();
        for (MachineFabricator.Smelting smelting : MachineFabricator.RecipeManager.smeltings) {
            Fluid fluid = smelting.getProduct().getFluid();
            if (!smeltingInputs.containsKey(fluid)) {
                smeltingInputs.put(fluid, new ArrayList<ItemStack>());
            }
            smeltingInputs.get(fluid).add(smelting.getResource());
        }
        return smeltingInputs;
    }
    
}
