package tonius.neiintegration.mods.electricalage;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class ElectricalAgeIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Electrical Age";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("Eln");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerCompressor());
        this.registerHandler(new RecipeHandlerMacerator());
        this.registerHandler(new RecipeHandlerMagnetizer());
        this.registerHandler(new RecipeHandlerPlateMachine());
    }
    
}
