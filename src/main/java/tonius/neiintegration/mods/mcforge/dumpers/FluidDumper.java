package tonius.neiintegration.mods.mcforge.dumpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import codechicken.nei.config.DataDumper;

public class FluidDumper extends DataDumper {
    
    public FluidDumper() {
        super("tools.dump.neiintegration.fluid");
    }
    
    @Override
    public String[] header() {
        return new String[] { "ID", "Name", "Localized Name", "Temperature", "Luminosity", "Density", "Viscosity", "Placeable", "Block ID", "Block Class" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new LinkedList<String[]>();
        
        List<Fluid> fluids = new ArrayList<Fluid>();
        fluids.addAll(FluidRegistry.getRegisteredFluids().values());
        Collections.sort(fluids, new Comparator<Fluid>() {
            @Override
            public int compare(Fluid f1, Fluid f2) {
                return Integer.compare(f1.getID(), f2.getID());
            }
        });
        
        for (Fluid f : fluids) {
            list.add(new String[] { String.valueOf(f.getID()), f.getName(), f.getLocalizedName(new FluidStack(f, 1000)), String.valueOf(f.getTemperature()), String.valueOf(f.getLuminosity()), String.valueOf(f.getDensity()), String.valueOf(f.getViscosity()), String.valueOf(f.canBePlacedInWorld()), f.getBlock() != null ? Block.blockRegistry.getNameForObject(f.getBlock()) : null, f.getBlock() != null ? f.getBlock().getClass().getName() : null });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
