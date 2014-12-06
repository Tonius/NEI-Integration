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
            if (recipe.getInputs() instanceof List) {
                List<FluidStack> inputs = (List) recipe.getInputs();
                PositionedFluidTank tank = new PositionedFluidTank(inputs.get(0), inputs.get(0).amount, new Rectangle(33, 23, 16, 16));
                this.tanks.add(tank);
                if (inputs.size() > 1) {
                    this.tanks.add(new PositionedFluidTank(inputs.get(1), inputs.get(1).amount, new Rectangle(121, 23, 16, 16)));
                }
            }
            if (recipe.getOutput() instanceof FluidStack) {
                this.tanks.add(new PositionedFluidTank((FluidStack) recipe.getOutput(), ((FluidStack) recipe.getOutput()).amount, new Rectangle(77, 23, 16, 16)));
            }
            
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
        return Utils.translate("tile.refineryBlock.name", false);
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
        this.addTransferRect(52, 24, 23, 15);
        this.addTransferRect(96, 24, 23, 15);
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
        int energyPerTick = ((CachedRefineryRecipe) this.arecipes.get(recipe)).energy;
        long time = ((CachedRefineryRecipe) this.arecipes.get(recipe)).time;
        GuiDraw.drawStringC(String.format(Utils.translate("handler.refinery.energyRate"), energyPerTick * time, energyPerTick), 82, 45, 0x808080, false);
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
            if (recipe instanceof IFlexibleRecipeViewable && ((IFlexibleRecipeViewable) recipe).getOutput() instanceof FluidStack) {
                FluidStack output = (FluidStack) ((IFlexibleRecipeViewable) recipe).getOutput();
                if (Utils.areFluidsSameType(output, result)) {
                    this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        for (IFlexibleRecipe<FluidStack> recipe : BuildcraftRecipeRegistry.refinery.getRecipes()) {
            if (recipe instanceof IFlexibleRecipeViewable) {
                if (((IFlexibleRecipeViewable) recipe).getInputs() instanceof List) {
                    for (Object o : ((IFlexibleRecipeViewable) recipe).getInputs()) {
                        if (o instanceof FluidStack) {
                            if (Utils.areFluidsSameType((FluidStack) o, ingredient)) {
                                this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
                            }
                        }
                    }
                }
            }
        }
    }
}
