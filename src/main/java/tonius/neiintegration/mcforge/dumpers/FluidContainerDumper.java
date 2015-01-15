package tonius.neiintegration.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import codechicken.nei.config.DataDumper;

public class FluidContainerDumper extends DataDumper {
    
    public FluidContainerDumper() {
        super("tools.dump.neiintegration_fluidcontainer");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Fluid", "Amount", "Empty Container", "Empty Container Display Name", "Empty Container Item", "Filled Container", "Filled Container Display Name", "Filled Container Item" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        List<FluidContainerData> datas = new ArrayList<FluidContainerData>();
        datas.addAll(Arrays.asList(FluidContainerRegistry.getRegisteredFluidContainerData()));
        Collections.sort(datas, new Comparator<FluidContainerData>() {
            @Override
            public int compare(FluidContainerData d1, FluidContainerData d2) {
                return d1.fluid.getFluid().getName().compareTo(d2.fluid.getFluid().getName());
            }
        });
        
        for (FluidContainerData data : datas) {
            String emptyDisplayName;
            try {
                emptyDisplayName = data.emptyContainer.getDisplayName();
            } catch (Exception e) {
                emptyDisplayName = "-";
            }
            String filledDisplayName;
            try {
                filledDisplayName = data.filledContainer.getDisplayName();
            } catch (Exception e) {
                filledDisplayName = "-";
            }
            list.add(new String[] { data.fluid.getFluid().getName(), String.valueOf(data.fluid.amount), data.emptyContainer.toString(), emptyDisplayName, Item.itemRegistry.getNameForObject(data.emptyContainer.getItem()), data.filledContainer.toString(), filledDisplayName, Item.itemRegistry.getNameForObject(data.filledContainer.getItem()) });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
