package tonius.neiintegration.mods.electricalage;

import mods.eln.Eln;
import mods.eln.misc.RecipesList;
import tonius.neiintegration.Utils;

public class RecipeHandlerMacerator extends RecipeHandlerElnBase {
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.electricalage.macerator");
    }
    
    @Override
    public String getRecipeID() {
        return "electricalage.macerator";
    }
    
    @Override
    public RecipesList getRecipes() {
        return Eln.instance.maceratorRecipes;
    }
    
}
