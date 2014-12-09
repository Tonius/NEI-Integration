package tonius.neiintegration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemTooltipHandler {
    
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent evt) {
        evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Item.itemRegistry.getNameForObject(evt.itemStack.getItem()));
        
        int burnTime = TileEntityFurnace.getItemBurnTime(evt.itemStack);
        if (burnTime > 0) {
            evt.toolTip.add(Utils.translate("tooltip.burntime") + " " + burnTime + " " + Utils.translate("ticks"));
        }
        
        List<String> names = new ArrayList<String>();
        for (int id : OreDictionary.getOreIDs(evt.itemStack)) {
            String name = OreDictionary.getOreName(id);
            if (!names.contains(name)) {
                names.add("  " + EnumChatFormatting.GRAY + name);
            } else {
                names.add("  " + EnumChatFormatting.DARK_GRAY + name);
            }
        }
        Collections.sort(names);
        if (!names.isEmpty()) {
            evt.toolTip.add(EnumChatFormatting.GRAY + Utils.translate("tooltip.oredict"));
            evt.toolTip.addAll(names);
        }
        
        names = new ArrayList<String>();
        if (FluidContainerRegistry.isEmptyContainer(evt.itemStack)) {
            names.add(" Empty Container");
        } else {
            FluidStack fluid = Utils.getFluidStack(evt.itemStack);
            if (fluid != null) {
                names.add("  " + fluid.getLocalizedName());
                names.add("  " + fluid.amount + " mB");
            }
        }
        if (!names.isEmpty()) {
            evt.toolTip.add(EnumChatFormatting.GRAY + Utils.translate("tooltip.fluidreg"));
            evt.toolTip.addAll(names);
        }
    }
    
}
