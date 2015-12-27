package tonius.neiintegration.mods.railcraft;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class RailcraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Railcraft";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("Railcraft");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerBlastFurnace());
        this.registerHandler(new RecipeHandlerCokeOven());
        this.registerHandler(new RecipeHandlerRockCrusher());
        this.registerHandler(new RecipeHandlerRollingMachineShaped());
        this.registerHandler(new RecipeHandlerRollingMachineShapeless());
    }
    
}
