package tonius.neiintegration.mods.electricalage;

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
        this.registerHandler(new RecipeHandlerCompressor());
        this.registerHandler(new RecipeHandlerMacerator());
        this.registerHandler(new RecipeHandlerMagnetizer());
        this.registerHandler(new RecipeHandlerPlateMachine());
    }
    
}
