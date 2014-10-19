package tonius.neiintegration.botania;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.RecipeHandlerBase;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerManaPool extends RecipeHandlerBase {
    
    private static Block centerItem;
    private static Block alchemyCatalyst;
    private static Block conjurationCatalyst;
    
    @Override
    public void prepare() {
        // XXX: change this when merging into Botania
        centerItem = GameRegistry.findBlock("Botania", "pool");
        alchemyCatalyst = GameRegistry.findBlock("Botania", "alchemyCatalyst");
        conjurationCatalyst = GameRegistry.findBlock("Botania", "conjurationCatalyst");
    }
    
    public class CachedManaPoolRecipe extends CachedBaseRecipe {
        
        public PositionedStack input;
        public PositionedStack output;
        public List<PositionedStack> otherStacks = new ArrayList<PositionedStack>();
        public int mana;
        
        public CachedManaPoolRecipe(RecipeManaInfusion recipe) {
            if (recipe.getInput() instanceof String) {
                this.input = new PositionedStack(OreDictionary.getOres((String) recipe.getInput()), 42, 17);
            } else {
                this.input = new PositionedStack(recipe.getInput(), 42, 17);
            }
            // RenderTilePool.forceMana = true; <-- use Botania render methods
            // when merging instead of PositionedStack
            this.otherStacks.add(new PositionedStack(new ItemStack(centerItem, 1, recipe.getOutput().getItem() == Item.getItemFromBlock(centerItem) ? 2 : 0), 71, 17));
            if (recipe.isAlchemy()) {
                this.otherStacks.add(new PositionedStack(new ItemStack(alchemyCatalyst), 10, 17));
            } else if (recipe.isConjuration()) {
                this.otherStacks.add(new PositionedStack(new ItemStack(conjurationCatalyst), 10, 17));
            }
            
            this.output = new PositionedStack(recipe.getOutput(), 101, 17);
            this.mana = recipe.getManaToConsume();
        }
        
        @Override
        public PositionedStack getIngredient() {
            this.randomRenderPermutation(this.input, RecipeHandlerManaPool.this.cycleticks / 20);
            return this.input;
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.otherStacks;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Mana Pool";
    }
    
    @Override
    public String getRecipeID() {
        return "botania.manaPool";
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/blank.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(70, 16, 18, 18), this.getRecipeID()));
    }
    
    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GuiDraw.changeTexture("botania:textures/gui/manaInfusionOverlay.png");
        GuiDraw.drawTexturedModalRect(45, 0, 38, 35, 92, 50);
    }
    
    // TODO: draw mana bar, alchemy/conjuration
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
                this.arecipes.add(new CachedManaPoolRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getOutput(), result)) {
                this.arecipes.add(new CachedManaPoolRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
            if (recipe.getInput() instanceof String) {
                for (int i : OreDictionary.getOreIDs(ingredient)) {
                    if (OreDictionary.getOreName(i).equals(recipe.getInput())) {
                        this.arecipes.add(new CachedManaPoolRecipe(recipe));
                    }
                }
            } else if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) recipe.getInput(), ingredient)) {
                this.arecipes.add(new CachedManaPoolRecipe(recipe));
            } else if (recipe.isAlchemy() && ingredient.getItem() == Item.getItemFromBlock(alchemyCatalyst) || recipe.isConjuration() && ingredient.getItem() == Item.getItemFromBlock(conjurationCatalyst)) {
                this.arecipes.add(new CachedManaPoolRecipe(recipe));
            }
        }
    }
    
}
