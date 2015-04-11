package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import net.minecraftforge.common.DimensionManager;
import codechicken.nei.config.DataDumper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class DimensionDumper extends DataDumper {
    
    public DimensionDumper() {
        super("tools.dump.neiintegration_dimension");
    }
    
    @Override
    public String[] header() {
        return new String[] { "ID", "Provider ID", "Provider Class Name" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        List<Integer> ids = new ArrayList<Integer>();
        ids.addAll(Arrays.asList(DimensionManager.getStaticDimensionIDs()));
        Collections.sort(ids);
        
        for (int id : ids) {
            int providerId = DimensionManager.getProviderType(id);
            Hashtable<Integer, Class> providers = ReflectionHelper.getPrivateValue(DimensionManager.class, null, "providers");
            Class providerClass = providers.get(providerId);
            list.add(new String[] { String.valueOf(id), String.valueOf(providerId), providerClass.getName() });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
