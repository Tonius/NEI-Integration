package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import codechicken.nei.config.DataDumper;

public class InventoryDumper extends DataDumper {
    
    public InventoryDumper() {
        super("tools.dump.neiintegration.inventory");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Slot", "Display Name", "Amount", "Damage", "Max Damage", "Item ID", "Unlocalized Name", "Item Class", "NBT" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        this.addToList(list, Minecraft.getMinecraft().thePlayer.inventory.mainInventory, false);
        this.addToList(list, Minecraft.getMinecraft().thePlayer.inventory.armorInventory, true);
        
        return list;
    }
    
    private void addToList(List<String[]> list, ItemStack[] stacks, boolean isArmor) {
        for (int i = 0; i < stacks.length; i++) {
            ItemStack stack = stacks[i];
            if (stack == null || stack.getItem() == null) {
                continue;
            }
            
            String slot = String.valueOf(i + (isArmor ? 100 : 0));
            String displayName = stack.getDisplayName();
            String amount = String.valueOf(stack.stackSize);
            String damage = String.valueOf(stack.getItemDamage());
            String maxDamage = String.valueOf(stack.getMaxDamage());
            String itemID = Item.itemRegistry.getNameForObject(stack.getItem());
            String unlocalizedName = stack.getUnlocalizedName();
            String itemClass = stack.getItem().getClass().getName();
            String nbt = stack.stackTagCompound != null ? stack.stackTagCompound.toString() : "";
            
            list.add(new String[] { slot, displayName, amount, damage, maxDamage, itemID, unlocalizedName, itemClass, nbt });
        }
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
