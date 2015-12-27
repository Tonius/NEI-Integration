package tonius.neiintegration.mods.harvestcraft;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

import com.pam.harvestcraft.PresserRecipes;

public class HarvestCraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Pam's HarvestCraft";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("harvestcraft");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerApiary());
        
        try {
            PresserRecipes.class.getMethod("pressing");
            this.registerHandler(new RecipeHandlerPresser());
        } catch (NoSuchMethodException e) {
            this.registerHandler(new RecipeHandlerPresserOld());
        }
        
        if (Utils.getClass("com.pam.harvestcraft.OvenRecipes") != null) {
            this.registerHandler(new RecipeHandlerOven());
        }
        
        if (Utils.getClass("com.pam.harvestcraft.ChurnRecipes") != null) {
            this.registerHandler(new RecipeHandlerChurn());
        }
        
        if (Utils.getClass("com.pam.harvestcraft.QuernRecipes") != null) {
            this.registerHandler(new RecipeHandlerQuern());
        }
    }
}
