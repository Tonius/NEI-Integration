package tonius.neiintegration;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import tonius.neiintegration.botania.BotaniaIntegration;
import tonius.neiintegration.buildcraft.BuildCraftIntegration;
import tonius.neiintegration.minefactoryreloaded.MFRIntegration;
import tonius.neiintegration.railcraft.RailcraftIntegration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIIntegration.MODID)
public class NEIIntegration {
    
    public static final String MODID = "neiintegration";
    public static final String PREFIX = MODID + ".";
    public static final String RESOURCE_PREFIX = MODID + ":";
    
    @Instance(MODID)
    public static NEIIntegration instance;
    public static Logger log;
    
    public static List<IntegrationBase> integrations = new ArrayList<IntegrationBase>();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        log = evt.getModLog();
        log.info("Starting NEI Integration");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent evt) {
        if (evt.getSide() == Side.CLIENT) {
            integrations.add(new BotaniaIntegration());
            integrations.add(new BuildCraftIntegration());
            integrations.add(new MFRIntegration());
            integrations.add(new RailcraftIntegration());
        }
    }
    
}
