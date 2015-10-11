package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.EntityList;
import codechicken.nei.config.DataDumper;

public class EntityDumper extends DataDumper {
    
    public EntityDumper() {
        super("tools.dump.neiintegration.entity");
    }
    
    @Override
    public String[] header() {
        return new String[] { "ID", "Name", "Class Name" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        List<Integer> ids = new ArrayList<Integer>();
        ids.addAll(EntityList.IDtoClassMapping.keySet());
        Collections.sort(ids);
        
        for (int id : ids) {
            list.add(new String[] { String.valueOf(id), EntityList.getStringFromID(id), EntityList.getClassFromID(id).getName() });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
