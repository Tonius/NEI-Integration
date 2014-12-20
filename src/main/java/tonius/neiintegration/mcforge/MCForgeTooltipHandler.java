package tonius.neiintegration.mcforge;

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
import tonius.neiintegration.Utils;
import tonius.neiintegration.config.Config;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MCForgeTooltipHandler {
    
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent evt) {
        if (Config.tooltip_unlocalizedName && (!Config.tooltip_unlocalizedNameShift || Utils.isShiftKeyDown()) && (!Config.tooltip_unlocalizedNameAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.unlocalizedName") + " " + evt.itemStack.getUnlocalizedName());
        }
        
        if (Config.tooltip_internalName && (!Config.tooltip_internalNameShift || Utils.isShiftKeyDown()) && (!Config.tooltip_internalNameAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.internalName") + " " + Item.itemRegistry.getNameForObject(evt.itemStack.getItem()));
        }
        
        if (Config.tooltip_maxStack && (!Config.tooltip_maxStackShift || Utils.isShiftKeyDown()) && (!Config.tooltip_maxStackAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.maxstack") + " " + String.valueOf(evt.itemStack.getMaxStackSize()));
        }
        
        if (Config.tooltip_burnTime && (!Config.tooltip_burnTimeShift || Utils.isShiftKeyDown()) && (!Config.tooltip_burnTimeAdvanced || evt.showAdvancedItemTooltips)) {
            int burnTime = TileEntityFurnace.getItemBurnTime(evt.itemStack);
            if (burnTime > 0) {
                evt.toolTip.add(Utils.translate("tooltip.burntime") + " " + burnTime + " " + Utils.translate("ticks"));
            }
        }
        
        if (Config.tooltip_oreDictNames && (!Config.tooltip_oreDictNamesShift || Utils.isShiftKeyDown()) && (!Config.tooltip_oreDictNamesAdvanced || evt.showAdvancedItemTooltips)) {
            List<String> names = new ArrayList<String>();
            for (int id : OreDictionary.getOreIDs(evt.itemStack)) {
                String name = OreDictionary.getOreName(id);
                if (!names.contains(name)) {
                    names.add("  " + name);
                } else {
                    names.add("  " + EnumChatFormatting.DARK_GRAY + name);
                }
            }
            Collections.sort(names);
            if (!names.isEmpty()) {
                evt.toolTip.add(Utils.translate("tooltip.oredict"));
                evt.toolTip.addAll(names);
            }
        }
        
        if (Config.tooltip_fluidRegInfo && (!Config.tooltip_fluidRegInfoShift || Utils.isShiftKeyDown()) && (!Config.tooltip_fluidRegInfoAdvanced || evt.showAdvancedItemTooltips)) {
            List<String> names = new ArrayList<String>();
            if (FluidContainerRegistry.isEmptyContainer(evt.itemStack)) {
                names.add("  " + Utils.translate("tooltip.fluidreg.empty"));
            } else {
                FluidStack fluid = Utils.getFluidStack(evt.itemStack);
                if (fluid != null) {
                    names.add("  " + fluid.getLocalizedName());
                    names.add("  " + fluid.amount + " mB");
                }
            }
            if (!names.isEmpty()) {
                evt.toolTip.add(Utils.translate("tooltip.fluidreg"));
                evt.toolTip.addAll(names);
            }
        }
    }
    
}
