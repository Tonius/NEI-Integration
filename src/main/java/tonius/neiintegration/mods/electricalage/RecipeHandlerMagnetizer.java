package tonius.neiintegration.mods.electricalage;

import mods.eln.Eln;
import mods.eln.misc.RecipesList;
import tonius.neiintegration.Utils;

public class RecipeHandlerMagnetizer extends RecipeHandlerElnBase {
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.electricalage.magnetizer");
    }
    
    @Override
    public String getRecipeID() {
        return "electricalage.magnetizer";
    }
    
    @Override
    public RecipesList getRecipes() {
        return Eln.instance.magnetiserRecipes;
    }
    
}
