package tonius.neiintegration.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import tonius.neiintegration.NEIIntegration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    
    public static Configuration config;
    public static List<Section> configSections = new ArrayList<Section>();
    
    public static final Section sectionTooltips = new Section("Tooltips", "tooltips");
    
    // tooltips
    public static boolean internalName = Defaults.internalName;
    public static boolean internalNameShift = Defaults.internalNameShift;
    public static boolean internalNameAdvanced = Defaults.internalNameAdvanced;
    public static boolean burnTime = Defaults.burnTime;
    public static boolean burnTimeShift = Defaults.burnTimeShift;
    public static boolean burnTimeAdvanced = Defaults.burnTimeAdvanced;
    public static boolean oreDictNames = Defaults.oreDictNames;
    public static boolean oreDictNamesShift = Defaults.oreDictNamesShift;
    public static boolean oreDictNamesAdvanced = Defaults.oreDictNamesAdvanced;
    public static boolean fluidRegInfo = Defaults.fluidRegInfo;
    public static boolean fluidRegInfoShift = Defaults.fluidRegInfoShift;
    public static boolean fluidRegInfoAdvanced = Defaults.fluidRegInfoAdvanced;
    
    public static void preInit(FMLPreInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(new Config());
        config = new Configuration(new File(evt.getModConfigurationDirectory(), NEIIntegration.MODID + ".cfg"));
        syncConfig();
    }
    
    public static void syncConfig() {
        NEIIntegration.log.info("Loading configuration files");
        try {
            processConfig();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent evt) {
        if (evt.modID.equals(NEIIntegration.MODID)) {
            syncConfig();
        }
    }
    
    public static void processConfig() {
        internalName = config.get(sectionTooltips.name, "Internal Name", Defaults.internalName, "Show the internal name (example: 'minecraft:stone') of items.").getBoolean(Defaults.internalName);
        internalNameShift = config.get(sectionTooltips.name, "Internal Name Shift", Defaults.internalNameShift, "If internal names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.internalNameShift);
        internalNameAdvanced = config.get(sectionTooltips.name, "Internal Name Advanced", Defaults.internalNameAdvanced, "If internal names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.internalNameAdvanced);
        burnTime = config.get(sectionTooltips.name, "Burn Time", Defaults.burnTime, "Show the burn time of items when used as furnace fuel.").getBoolean(Defaults.burnTime);
        burnTimeShift = config.get(sectionTooltips.name, "Burn Time Shift", Defaults.burnTimeShift, "If burn times are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.burnTimeShift);
        burnTimeAdvanced = config.get(sectionTooltips.name, "Burn Time Advanced", Defaults.burnTimeAdvanced, "If burn times are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.burnTimeAdvanced);
        oreDictNames = config.get(sectionTooltips.name, "Ore Dictionary Names", Defaults.oreDictNames, "Show the Ore Dictionary names of items.").getBoolean(Defaults.oreDictNames);
        oreDictNamesShift = config.get(sectionTooltips.name, "Ore Dictionary Names Shift", Defaults.oreDictNamesShift, "If Ore Dictionary names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.oreDictNamesShift);
        oreDictNamesAdvanced = config.get(sectionTooltips.name, "Ore Dictionary Names Advanced", Defaults.oreDictNamesAdvanced, "If Ore Dictionary names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.oreDictNamesAdvanced);
        fluidRegInfo = config.get(sectionTooltips.name, "Fluid Registry Info", Defaults.fluidRegInfo, "Show some fluid info on fluid-related items.").getBoolean(Defaults.fluidRegInfo);
        fluidRegInfoShift = config.get(sectionTooltips.name, "Fluid Registry Info Shift", Defaults.fluidRegInfoShift, "If fluid registry info is enabled, it will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.fluidRegInfoShift);
        fluidRegInfoAdvanced = config.get(sectionTooltips.name, "Fluid Registry Info Advanced", Defaults.fluidRegInfoAdvanced, "If fluid registry info is enabled, it will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.fluidRegInfoAdvanced);
    }
    
}
