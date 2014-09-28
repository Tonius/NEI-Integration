package tonius.neiintegration;

import codechicken.nei.api.IConfigureNEI;

public class NEIIntegrationConfig implements IConfigureNEI {
    
    @Override
    public void loadConfig() {
        for (IntegrationBase integration : NEIIntegration.integrations) {
            if (integration.isValid()) {
                NEIIntegration.log.info("Loading integration: " + integration.getName());
                integration.loadConfig();
            }
        }
    }
    
    @Override
    public String getName() {
        return "NEI Integration";
    }
    
    @Override
    public String getVersion() {
        return "${version}";
    }
    
}
