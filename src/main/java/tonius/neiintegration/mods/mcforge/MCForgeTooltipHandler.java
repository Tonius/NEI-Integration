package tonius.neiintegration.mods.mcforge;

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
        if (Config.tooltipUnlocalizedName && (!Config.tooltipUnlocalizedNameShift || Utils.isShiftKeyDown()) && (!Config.tooltipUnlocalizedNameAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.unlocalizedName") + " " + evt.itemStack.getUnlocalizedName());
        }
        
        if (Config.tooltipInternalName && (!Config.tooltipInternalNameShift || Utils.isShiftKeyDown()) && (!Config.tooltipInternalNameAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.internalName") + " " + Item.itemRegistry.getNameForObject(evt.itemStack.getItem()));
        }
        
        if (Config.tooltipMaxStack && (!Config.tooltipMaxStackShift || Utils.isShiftKeyDown()) && (!Config.tooltipMaxStackAdvanced || evt.showAdvancedItemTooltips)) {
            evt.toolTip.add(EnumChatFormatting.DARK_GRAY + Utils.translate("tooltip.maxstack") + " " + String.valueOf(evt.itemStack.getMaxStackSize()));
        }
        
        if (Config.tooltipBurnTime && (!Config.tooltipBurnTimeShift || Utils.isShiftKeyDown()) && (!Config.tooltipBurnTimeAdvanced || evt.showAdvancedItemTooltips)) {
            int burnTime = TileEntityFurnace.getItemBurnTime(evt.itemStack);
            if (burnTime > 0) {
                evt.toolTip.add(Utils.translate("tooltip.burntime") + " " + burnTime + " " + Utils.translate("ticks"));
            }
        }
        
        if (Config.tooltipOreDictNames && (!Config.tooltipOreDictNamesShift || Utils.isShiftKeyDown()) && (!Config.tooltipOreDictNamesAdvanced || evt.showAdvancedItemTooltips)) {
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
        
        if (Config.tooltipFluidRegInfo && (!Config.tooltipFluidRegInfoShift || Utils.isShiftKeyDown()) && (!Config.tooltipFluidRegInfoAdvanced || evt.showAdvancedItemTooltips)) {
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
