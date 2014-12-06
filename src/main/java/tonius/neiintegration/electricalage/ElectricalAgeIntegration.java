package tonius.neiintegration.electricalage;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;

public class ElectricalAgeIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Electrical Age";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("Eln");
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerCompressor());
        registerHandler(new RecipeHandlerMacerator());
        registerHandler(new RecipeHandlerMagnetizer());
        registerHandler(new RecipeHandlerPlateMachine());
    }
    
}
