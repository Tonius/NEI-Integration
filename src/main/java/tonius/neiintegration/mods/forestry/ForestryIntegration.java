package tonius.neiintegration.mods.forestry;

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
        this.registerHandler(new RecipeHandlerShapedCustom());
        this.registerHandler(new RecipeHandlerBottler());
        this.registerHandler(new RecipeHandlerCarpenter());
        this.registerHandler(new RecipeHandlerCentrifuge());
        this.registerHandler(new RecipeHandlerFabricator());
        this.registerHandler(new RecipeHandlerFermenter());
        this.registerHandler(new RecipeHandlerMoistener());
        this.registerHandler(new RecipeHandlerSqueezer());
        this.registerHandler(new RecipeHandlerStill());
    }
    
}
