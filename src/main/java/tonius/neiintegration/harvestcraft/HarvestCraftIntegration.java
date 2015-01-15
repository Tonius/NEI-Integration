package tonius.neiintegration.harvestcraft;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class HarvestCraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Pam's HarvestCraft";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("harvestcraft");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerApiary());
        registerHandler(new RecipeHandlerPresser());
    }
    
}
