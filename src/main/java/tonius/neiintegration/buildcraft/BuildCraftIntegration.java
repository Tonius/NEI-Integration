package tonius.neiintegration.buildcraft;

import tonius.neiintegration.IntegrationBase;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class BuildCraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "BuildCraft";
    }
    
    @Override
    public boolean isValid() {
        for (ModContainer mod : Loader.instance().getModList()) {
            if (mod.getModId().equals("BuildCraft|Core") && mod.getVersion().startsWith("6.1")) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void loadConfig() {
        if (Loader.isModLoaded("BuildCraft|Factory")) {
            registerHandler(new RecipeHandlerRefinery());
        }
        if (Loader.isModLoaded("BuildCraft|Silicon")) {
            registerHandler(new RecipeHandlerAssemblyTable());
        }
    }
    
}
