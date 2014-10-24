package tonius.neiintegration;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class IntegrationBase {
    
    public abstract String getName();
    
    public abstract boolean isValid();
    
    public abstract void loadConfig();
    
    public static void registerHandler(TemplateRecipeHandler handler) {
        if (handler instanceof IRecipeHandlerBase) {
            ((IRecipeHandlerBase) handler).prepare();
        }
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
    
}
