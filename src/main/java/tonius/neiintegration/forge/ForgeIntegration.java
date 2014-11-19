package tonius.neiintegration.forge;

import tonius.neiintegration.IntegrationBase;

public class ForgeIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Forge";
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerFluidRegistry());
    }
    
}
