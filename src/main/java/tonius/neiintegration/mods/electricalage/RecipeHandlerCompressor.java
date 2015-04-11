package tonius.neiintegration.mods.electricalage;

import mods.eln.Eln;
import mods.eln.misc.RecipesList;
import tonius.neiintegration.Utils;

public class RecipeHandlerCompressor extends RecipeHandlerElnBase {
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.electricalage.compressor");
    }
    
    @Override
    public String getRecipeID() {
        return "electricalage.compressor";
    }
    
    @Override
    public RecipesList getRecipes() {
        return Eln.instance.compressorRecipes;
    }
    
}
