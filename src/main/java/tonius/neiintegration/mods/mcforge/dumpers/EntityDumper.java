package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.entity.EntityList;
import codechicken.nei.config.DataDumper;

import com.google.common.collect.ListMultimap;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.EntityRegistry.EntityRegistration;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class EntityDumper extends DataDumper {
    
    private static final String GLOBAL = "(global)";
    
    public EntityDumper() {
        super("tools.dump.neiintegration.entity");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Mod", "ID", "Name", "Class Name" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        List<Integer> ids = new ArrayList<Integer>();
        ids.addAll(EntityList.IDtoClassMapping.keySet());
        Collections.sort(ids);
        
        for (int id : ids) {
            list.add(new String[] { GLOBAL, String.valueOf(id), EntityList.getStringFromID(id), EntityList.getClassFromID(id).getName() });
        }
        
        ListMultimap<ModContainer, EntityRegistration> modEntities = ReflectionHelper.getPrivateValue(EntityRegistry.class, EntityRegistry.instance(), "entityRegistrations");
        
        for (Entry<ModContainer, EntityRegistration> e : modEntities.entries()) {
            EntityRegistration er = e.getValue();
            list.add(new String[] { e.getKey().getModId(), String.valueOf(er.getModEntityId()), e.getKey().getModId() + "." + er.getEntityName(), er.getEntityClass().getName() });
        }
        
        Collections.sort(list, new Comparator<String[]>() {
            @Override
            public int compare(String[] s1, String[] s2) {
                if (s1[0].equals(GLOBAL) && !s1[0].equals(s2[0])) {
                    return -1;
                }
                int i = s1[0].compareTo(s2[0]);
                if (i != 0) {
                    return i;
                }
                return Integer.compare(Integer.valueOf(s1[1]), Integer.valueOf(s2[1]));
            }
        });
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
