package tonius.neiintegration;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import tonius.neiintegration.buildcraft.BuildCraftIntegration;
import tonius.neiintegration.config.Config;
import tonius.neiintegration.electricalage.ElectricalAgeIntegration;
import tonius.neiintegration.forestry.ForestryIntegration;
import tonius.neiintegration.forge.ForgeIntegration;
import tonius.neiintegration.minefactoryreloaded.MFRIntegration;
import tonius.neiintegration.railcraft.RailcraftIntegration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = NEIIntegration.MODID, acceptableRemoteVersions = "*", guiFactory = "tonius.neiintegration.config.ConfigGuiFactory")
public class NEIIntegration {
    
    public static final String MODID = "neiintegration";
    
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
            MinecraftForge.EVENT_BUS.register(new ItemTooltipHandler());
            
            integrations.add(new ForgeIntegration());
            integrations.add(new BuildCraftIntegration());
            integrations.add(new ElectricalAgeIntegration());
            integrations.add(new ForestryIntegration());
            integrations.add(new MFRIntegration());
            integrations.add(new RailcraftIntegration());
        }
    }
    
}
