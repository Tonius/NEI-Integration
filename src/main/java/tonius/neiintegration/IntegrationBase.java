package tonius.neiintegration;

import codechicken.nei.api.API;

public abstract class IntegrationBase {
    
    public abstract String getName();
    
    public boolean isEnabledByDefault() {
        return true;
    }
    
    public abstract boolean isValid();
    
    public abstract void loadConfig();
    
    protected void registerHandler(IRecipeHandler handler) {
        handler.prepare();
        API.registerRecipeHandler(handler);
        API.registerUsageHandler(handler);
    }
    
}
