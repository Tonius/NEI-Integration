package tonius.neiintegration.mcforge;

import tonius.neiintegration.IntegrationBase;
import codechicken.nei.api.API;

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
        API.addOption(new LoadedModDumper());
        API.addOption(new TileEntityDumper());
        API.addOption(new OreDictionaryDumper());
        API.addOption(new FluidDumper());
        API.addOption(new FluidContainerDumper());
        API.addOption(new RecipeHandlerDumper());
        
        registerHandler(new RecipeHandlerFluidRegistry());
    }
    
}
