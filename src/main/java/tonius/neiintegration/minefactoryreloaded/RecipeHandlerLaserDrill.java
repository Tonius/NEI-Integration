package tonius.neiintegration.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLaserDrill;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLaserDrillPrecharger;
import tonius.neiintegration.RecipeHandlerBase;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import cofh.lib.util.WeightedRandomItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerLaserDrill extends RecipeHandlerBase {
    
    private static final Rectangle ENERGY = new Rectangle(139, 2, 8, 60);
    private static final Rectangle WORK = new Rectangle(149, 2, 8, 60);
    private static List<WeightedRandom.Item> laserOres;
    private static int totalWeight;
    private static Map<Integer, List<ItemStack>> laserPreferredOres;
    private static int energyPerOperation;
    private static Item laserFocus;
    
    @Override
    public void prepare() {
        laserOres = MFRRegistry.getLaserOres();
        for (WeightedRandom.Item ore : laserOres) {
            totalWeight += ore.itemWeight;
        }
        
        laserPreferredOres = new HashMap<Integer, List<ItemStack>>();
        for (int i = 0; i <= 15; i++) {
            List<ItemStack> preferredOres = MFRRegistry.getLaserPreferredOres(i);
            laserPreferredOres.put(i, preferredOres);
        }
        
        TileEntityLaserDrillPrecharger dummyPrecharger = new TileEntityLaserDrillPrecharger();
        TileEntityLaserDrill dummyDrill = new TileEntityLaserDrill();
        energyPerOperation = dummyPrecharger.getActivationEnergy() * dummyDrill.getWorkMax();
        dummyPrecharger = null;
        dummyDrill = null;
        
        laserFocus = GameRegistry.findItem("MineFactoryReloaded", "item.mfr.laserfocus");
    }
    
    public class CachedLaserDrillRecipe extends CachedBaseRecipe {
        
        public PositionedStack output;
        public float chance;
        public PositionedStack focus = null;
        
        public CachedLaserDrillRecipe(ItemStack output, int weight, Integer focus) {
            this.output = new PositionedStack(output, 74, 24);
            this.chance = (float) weight / (float) totalWeight;
            if (focus != null) {
                this.focus = new PositionedStack(new ItemStack(laserFocus, 1, focus), 20, 24);
            }
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedStack getOtherStack() {
            if (this.focus != null) {
                return this.focus;
            } else {
                return super.getOtherStack();
            }
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Laser Drill";
    }
    
    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.laserdrill";
    }
    
    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "laserdrill.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(104, 25, 22, 15), this.getRecipeID(), new Object[0]));
    }
    
    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 11, 13, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(19, 23, 0, 30, 18, 18);
        GuiDraw.drawTexturedModalRect(104, 25, 0, 15, 22, 15);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(111, 2, 176, 0, 16, 60);
        this.drawProgressBar(139, 0, 176, 58, 8, 62, 1.0F, 3);
        this.drawProgressBar(149, 0, 185, 58, 8, 62, 60, 3);
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedLaserDrillRecipe crecipe = (CachedLaserDrillRecipe) this.arecipes.get(recipe);
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(2);
        GuiDraw.drawStringC(percentFormat.format(crecipe.chance), 83, 44, 0x808080, false);
        GuiDraw.drawStringC("Focus", 29, 44, 0x808080, false);
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
            for (WeightedRandom.Item drop : laserOres) {
                if (drop instanceof WeightedRandomItemStack) {
                    ItemStack dropStack = ((WeightedRandomItemStack) drop).getStack();
                    for (int i : laserPreferredOres.keySet()) {
                        List<ItemStack> preferredStacks = laserPreferredOres.get(i);
                        if (preferredStacks != null) {
                            for (ItemStack preferredStack : preferredStacks) {
                                if (NEIServerUtils.areStacksSameTypeCrafting(preferredStack, dropStack)) {
                                    this.arecipes.add(new CachedLaserDrillRecipe(dropStack, drop.itemWeight, i));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (WeightedRandom.Item drop : laserOres) {
            if (drop instanceof WeightedRandomItemStack) {
                if (NEIServerUtils.areStacksSameTypeCrafting(((WeightedRandomItemStack) drop).getStack(), result)) {
                    ItemStack dropStack = ((WeightedRandomItemStack) drop).getStack();
                    for (int i : laserPreferredOres.keySet()) {
                        List<ItemStack> preferredStacks = laserPreferredOres.get(i);
                        if (preferredStacks != null) {
                            for (ItemStack preferredStack : preferredStacks) {
                                if (NEIServerUtils.areStacksSameTypeCrafting(preferredStack, dropStack)) {
                                    this.arecipes.add(new CachedLaserDrillRecipe(result, drop.itemWeight, i));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        int dmg = ingred.getItemDamage();
        if (ingred.getItem() == laserFocus && dmg <= 15) {
            for (WeightedRandom.Item drop : laserOres) {
                if (drop instanceof WeightedRandomItemStack) {
                    ItemStack dropStack = ((WeightedRandomItemStack) drop).getStack();
                    List<ItemStack> preferredStacks = laserPreferredOres.get(dmg);
                    if (preferredStacks != null) {
                        for (ItemStack preferredStack : preferredStacks) {
                            if (NEIServerUtils.areStacksSameTypeCrafting(preferredStack, dropStack)) {
                                this.arecipes.add(new CachedLaserDrillRecipe(dropStack, drop.itemWeight, dmg));
                            }
                        }
                    }
                }
            }
        }
    }
    
}
