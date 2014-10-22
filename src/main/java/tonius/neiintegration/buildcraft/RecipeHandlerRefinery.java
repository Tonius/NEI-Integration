package tonius.neiintegration.buildcraft;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.Hacks;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;

public class RecipeHandlerRefinery extends RecipeHandlerBase {
    
    private static final Rectangle INPUT1 = new Rectangle(33, 23, 16, 16);
    private static final Rectangle INPUT2 = new Rectangle(121, 23, 16, 16);
    private static final Rectangle OUTPUT = new Rectangle(77, 23, 16, 16);
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Hacks.getClass("buildcraft.factory.gui.GuiRefinery");
        API.setGuiOffset(guiClass, 0, 31);
    }
    
    public class CachedRefineryRecipe extends CachedBaseRecipe {
        
        public List<PositionedFluidTank> tanks = new ArrayList<PositionedFluidTank>();
        public int energy;
        public long time;
        
        public CachedRefineryRecipe(IFlexibleRecipeViewable recipe) {
            List<FluidStack> inputs = (List) recipe.getInputs();
            PositionedFluidTank tank = new PositionedFluidTank(INPUT1, inputs.get(0).amount, inputs.get(0));
            this.tanks.add(tank);
            if (inputs.size() > 1) {
                this.tanks.add(new PositionedFluidTank(INPUT2, inputs.get(1).amount, inputs.get(1)));
            }
            this.tanks.add(new PositionedFluidTank(OUTPUT, ((FluidStack) recipe.getOutput()).amount, (FluidStack) recipe.getOutput()));
            
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
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 31, 166, 65);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        int energy = ((CachedRefineryRecipe) this.arecipes.get(recipe)).energy;
        long time = ((CachedRefineryRecipe) this.arecipes.get(recipe)).time;
        GuiDraw.drawStringC(energy + " RF per " + (time > 1 ? time + " ticks" : "tick"), 82, 45, 0x808080, false);
        GuiDraw.drawString("->", 58, 28, 0x404040, false);
        GuiDraw.drawString("<-", 102, 28, 0x404040, false);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (IFlexibleRecipe<FluidStack> recipe : BuildcraftRecipeRegistry.refinery.getRecipes()) {
                if (recipe instanceof IFlexibleRecipeViewable) {
                    this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
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
                    if (((FluidStack) o).getFluid() == ingredient.getFluid()) {
                        this.arecipes.add(new CachedRefineryRecipe((IFlexibleRecipeViewable) recipe));
                    }
                }
            }
        }
    }
    
}
