package tonius.neiintegration.botania;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.RecipeHandlerBase;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexicon;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;

public class RecipeHandlerElvenTrade extends RecipeHandlerBase {
    
    public class CachedElvenTradeRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedElvenTradeRecipe(RecipeElvenTrade recipe) {
            this.setIngredients(recipe.getInputs());
            this.output = new PositionedStack(recipe.getOutput(), 107, 46);
        }
        
        public void setIngredients(List<Object> inputs) {
            int i = 0;
            for (Object o : inputs) {
                if (o instanceof String) {
                    this.inputs.add(new PositionedStack(OreDictionary.getOres((String) o), 60 + i * 18, 6));
                } else {
                    this.inputs.add(new PositionedStack(o, 60 + i * 18, 6));
                }
                i++;
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerElvenTrade.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Elven Trade";
    }
    
    @Override
    public String getRecipeID() {
        return "botania.elvenTrade";
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/blank.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(35, 30, 48, 48), this.getRecipeID()));
    }
    
    @Override
    public int recipiesPerPage() {
        return 1;
    }
    
    @Override
    public void drawBackground(int recipe) {
        super.drawBackground(recipe);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        GuiDraw.changeTexture("botania:textures/gui/elvenTradeOverlay.png");
        GuiDraw.drawTexturedModalRect(30, 10, 17, 17, 100, 80);
        // TODO: render the portal texture when merging into Botania
    }
    
    private static boolean hasElvenKnowledge() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null) {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack != null && stack.getItem() instanceof ILexicon) {
                    ILexicon lexicon = (ILexicon) stack.getItem();
                    if (lexicon.isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID()) && hasElvenKnowledge()) {
            if (hasElvenKnowledge()) {
                for (RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
                    this.arecipes.add(new CachedElvenTradeRecipe(recipe));
                }
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (hasElvenKnowledge()) {
            for (RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
                if (NEIServerUtils.areStacksSameTypeCrafting(recipe.getOutput(), result)) {
                    this.arecipes.add(new CachedElvenTradeRecipe(recipe));
                }
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (hasElvenKnowledge()) {
            for (RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
                CachedElvenTradeRecipe crecipe = new CachedElvenTradeRecipe(recipe);
                if (crecipe.contains(crecipe.inputs, ingredient)) {
                    this.arecipes.add(crecipe);
                }
            }
        }
    }
    
}
