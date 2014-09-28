package tonius.neiintegration;

import codechicken.nei.api.API;

public abstract class IntegrationBase {
    
    public abstract String getName();
    
    public abstract boolean isValid();
    
    public abstract void loadConfig();
    
    public static void registerHandler(RecipeHandlerBase handler) {
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
    
}
