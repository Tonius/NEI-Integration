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
    
    public static final Section sectionHandlers = new Section("Handlers", "handlers");
    public static final Section sectionTooltips = new Section("Tooltips", "tooltips");
    
    // handlers
    public static boolean handler_fluidRegistry = Defaults.handler_fluidRegistry;
    public static boolean handler_oreDictionary = Defaults.handler_oreDictionary;
    
    // tooltips
    public static boolean tooltip_unlocalizedName = Defaults.tooltip_unlocalizedName;
    public static boolean tooltip_unlocalizedNameShift = Defaults.tooltip_unlocalizedNameShift;
    public static boolean tooltip_unlocalizedNameAdvanced = Defaults.tooltip_unlocalizedNameAdvanced;
    public static boolean tooltip_internalName = Defaults.tooltip_internalName;
    public static boolean tooltip_internalNameShift = Defaults.tooltip_internalNameShift;
    public static boolean tooltip_internalNameAdvanced = Defaults.tooltip_internalNameAdvanced;
    public static boolean tooltip_maxStack = Defaults.tooltip_maxStack;
    public static boolean tooltip_maxStackShift = Defaults.tooltip_maxStackShift;
    public static boolean tooltip_maxStackAdvanced = Defaults.tooltip_maxStackAdvanced;
    public static boolean tooltip_burnTime = Defaults.tooltip_burnTime;
    public static boolean tooltip_burnTimeShift = Defaults.tooltip_burnTimeShift;
    public static boolean tooltip_burnTimeAdvanced = Defaults.tooltip_burnTimeAdvanced;
    public static boolean tooltip_oreDictNames = Defaults.tooltip_oreDictNames;
    public static boolean tooltip_oreDictNamesShift = Defaults.tooltip_oreDictNamesShift;
    public static boolean tooltip_oreDictNamesAdvanced = Defaults.tooltip_oreDictNamesAdvanced;
    public static boolean tooltip_fluidRegInfo = Defaults.tooltip_fluidRegInfo;
    public static boolean tooltip_fluidRegInfoShift = Defaults.tooltip_fluidRegInfoShift;
    public static boolean tooltip_fluidRegInfoAdvanced = Defaults.tooltip_fluidRegInfoAdvanced;
    
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
        handler_fluidRegistry = config.get(sectionHandlers.name, "Fluid Registry", Defaults.handler_fluidRegistry, "Shows information about registered fluids when looking them or related items up.").getBoolean(Defaults.handler_fluidRegistry);
        handler_oreDictionary = config.get(sectionHandlers.name, "Ore Dictionary", Defaults.handler_oreDictionary, "Shows information about items registered in the Ore Dictionary when looking up item usage.").getBoolean(Defaults.handler_oreDictionary);
        
        tooltip_unlocalizedName = config.get(sectionTooltips.name, "Unlocalized Name", Defaults.tooltip_unlocalizedName, "Show the unlocalized name of items.").getBoolean(Defaults.tooltip_unlocalizedName);
        tooltip_unlocalizedNameShift = config.get(sectionTooltips.name, "Unlocalized Name Shift", Defaults.tooltip_unlocalizedNameShift, "If unlocalized names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_unlocalizedNameShift);
        tooltip_unlocalizedNameAdvanced = config.get(sectionTooltips.name, "Unlocalized Name Advanced", Defaults.tooltip_unlocalizedNameAdvanced, "If unlocalized names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_unlocalizedNameAdvanced);
        tooltip_internalName = config.get(sectionTooltips.name, "Internal Name", Defaults.tooltip_internalName, "Show the internal name (example: 'minecraft:stone') of items.").getBoolean(Defaults.tooltip_internalName);
        tooltip_internalNameShift = config.get(sectionTooltips.name, "Internal Name Shift", Defaults.tooltip_internalNameShift, "If internal names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_internalNameShift);
        tooltip_internalNameAdvanced = config.get(sectionTooltips.name, "Internal Name Advanced", Defaults.tooltip_internalNameAdvanced, "If internal names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_internalNameAdvanced);
        tooltip_maxStack = config.get(sectionTooltips.name, "Maximum Stack Size", Defaults.tooltip_maxStack, "Show the unlocalized name (example: 'minecraft:stone') of items.").getBoolean(Defaults.tooltip_maxStack);
        tooltip_maxStackShift = config.get(sectionTooltips.name, "Maximum Stack Size Shift", Defaults.tooltip_maxStackShift, "If unlocalized names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_maxStackShift);
        tooltip_maxStackAdvanced = config.get(sectionTooltips.name, "Maximum Stack Size Advanced", Defaults.tooltip_maxStackAdvanced, "If unlocalized names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_maxStackAdvanced);
        tooltip_burnTime = config.get(sectionTooltips.name, "Burn Time", Defaults.tooltip_burnTime, "Show the burn time of items when used as furnace fuel.").getBoolean(Defaults.tooltip_burnTime);
        tooltip_burnTimeShift = config.get(sectionTooltips.name, "Burn Time Shift", Defaults.tooltip_burnTimeShift, "If burn times are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_burnTimeShift);
        tooltip_burnTimeAdvanced = config.get(sectionTooltips.name, "Burn Time Advanced", Defaults.tooltip_burnTimeAdvanced, "If burn times are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_burnTimeAdvanced);
        tooltip_oreDictNames = config.get(sectionTooltips.name, "Ore Dictionary Names", Defaults.tooltip_oreDictNames, "Show the Ore Dictionary names of items.").getBoolean(Defaults.tooltip_oreDictNames);
        tooltip_oreDictNamesShift = config.get(sectionTooltips.name, "Ore Dictionary Names Shift", Defaults.tooltip_oreDictNamesShift, "If Ore Dictionary names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_oreDictNamesShift);
        tooltip_oreDictNamesAdvanced = config.get(sectionTooltips.name, "Ore Dictionary Names Advanced", Defaults.tooltip_oreDictNamesAdvanced, "If Ore Dictionary names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_oreDictNamesAdvanced);
        tooltip_fluidRegInfo = config.get(sectionTooltips.name, "Fluid Registry Info", Defaults.tooltip_fluidRegInfo, "Show some fluid info on fluid-related items.").getBoolean(Defaults.tooltip_fluidRegInfo);
        tooltip_fluidRegInfoShift = config.get(sectionTooltips.name, "Fluid Registry Info Shift", Defaults.tooltip_fluidRegInfoShift, "If fluid registry info is enabled, it will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltip_fluidRegInfoShift);
        tooltip_fluidRegInfoAdvanced = config.get(sectionTooltips.name, "Fluid Registry Info Advanced", Defaults.tooltip_fluidRegInfoAdvanced, "If fluid registry info is enabled, it will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltip_fluidRegInfoAdvanced);
    }
    
}
