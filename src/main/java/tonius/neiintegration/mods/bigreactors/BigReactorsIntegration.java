package tonius.neiintegration.mods.bigreactors;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class BigReactorsIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Big Reactors";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("BigReactors");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerCyaniteReprocessor());
    }
    
}
