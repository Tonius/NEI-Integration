package tonius.neiintegration.mods.mcforge;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.OreDictionary;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import tonius.neiintegration.config.Config;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;

public class RecipeHandlerOreDictionary extends RecipeHandlerBase {
    
    public class CachedOreDictionaryRecipe extends CachedBaseRecipe {
        
        public String oreName;
        public PositionedStack input;
        public PositionedStack output;
        
        public CachedOreDictionaryRecipe(String oreName, ItemStack input, List<ItemStack> outputs) {
            this.oreName = oreName;
            this.input = new PositionedStack(input, 44, 22);
            this.output = new PositionedStack(outputs, 105, 22);
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerOreDictionary.this.cycleticks / 20);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            this.randomRenderPermutation(this.output, RecipeHandlerOreDictionary.this.cycleticks / 20);
            return this.output;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.oreDictionary");
    }
    
    @Override
    public String getRecipeID() {
        return "forge.oreDictionary";
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/basicProcessing.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.addTransferRect(68, 21, 24, 17);
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 160, 65);
    }
    
    @Override
    public void drawExtras(int recipe) {
        GuiDraw.drawStringC(((CachedOreDictionaryRecipe) this.arecipes.get(recipe)).oreName, 83, 5, 0x808080, false);
    }
    
    @Override
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        if (new Rectangle(68, 21, 24, 17).contains(relMouse)) {
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.oreDictionary.warning"));
        }
        return currenttip;
    }
    
    public List<String> getOreNames() {
        List<String> oreNames = new LinkedList<String>();
        for (String oreName : OreDictionary.getOreNames()) {
            if (oreName != null) {
                oreNames.add(oreName);
            }
        }
        Collections.sort(oreNames);
        return oreNames;
    }
    
    @Override
    public void loadAllRecipes() {
        if (Config.handlerOreDictionary) {
            for (String oreName : this.getOreNames()) {
                for (ItemStack ore : OreDictionary.getOres(oreName)) {
                    this.arecipes.add(new CachedOreDictionaryRecipe(oreName, ore, OreDictionary.getOres(oreName)));
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        if (Config.handlerOreDictionary) {
            for (int id : OreDictionary.getOreIDs(ingred)) {
                for (String oreName : this.getOreNames()) {
                    if (OreDictionary.getOreName(id).equals(oreName)) {
                        CachedOreDictionaryRecipe crecipe = new CachedOreDictionaryRecipe(oreName, new ItemStack(ingred.getItem(), 1, ingred.getItemDamage()), OreDictionary.getOres(oreName));
                        crecipe.setIngredientPermutation(Collections.singletonList(crecipe.input), ingred);
                        this.arecipes.add(crecipe);
                    }
                }
            }
        }
    }
    
}
