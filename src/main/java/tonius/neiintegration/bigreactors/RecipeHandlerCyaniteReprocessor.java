package tonius.neiintegration.bigreactors;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import cpw.mods.fml.relauncher.ReflectionHelper;
import erogenousbeef.bigreactors.api.data.OreDictToReactantMapping;
import erogenousbeef.bigreactors.api.data.ReactantData;
import erogenousbeef.bigreactors.api.registry.Reactants;
import erogenousbeef.bigreactors.client.gui.GuiCyaniteReprocessor;

public class RecipeHandlerCyaniteReprocessor extends RecipeHandlerBase {
    
    private static Map<String, OreDictToReactantMapping> solidToReactant;
    
    @Override
    public void prepare() {
        solidToReactant = ReflectionHelper.getPrivateValue(Reactants.class, null, "_solidToReactant");
        API.setGuiOffset(GuiCyaniteReprocessor.class, 8, 17);
    }
    
    public class CachedCyaniteReprocessorRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        public PositionedFluidTank water;
        
        public CachedCyaniteReprocessorRecipe(ItemStack input, ItemStack output) {
            input = input.copy();
            input.stackSize = 2;
            this.input = new PositionedStack(input, 36, 25);
            this.output = new PositionedStack(output, 108, 25);
            this.water = new PositionedFluidTank(new FluidStack(FluidRegistry.WATER, 1000), 5000, new Rectangle(1, 1, 16, 62), "neiintegration:textures/overlays.png", new Point(18, 97));
        }
        
        @Override
        public PositionedStack getIngredient() {
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public PositionedFluidTank getFluidTank() {
            return this.water;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.blockBRDevice.0.name", false);
    }
    
    @Override
    public String getRecipeID() {
        return "bigreactors.cyaniteReprocessor";
    }
    
    @Override
    public String getGuiTexture() {
        return "bigreactors:textures/gui/CyaniteReprocessor.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(68, 25, 27, 18);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiCyaniteReprocessor.class;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 8, 16, 160, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 96, 18, 64);
    }
    
    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(68, 24, 0, 177, 27, 18, 100, 0);
        GuiDraw.drawStringC("2000 RF", 81, 48, 0x808080, false);
    }
    
    @Override
    public void loadAllRecipes() {
        for (OreDictToReactantMapping o : solidToReactant.values()) {
            ReactantData data = Reactants.getReactant(o.getProduct());
            if (data.isWaste()) {
                for (ItemStack ore : OreDictionary.getOres(o.getSource())) {
                    this.arecipes.add(new CachedCyaniteReprocessorRecipe(ore, OreDictionary.getOres("ingotBlutonium").get(0)));
                }
            }
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (Utils.areStacksSameTypeCraftingSafe(OreDictionary.getOres("ingotBlutonium").get(0), result)) {
            this.loadAllRecipes();
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (OreDictToReactantMapping o : solidToReactant.values()) {
            ReactantData data = Reactants.getReactant(o.getProduct());
            if (data.isWaste()) {
                for (ItemStack ore : OreDictionary.getOres(o.getSource())) {
                    if (Utils.areStacksSameTypeCraftingSafe(ore, ingred)) {
                        this.arecipes.add(new CachedCyaniteReprocessorRecipe(ore, OreDictionary.getOres("ingotBlutonium").get(0)));
                    }
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(FluidStack ingredient) {
        if (ingredient.getFluid() == FluidRegistry.WATER) {
            this.loadAllRecipes();
        }
    }
    
}
