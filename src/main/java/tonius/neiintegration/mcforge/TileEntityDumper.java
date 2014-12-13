package tonius.neiintegration.mcforge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import codechicken.nei.config.DataDumper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class TileEntityDumper extends DataDumper {
    
    public TileEntityDumper() {
        super("tools.dump.neiintegration_tileentity");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Class Name", "Registered Name" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        try {
            Map<Class, String> classToNameMap = (HashMap<Class, String>) ReflectionHelper.getPrivateValue(TileEntity.class, null, "field_145853_j", "classToNameMap");
            List<Class> classes = new ArrayList<Class>();
            for (Class clazz : classToNameMap.keySet()) {
                classes.add(clazz);
            }
            Collections.sort(classes, new Comparator<Class>() {
                @Override
                public int compare(Class o1, Class o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Class clazz : classes) {
                list.add(new String[] { clazz.getName(), classToNameMap.get(clazz) });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
