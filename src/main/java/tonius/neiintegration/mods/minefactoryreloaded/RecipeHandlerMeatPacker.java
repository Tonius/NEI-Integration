package tonius.neiintegration.mods.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityMeatPacker;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerMeatPacker extends RecipeHandlerBase {
    
    private static Item meatIngot;
    private static Item meatNugget;
    private static int fluidPerOperation;
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        meatIngot = GameRegistry.findItem("MineFactoryReloaded", "meat.ingot.raw");
        meatNugget = GameRegistry.findItem("MineFactoryReloaded", "meat.nugget.raw");
        if (meatIngot == null || meatNugget == null) {
            meatIngot = GameRegistry.findItem("MineFactoryReloaded", "item.mfr.meat.ingot.raw");
            meatNugget = GameRegistry.findItem("MineFactoryReloaded", "item.mfr.meat.nugget.raw");
        }
        
        TileEntityMeatPacker dummy = new TileEntityMeatPacker();
        fluidPerOperation = dummy.getWorkMax() * 2;
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedMeatPackerRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank fluidInput;
        public PositionedStack output;
        
        public CachedMeatPackerRecipe(FluidStack input, ItemStack output) {
            this.fluidInput = new PositionedFluidTank(input, 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerMeatPacker.this.getGuiTexture(), new Point(176, 0));
            this.output = new PositionedStack(output, 48, 24);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.fluidInput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.meatpacker.name", false);
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.meatpacker";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "meatpacker.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(76, 25, 22, 15);
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(76, 25, 0, 15, 22, 15);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 20, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(129, 2, 8, 60).contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadAllRecipes() {
        this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("meat", fluidPerOperation), new ItemStack(meatIngot)));
        this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("pinkslime", fluidPerOperation), new ItemStack(meatNugget)));
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == meatIngot) {
            this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("meat", fluidPerOperation), new ItemStack(meatIngot)));
        } else if (result.getItem() == meatNugget) {
            this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("pinkslime", fluidPerOperation), new ItemStack(meatNugget)));
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        if (ingredient.getFluid().getName().equals("meat")) {
            this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("meat", fluidPerOperation), new ItemStack(meatIngot)));
        } else if (ingredient.getFluid().getName().equals("pinkslime")) {
            this.arecipes.add(new CachedMeatPackerRecipe(FluidRegistry.getFluidStack("pinkslime", fluidPerOperation), new ItemStack(meatNugget)));
        }
    }
    
}
