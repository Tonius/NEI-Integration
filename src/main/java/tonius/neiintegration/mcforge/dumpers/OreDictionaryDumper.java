package tonius.neiintegration.mcforge.dumpers;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import codechicken.nei.api.ItemInfo;
import codechicken.nei.config.DataDumper;

public class OreDictionaryDumper extends DataDumper {
    
    public OreDictionaryDumper() {
        super("tools.dump.neiintegration_oredict");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Ore Name", "ItemStack", "Item ID", "Wildcard", "Mod" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        List<String> oreNames = Arrays.asList(OreDictionary.getOreNames());
        Collections.sort(oreNames);
        
        for (String oreName : oreNames) {
            List<ItemStack> ores = OreDictionary.getOres(oreName);
            for (ItemStack ore : ores) {
                list.add(new String[] { oreName, ore.toString(), Item.itemRegistry.getNameForObject(ore.getItem()), String.valueOf(ore.getItemDamage() == OreDictionary.WILDCARD_VALUE), ItemInfo.itemOwners.get(ore.getItem()) });
            }
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
