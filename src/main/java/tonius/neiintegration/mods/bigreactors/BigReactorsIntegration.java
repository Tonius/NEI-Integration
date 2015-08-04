package tonius.neiintegration.mods.bigreactors;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class BigReactorsIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Big Reactors";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("BigReactors");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerCyaniteReprocessor());
    }
    
}
