package tonius.neiintegration.mcforge;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerFluidRegistry extends RecipeHandlerBase {
    
    private static List<Fluid> fluids = new ArrayList<Fluid>();
    
    @Override
    public void prepare() {
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            fluids.add(fluid);
        }
        Collections.sort(fluids, new Comparator<Fluid>() {
            
            @Override
            public int compare(Fluid f1, Fluid f2) {
                return Integer.compare(f1.getID(), f2.getID());
            }
            
        });
    }
    
    public class CachedFluidRegistryRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank fluid;
        public PositionedStack block = null;
        public PositionedStack emptyContainer = null;
        public PositionedStack filledContainer = null;
        
        public CachedFluidRegistryRecipe(Fluid fluid) {
            this.fluid = new PositionedFluidTank(new FluidStack(fluid, 1000), 1000, new Rectangle(32, 5, 96, 32));
            this.fluid.showAmount = false;
            
            if (fluid.getBlock() != null) {
                this.block = new PositionedStack(new ItemStack(fluid.getBlock()), 32, 43);
            }
            
            this.setContainerItems(fluid);
        }
        
        private void setContainerItems(Fluid fluid) {
            List<ItemStack> emptyContainers = new ArrayList<ItemStack>();
            List<ItemStack> filledContainers = new ArrayList<ItemStack>();
            for (FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                if (data.fluid.getFluid() == fluid) {
                    emptyContainers.add(data.emptyContainer);
                    filledContainers.add(data.filledContainer);
                }
            }
            
            if (!emptyContainers.isEmpty() && !filledContainers.isEmpty()) {
                this.emptyContainer = new PositionedStack(emptyContainers, 71, 43);
                this.filledContainer = new PositionedStack(filledContainers, 112, 43);
            }
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.fluid;
        }
        
        @Override
        public PositionedStack getOtherStack() {
            return this.block;
        }
        
        @Override
        public PositionedStack getIngredient() {
            if (this.emptyContainer != null) {
                this.randomRenderPermutation(this.emptyContainer, RecipeHandlerFluidRegistry.this.cycleticks / 20);
            }
            return this.emptyContainer;
        }
        
        @Override
        public PositionedStack getResult() {
            if (this.filledContainer != null) {
                this.randomRenderPermutation(this.filledContainer, RecipeHandlerFluidRegistry.this.cycleticks / 20);
            }
            return this.filledContainer;
        }
        
        public void setPermutation(PositionedStack pStack, ItemStack stack) {
            if (pStack != null) {
                for (int i = 0; i < pStack.items.length; i++) {
                    if (NEIServerUtils.areStacksSameTypeCrafting(stack, pStack.items[i])) {
                        pStack.item = pStack.items[i];
                        pStack.item.setItemDamage(stack.getItemDamage());
                        pStack.items = new ItemStack[] { pStack.item };
                        pStack.setPermutationToRender(0);
                        break;
                    }
                }
            }
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.fluidRegistry");
    }
    
    @Override
    public String getRecipeID() {
        return "forge.fluids";
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/fluidRegistry.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(91, 42, 17, 17);
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        Rectangle tank = ((CachedFluidRegistryRecipe) crecipe).fluid.position;
        Fluid fluid = ((CachedFluidRegistryRecipe) crecipe).fluid.tank.getFluid().getFluid();
        if (tank.contains(relMouse)) {
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.id") + " " + EnumChatFormatting.GRAY + fluid.getName() + " (" + fluid.getID() + ")");
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.state") + " " + EnumChatFormatting.GRAY + (fluid.isGaseous() ? Utils.translate("handler.fluidRegistry.state.gaseous") : Utils.translate("handler.fluidRegistry.state.liquid")));
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.placeable") + " " + EnumChatFormatting.GRAY + (fluid.canBePlacedInWorld() ? Utils.translate("yes") : Utils.translate("no")));
            currenttip.add("");
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.temperature") + " " + EnumChatFormatting.GRAY + fluid.getTemperature());
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.luminosity") + " " + EnumChatFormatting.GRAY + fluid.getLuminosity());
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.density") + " " + EnumChatFormatting.GRAY + fluid.getDensity());
            currenttip.add(EnumChatFormatting.GOLD + Utils.translate("handler.fluidRegistry.viscosity") + " " + EnumChatFormatting.GRAY + fluid.getViscosity());
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        for (Fluid fluid : fluids) {
            this.arecipes.add(new CachedFluidRegistryRecipe(fluid));
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (Block.getBlockFromItem(result.getItem()) instanceof IFluidBlock) {
            super.loadCraftingRecipes(result);
        }
        for (Fluid fluid : fluids) {
            CachedFluidRegistryRecipe crecipe = new CachedFluidRegistryRecipe(fluid);
            if (crecipe.filledContainer != null && crecipe.filledContainer.contains(result)) {
                crecipe.setPermutation(crecipe.filledContainer, result);
                for (FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                    if (NEIServerUtils.areStacksSameTypeCrafting(data.filledContainer, result)) {
                        crecipe.setPermutation(crecipe.emptyContainer, data.emptyContainer);
                    }
                }
                this.arecipes.add(crecipe);
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            if (fluid == result.getFluid()) {
                this.arecipes.add(new CachedFluidRegistryRecipe(fluid));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        super.loadUsageRecipes(ingredient);
        for (Fluid fluid : fluids) {
            CachedFluidRegistryRecipe crecipe = new CachedFluidRegistryRecipe(fluid);
            if (crecipe.emptyContainer != null && crecipe.emptyContainer.contains(ingredient)) {
                crecipe.setPermutation(crecipe.emptyContainer, ingredient);
                for (FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                    if (NEIServerUtils.areStacksSameTypeCrafting(data.emptyContainer, ingredient)) {
                        crecipe.setPermutation(crecipe.filledContainer, data.filledContainer);
                    }
                }
                this.arecipes.add(crecipe);
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        for (Fluid fluid : fluids) {
            if (fluid == ingredient.getFluid()) {
                this.arecipes.add(new CachedFluidRegistryRecipe(fluid));
            }
        }
    }
    
}
