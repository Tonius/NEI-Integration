package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import codechicken.nei.config.DataDumper;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class ChestLootDumper extends DataDumper {
    
    public ChestLootDumper() {
        super("tools.dump.neiintegration.chestloot");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Chest Type", "ItemStack", "Item Display Name", "Item ID", "Weight" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        Map<String, ChestGenHooks> lootTables = ReflectionHelper.getPrivateValue(ChestGenHooks.class, null, "chestInfo");
        List<String> names = new ArrayList<String>();
        names.addAll(lootTables.keySet());
        Collections.sort(names);
        
        for (String name : names) {
            List<WeightedRandomChestContent> contents = ReflectionHelper.getPrivateValue(ChestGenHooks.class, lootTables.get(name), "contents");
            
            for (WeightedRandomChestContent w : contents) {
                String displayName;
                try {
                    displayName = w.theItemId.getDisplayName();
                } catch (Exception ex) {
                    displayName = "-";
                }
                list.add(new String[] { name, w.theItemId.toString(), displayName, Item.itemRegistry.getNameForObject(w.theItemId.getItem()), String.valueOf(w.itemWeight) });
            }
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
