package tonius.neiintegration.mods.minefactoryreloaded;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class MFRIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "MineFactory Reloaded";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("MineFactoryReloaded");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerBioReactor());
        this.registerHandler(new RecipeHandlerComposter());
        this.registerHandler(new RecipeHandlerGrinder());
        this.registerHandler(new RecipeHandlerHarvester());
        this.registerHandler(new RecipeHandlerLaserDrill());
        this.registerHandler(new RecipeHandlerLavaFabricator());
        this.registerHandler(new RecipeHandlerMeatPacker());
        this.registerHandler(new RecipeHandlerSewer());
        this.registerHandler(new RecipeHandlerSlaughterhouse());
        this.registerHandler(new RecipeHandlerSludgeBoiler());
    }
    
}
