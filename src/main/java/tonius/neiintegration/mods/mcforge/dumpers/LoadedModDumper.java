package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.LinkedList;
import java.util.List;

import codechicken.nei.config.DataDumper;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class LoadedModDumper extends DataDumper {
    
    public LoadedModDumper() {
        super("tools.dump.neiintegration.loadedmod");
    }
    
    @Override
    public String[] header() {
        return new String[] { "ID", "Name", "Version", "Custom Container", "Disableable", "Dependencies" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        for (ModContainer mod : Loader.instance().getModList()) {
            list.add(new String[] { mod.getModId(), mod.getName(), mod.getVersion(), String.valueOf(!(mod instanceof FMLModContainer)), String.valueOf(mod.canBeDisabled()), mod.getDependencies().toString() });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
