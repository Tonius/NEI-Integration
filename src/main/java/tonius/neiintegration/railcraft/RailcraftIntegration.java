package tonius.neiintegration.railcraft;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class RailcraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Railcraft";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("Railcraft");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerBlastFurnace());
        registerHandler(new RecipeHandlerCokeOven());
    }
    
}
