package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityComposter;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerComposter extends RecipeHandlerBase {
    
    private static final Rectangle SEWAGE = new Rectangle(111, 2, 16, 60);
    private static final Rectangle ENERGY = new Rectangle(129, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(139, 2, 8, 60);
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
        
        public FluidTankElement sewageInput;
        public PositionedStack output;
        
        public CachedComposterRecipe() {
            this.sewageInput = new FluidTankElement(SEWAGE, 4000, new FluidStack(FluidRegistry.getFluid("sewage"), sewagePerOperation));
            this.output = new PositionedStack(new ItemStack(fertilizer), 48, 24);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public List<FluidTankElement> getFluidTanks() {
            return getSingleList(this.sewageInput);
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
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(76, 25, 22, 15), this.getRecipeID(), new Object[0]));
        this.transferRects.add(new RecipeTransferRect(WORK, this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(76, 25, 0, 15, 22, 15);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(111, 2, 176, 0, 16, 60);
        this.drawProgressBar(129, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(139, 0, 185, 58, 8, 62, 40, 3);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (ENERGY.contains(relMouse)) {
            currenttip.add(energyPerOperation + " RF");
        }
        return currenttip;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            this.arecipes.add(new CachedComposterRecipe());
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result.getItem() == fertilizer) {
            this.arecipes.add(new CachedComposterRecipe());
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        if (ingredient.getFluid().getName().equals("sewage")) {
            this.arecipes.add(new CachedComposterRecipe());
        }
    }
    
}
