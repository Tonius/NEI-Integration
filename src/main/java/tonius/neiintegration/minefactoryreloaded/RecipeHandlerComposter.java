package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityComposter;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerComposter extends RecipeHandlerBase {
    
    private static Item fertilizer;
    private static int sewagePerOperation;
    private static int energyPerOperation;
    
    @Override
    public void prepare() {
        fertilizer = GameRegistry.findItem("MineFactoryReloaded", "item.mfr.fertilizer");
        
        TileEntityComposter dummy = new TileEntityComposter();
        sewagePerOperation = dummy.getWorkMax() * 20;
        energyPerOperation = dummy.getActivationEnergy() * dummy.getWorkMax();
        dummy = null;
    }
    
    public class CachedComposterRecipe extends CachedBaseRecipe {
        
        public PositionedFluidTank sewageInput;
        public PositionedStack output;
        
        public CachedComposterRecipe() {
            this.sewageInput = new PositionedFluidTank(FluidRegistry.getFluidStack("sewage", sewagePerOperation), 4000, new Rectangle(111, 2, 16, 60), RecipeHandlerComposter.this.getGuiTexture(), new Point(176, 0));
            this.output = new PositionedStack(new ItemStack(fertilizer), 48, 24);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.sewageInput;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Composter";
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.composter";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "composter.png";
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
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 40, 3);
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
        this.arecipes.add(new CachedComposterRecipe());
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == fertilizer) {
            this.loadAllRecipes();
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        if (ingredient.getFluid().getName().equals("sewage")) {
            this.loadAllRecipes();
        }
    }
    
}
