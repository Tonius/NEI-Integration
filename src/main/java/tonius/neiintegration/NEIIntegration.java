package tonius.neiintegration;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import tonius.neiintegration.config.Config;
import tonius.neiintegration.mods.ModIntegration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIIntegration.MODID, dependencies = NEIIntegration.DEPENDENCIES, acceptableRemoteVersions = NEIIntegration.ACCEPTABLE_REMOTE_VERSIONS, guiFactory = NEIIntegration.GUI_FACTORY)
public class NEIIntegration {
    
    public static final String MODID = "neiintegration";
    public static final String DEPENDENCIES = "after:Forestry@[3.6.0,)";
    public static final String ACCEPTABLE_REMOTE_VERSIONS = "*";
    public static final String GUI_FACTORY = "tonius.neiintegration.config.ConfigGuiFactory";
    
    @Instance(MODID)
    public static NEIIntegration instance;
    public static Logger log;
    
    public static List<IntegrationBase> integrations = new ArrayList<IntegrationBase>();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        log = evt.getModLog();
        log.info("Starting NEI Integration");
        
        Config.preInit(evt);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent evt) {
        if (evt.getSide() == Side.CLIENT) {
            ModIntegration.init(integrations);
        }
    }
    
}
