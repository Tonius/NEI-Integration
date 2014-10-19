package tonius.neiintegration.botania;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import tonius.neiintegration.RecipeHandlerBase;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerPetals extends RecipeHandlerBase {
    
    private static Block centerItem;
    
    @Override
    public void prepare() {
        // XXX: change this when merging into Botania
        centerItem = GameRegistry.findBlock("Botania", "altar");
    }
    
    public class CachedPetalsRecipe extends CachedBaseRecipe {
        
        public List<PositionedStack> inputs = new ArrayList<PositionedStack>();
        public PositionedStack output;
        
        public CachedPetalsRecipe(RecipePetals recipe, boolean addCenterItem) {
            this.setIngredients(recipe.getInputs());
            this.output = new PositionedStack(recipe.getOutput(), 111, 21);
            if (addCenterItem) {
                this.inputs.add(new PositionedStack(new ItemStack(centerItem), 73, 55));
            }
        }
        
        public CachedPetalsRecipe(RecipePetals recipe) {
            this(recipe, true);
        }
        
        public void setIngredients(List<Object> inputs) {
            float degreePerInput = 360F / inputs.size();
            float currentDegree = -90F;
            
            for (Object o : inputs) {
                int posX = (int) Math.round(73 + Math.cos(currentDegree * Math.PI / 180D) * 32);
                int posY = (int) Math.round(55 + Math.sin(currentDegree * Math.PI / 180D) * 32);
                
                if (o instanceof String) {
                    this.inputs.add(new PositionedStack(OreDictionary.getOres((String) o), posX, posY));
                } else {
                    this.inputs.add(new PositionedStack(o, posX, posY));
                }
                currentDegree += degreePerInput;
            }
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            return this.getCycledIngredients(RecipeHandlerPetals.this.cycleticks / 20, this.inputs);
        }
        
        @Override
        public PositionedStack getResult() {
            return this.output;
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Petal Apothecary";
    }
    
    @Override
    public String getRecipeID() {
        return "botania.petals";
    }
    
    @Override
    public String getGuiTexture() {
        return "neiintegration:textures/blank.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(72, 54, 18, 18), this.getRecipeID()));
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
        GuiDraw.changeTexture("botania:textures/gui/petalOverlay.png");
        GuiDraw.drawTexturedModalRect(45, 10, 38, 7, 92, 92);
    }
    
    public List<? extends RecipePetals> getRecipes() {
        return BotaniaAPI.petalRecipes;
    }
    
    public CachedPetalsRecipe getCachedRecipe(RecipePetals recipe) {
        return new CachedPetalsRecipe(recipe);
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(this.getRecipeID())) {
            for (RecipePetals recipe : this.getRecipes()) {
                this.arecipes.add(this.getCachedRecipe(recipe));
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (RecipePetals recipe : this.getRecipes()) {
            if (recipe.getOutput().stackTagCompound != null && NEIServerUtils.areStacksSameType(recipe.getOutput(), result) || recipe.getOutput().stackTagCompound == null && NEIServerUtils.areStacksSameTypeCrafting(recipe.getOutput(), result)) {
                this.arecipes.add(this.getCachedRecipe(recipe));
            }
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (RecipePetals recipe : this.getRecipes()) {
            inputs: for (Object o : recipe.getInputs()) {
                if (o instanceof String) {
                    for (int i : OreDictionary.getOreIDs(ingredient)) {
                        if (OreDictionary.getOreName(i).equals(o)) {
                            this.arecipes.add(this.getCachedRecipe(recipe));
                            break inputs;
                        }
                    }
                } else if (NEIServerUtils.areStacksSameTypeCrafting((ItemStack) o, ingredient)) {
                    this.arecipes.add(this.getCachedRecipe(recipe));
                    break;
                }
            }
        }
    }
    
}
