package tonius.neiintegration;

import java.util.List;

import org.apache.logging.log4j.Logger;

import tonius.neiintegration.config.Config;
import tonius.neiintegration.mods.Integrations;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIIntegration.MODID, version = NEIIntegration.VERSION, acceptableRemoteVersions = NEIIntegration.ACCEPTABLE_REMOTE_VERSIONS, guiFactory = NEIIntegration.GUI_FACTORY)
public class NEIIntegration {
    
    public static final String MODID = "neiintegration";
    public static final String VERSION = "@VERSION@";
    public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";
    public static final String GUI_FACTORY = "tonius.neiintegration.config.ConfigGuiFactory";
    
    @Instance(MODID)
    public static NEIIntegration instance;
    public static Logger log;
    public static List<IntegrationBase> integrations;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        if (evt.getSide() != Side.CLIENT) {
            return;
        }
        
        log = evt.getModLog();
        log.info("Starting NEI Integration");
        
        integrations = Integrations.getIntegrations();
        Config.preInit(evt);
    }
    
}
