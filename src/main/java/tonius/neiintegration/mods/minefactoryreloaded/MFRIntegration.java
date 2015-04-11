package tonius.neiintegration.mods.minefactoryreloaded;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class MFRIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "MineFactory Reloaded";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("MineFactoryReloaded");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerBioReactor());
        registerHandler(new RecipeHandlerComposter());
        registerHandler(new RecipeHandlerGrinder());
        registerHandler(new RecipeHandlerHarvester());
        registerHandler(new RecipeHandlerLaserDrill());
        registerHandler(new RecipeHandlerLavaFabricator());
        registerHandler(new RecipeHandlerMeatPacker());
        registerHandler(new RecipeHandlerSewer());
        registerHandler(new RecipeHandlerSlaughterhouse());
        registerHandler(new RecipeHandlerSludgeBoiler());
    }
    
}
