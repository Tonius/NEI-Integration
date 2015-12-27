package tonius.neiintegration.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.NEIIntegration;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {
    
    public static Configuration config;
    public static List<Section> configSections = new ArrayList<Section>();
    
    public static final Section sectionIntegrations = new Section("Integrations", "integrations");
    public static final Section sectionHandlers = new Section("Handlers", "handlers");
    public static final Section sectionTooltips = new Section("Tooltips", "tooltips");
    
    // handlers
    public static boolean handlerFluidRegistry = Defaults.handlerFluidRegistry;
    public static boolean handlerOreDictionary = Defaults.handlerOreDictionary;
    
    // tooltips
    public static boolean tooltipUnlocalizedName = Defaults.tooltipUnlocalizedName;
    public static boolean tooltipUnlocalizedNameShift = Defaults.tooltipUnlocalizedNameShift;
    public static boolean tooltipUnlocalizedNameAdvanced = Defaults.tooltipUnlocalizedNameAdvanced;
    public static boolean tooltipInternalName = Defaults.tooltipInternalName;
    public static boolean tooltipInternalNameShift = Defaults.tooltipInternalNameShift;
    public static boolean tooltipInternalNameAdvanced = Defaults.tooltipInternalNameAdvanced;
    public static boolean tooltipMaxStack = Defaults.tooltipMaxStack;
    public static boolean tooltipMaxStackShift = Defaults.tooltipMaxStackShift;
    public static boolean tooltipMaxStackAdvanced = Defaults.tooltipMaxStackAdvanced;
    public static boolean tooltipBurnTime = Defaults.tooltipBurnTime;
    public static boolean tooltipBurnTimeShift = Defaults.tooltipBurnTimeShift;
    public static boolean tooltipBurnTimeAdvanced = Defaults.tooltipBurnTimeAdvanced;
    public static boolean tooltipOreDictNames = Defaults.tooltipOreDictNames;
    public static boolean tooltipOreDictNamesShift = Defaults.tooltipOreDictNamesShift;
    public static boolean tooltipOreDictNamesAdvanced = Defaults.tooltipOreDictNamesAdvanced;
    public static boolean tooltipFluidRegInfo = Defaults.tooltipFluidRegInfo;
    public static boolean tooltipFluidRegInfoShift = Defaults.tooltipFluidRegInfoShift;
    public static boolean tooltipFluidRegInfoAdvanced = Defaults.tooltipFluidRegInfoAdvanced;
    
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
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent evt) {
        if (evt.modID.equals(NEIIntegration.MODID)) {
            syncConfig();
        }
    }
    
    public static void processConfig() {
        for (Iterator<IntegrationBase> itr = NEIIntegration.integrations.iterator(); itr.hasNext();) {
            IntegrationBase integration = itr.next();
            String name = integration.getName();
            boolean enabledByDefault = integration.isEnabledByDefault();
            if (!integration.isValid()) {
                itr.remove();
            } else if (!config.get(sectionIntegrations.name, name, enabledByDefault, String.format("Whether to enable %s integration. Disable if this part of the mod causes crashes.", name)).setRequiresMcRestart(true).getBoolean(enabledByDefault)) {
                itr.remove();
            }
        }
        
        handlerFluidRegistry = config.get(sectionHandlers.name, "Fluid Registry", Defaults.handlerFluidRegistry, "Shows information about registered fluids when looking them or related items up.").getBoolean(Defaults.handlerFluidRegistry);
        handlerOreDictionary = config.get(sectionHandlers.name, "Ore Dictionary", Defaults.handlerOreDictionary, "Shows information about items registered in the Ore Dictionary when looking up item usage.").getBoolean(Defaults.handlerOreDictionary);
        
        tooltipUnlocalizedName = config.get(sectionTooltips.name, "Unlocalized Name", Defaults.tooltipUnlocalizedName, "Show the unlocalized name (example: 'tile.stone') of items.").getBoolean(Defaults.tooltipUnlocalizedName);
        tooltipUnlocalizedNameShift = config.get(sectionTooltips.name, "Unlocalized Name Shift", Defaults.tooltipUnlocalizedNameShift, "If unlocalized names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipUnlocalizedNameShift);
        tooltipUnlocalizedNameAdvanced = config.get(sectionTooltips.name, "Unlocalized Name Advanced", Defaults.tooltipUnlocalizedNameAdvanced, "If unlocalized names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipUnlocalizedNameAdvanced);
        tooltipInternalName = config.get(sectionTooltips.name, "Internal Name", Defaults.tooltipInternalName, "Show the internal name (example: 'minecraft:stone') of items.").getBoolean(Defaults.tooltipInternalName);
        tooltipInternalNameShift = config.get(sectionTooltips.name, "Internal Name Shift", Defaults.tooltipInternalNameShift, "If internal names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipInternalNameShift);
        tooltipInternalNameAdvanced = config.get(sectionTooltips.name, "Internal Name Advanced", Defaults.tooltipInternalNameAdvanced, "If internal names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipInternalNameAdvanced);
        tooltipMaxStack = config.get(sectionTooltips.name, "Maximum Stack Size", Defaults.tooltipMaxStack, "Show the maximum stack size of items.").getBoolean(Defaults.tooltipMaxStack);
        tooltipMaxStackShift = config.get(sectionTooltips.name, "Maximum Stack Size Shift", Defaults.tooltipMaxStackShift, "If maximum stack sizes are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipMaxStackShift);
        tooltipMaxStackAdvanced = config.get(sectionTooltips.name, "Maximum Stack Size Advanced", Defaults.tooltipMaxStackAdvanced, "If maximum stack sizes are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipMaxStackAdvanced);
        tooltipBurnTime = config.get(sectionTooltips.name, "Burn Time", Defaults.tooltipBurnTime, "Show the burn time of items when used as furnace fuel.").getBoolean(Defaults.tooltipBurnTime);
        tooltipBurnTimeShift = config.get(sectionTooltips.name, "Burn Time Shift", Defaults.tooltipBurnTimeShift, "If burn times are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipBurnTimeShift);
        tooltipBurnTimeAdvanced = config.get(sectionTooltips.name, "Burn Time Advanced", Defaults.tooltipBurnTimeAdvanced, "If burn times are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipBurnTimeAdvanced);
        tooltipOreDictNames = config.get(sectionTooltips.name, "Ore Dictionary Names", Defaults.tooltipOreDictNames, "Show the Ore Dictionary names of items.").getBoolean(Defaults.tooltipOreDictNames);
        tooltipOreDictNamesShift = config.get(sectionTooltips.name, "Ore Dictionary Names Shift", Defaults.tooltipOreDictNamesShift, "If Ore Dictionary names are enabled, they will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipOreDictNamesShift);
        tooltipOreDictNamesAdvanced = config.get(sectionTooltips.name, "Ore Dictionary Names Advanced", Defaults.tooltipOreDictNamesAdvanced, "If Ore Dictionary names are enabled, they will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipOreDictNamesAdvanced);
        tooltipFluidRegInfo = config.get(sectionTooltips.name, "Fluid Registry Info", Defaults.tooltipFluidRegInfo, "Show some fluid info on fluid-related items.").getBoolean(Defaults.tooltipFluidRegInfo);
        tooltipFluidRegInfoShift = config.get(sectionTooltips.name, "Fluid Registry Info Shift", Defaults.tooltipFluidRegInfoShift, "If fluid registry info is enabled, it will only be shown if the Shift key is held. Effect stacks with Advanced if enabled.").getBoolean(Defaults.tooltipFluidRegInfoShift);
        tooltipFluidRegInfoAdvanced = config.get(sectionTooltips.name, "Fluid Registry Info Advanced", Defaults.tooltipFluidRegInfoAdvanced, "If fluid registry info is enabled, it will only be shown in advanced (F3+H) tooltips. Effect stacks with Shift if enabled.").getBoolean(Defaults.tooltipFluidRegInfoAdvanced);
    }
}
