package tonius.neiintegration.buildcraft;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;

public class RecipeHandlerRefinery extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("buildcraft.factory.gui.GuiRefinery");
        API.setGuiOffset(guiClass, 0, 31);
    }
    
    public class CachedRefineryRecipe extends CachedBaseRecipe {
        
        public List<PositionedFluidTank> tanks = new ArrayList<PositionedFluidTank>();
        public int energy;
        public long time;
        
        public CachedRefineryRecipe(IFlexibleRecipeViewable recipe) {
            List<FluidStack> inputs = (List) recipe.getInputs();
            PositionedFluidTank tank = new PositionedFluidTank(inputs.get(0), inputs.get(0).amount, new Rectangle(33, 23, 16, 16));
            this.tanks.add(tank);
            if (inputs.size() > 1) {
                this.tanks.add(new PositionedFluidTank(inputs.get(1), inputs.get(1).amount, new Rectangle(121, 23, 16, 16)));
            }
            this.tanks.add(new PositionedFluidTank((FluidStack) recipe.getOutput(), ((FluidStack) recipe.getOutput()).amount, new Rectangle(77, 23, 16, 16)));
            
            this.energy = recipe.getEnergyCost();
            this.time = recipe.getCraftingTime();
        }
        
        @Override
        public List<PositionedFluidTank> getFluidTanks() {
            return this.tanks;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Refinery";
    }
    
    @Override
    public String getRecipeID() {
        return "buildcraft.refinery";
    }
    
    @Override
    public String getGuiTexture() {
        return "buildcraft:textures/gui/refinery_filter.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(52, 24, 23, 15), this.getRecipeID()));
        this.transferRects.add(new RecipeTransferRect(new Rectangle(96, 24, 23, 15), this.getRecipeID()));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 5, 31, 166, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        int energy = ((CachedRefineryRecipe) this.arecipes.get(recipe)).energy;
        long time = ((CachedRefineryRecipe) this.arecipes.get(recipe)).time;
        GuiDraw.drawStringC(energy + " RF per " + (time > 1 ? time + " ticks" : "tick"), 82, 45, 0x808080, false);
        GuiDraw.drawString("->", 58, 28, 0x404040, false);
        GuiDraw.drawString("<-", 102, 28, 0x404040, false);
    }
    
    @Override
    public void loadAllRecipes() {
        for (IFlexibleRecipe<FluidStack> recipe : BuildcraftRecipeRegistry.refinery.getRecipes()) {
            if (recipe instanceof IFlexibleRecipeViewable) {
                this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (IFlexibleRecipe<FluidStack> recipe : BuildcraftRecipeRegistry.refinery.getRecipes()) {
            if (recipe instanceof IFlexibleRecipeViewable && ((FluidStack) ((IFlexibleRecipeViewable) recipe).getOutput()).getFluid() == result.getFluid()) {
                this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        for (IFlexibleRecipe<FluidStack> recipe : BuildcraftRecipeRegistry.refinery.getRecipes()) {
            if (recipe instanceof IFlexibleRecipeViewable) {
                for (Object o : ((IFlexibleRecipeViewable) recipe).getInputs()) {
                    if (Utils.areFluidsSameType((FluidStack) o, ingredient)) {
                        this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
                    }
                }
            }
        }
    }
    
}
