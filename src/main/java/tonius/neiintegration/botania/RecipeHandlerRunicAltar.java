package tonius.neiintegration.botania;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import codechicken.nei.PositionedStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeHandlerRunicAltar extends RecipeHandlerPetals {
    
    private static Block centerItem;
    
    @Override
    public void prepare() {
        centerItem = GameRegistry.findBlock("Botania", "runeAltar");
    }
    
    public class CachedRunicAltarRecipe extends CachedPetalsRecipe {
        
        public int manaUsage;
        
        public CachedRunicAltarRecipe(RecipeRuneAltar recipe) {
            super(recipe, false);
            this.manaUsage = recipe.getManaUsage();
            this.inputs.add(new PositionedStack(new ItemStack(centerItem), 73, 55));
        }
        
    }
    
    @Override
    public String getRecipeName() {
        return "Runic Altar";
    }
    
    @Override
    public String getRecipeID() {
        return "botania.runicAltar";
    }
    
    @Override
    public List<? extends RecipePetals> getRecipes() {
        return BotaniaAPI.runeAltarRecipes;
    }
    
    @Override
    public CachedPetalsRecipe getCachedRecipe(RecipePetals recipe) {
        return new CachedRunicAltarRecipe((RecipeRuneAltar) recipe);
    }
    
}
