package tonius.neiintegration.forestry;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class ForestryIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Forestry";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("Forestry");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerShapedCustom());
        registerHandler(new RecipeHandlerCarpenter());
    }
    
}
