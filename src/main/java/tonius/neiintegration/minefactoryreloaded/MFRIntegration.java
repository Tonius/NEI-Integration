package tonius.neiintegration.minefactoryreloaded;

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
        registerHandler(new RecipeHandlerComposter());
        registerHandler(new RecipeHandlerSludgeBoiler());
    }
    
}
