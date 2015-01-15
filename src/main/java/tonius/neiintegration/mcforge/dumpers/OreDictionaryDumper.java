package tonius.neiintegration.mcforge.dumpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.config.DataDumper;

public class OreDictionaryDumper extends DataDumper {
    
    public OreDictionaryDumper() {
        super("tools.dump.neiintegration_oredict");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Ore Name", "ItemStack", "Item ID", "Display Name", "Wildcard" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        List<String> oreNames = Arrays.asList(OreDictionary.getOreNames());
        Collections.sort(oreNames);
        
        for (String oreName : oreNames) {
            List<ItemStack> ores = OreDictionary.getOres(oreName);
            String displayName;
            for (ItemStack ore : ores) {
                if (ore.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    displayName = "(wildcard)";
                } else {
                    try {
                        displayName = ore.getDisplayName();
                    } catch (Exception e) {
                        displayName = "-";
                    }
                }
                list.add(new String[] { oreName, ore.toString(), Item.itemRegistry.getNameForObject(ore.getItem()), displayName, String.valueOf(ore.getItemDamage() == OreDictionary.WILDCARD_VALUE) });
            }
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
