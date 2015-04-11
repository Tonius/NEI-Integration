package tonius.neiintegration.mods.electricalage;

import mods.eln.Eln;
import mods.eln.misc.RecipesList;
import tonius.neiintegration.Utils;

public class RecipeHandlerPlateMachine extends RecipeHandlerElnBase {
    
    @Override
    public String getRecipeName() {
        return Utils.translate("handler.electricalage.plateMachine");
    }
    
    @Override
    public String getRecipeID() {
        return "electricalage.plateMachine";
    }
    
    @Override
    public RecipesList getRecipes() {
        return Eln.instance.plateMachineRecipes;
    }
    
}
