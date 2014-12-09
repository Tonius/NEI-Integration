package tonius.neiintegration.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import tonius.neiintegration.NEIIntegration;
import tonius.neiintegration.Utils;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigGui extends GuiConfig {
    
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), NEIIntegration.MODID, false, false, Utils.translate("config.title"));
    }
    
    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = NEIIntegration.PREFIX + "config.";
        
        for (Section configSection : Config.configSections) {
            list.add(new ConfigElement<ConfigCategory>(Config.config.getCategory(configSection.toLowerCase()).setLanguageKey(prefix + configSection.id)));
        }
        
        return list;
    }
    
}
