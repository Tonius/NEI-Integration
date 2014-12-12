package tonius.neiintegration.forge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import codechicken.nei.config.DataDumper;

public class FluidDumper extends DataDumper {
    
    public FluidDumper() {
        super("tools.dump.neiintegration_fluid");
    }
    
    @Override
    public String[] header() {
        return new String[] { "ID", "Name", "Temperature", "Luminosity", "Density", "Viscosity", "Block ID", "Block Class" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        List<Fluid> fluids = new ArrayList<Fluid>();
        for (Fluid f : FluidRegistry.getRegisteredFluids().values()) {
            fluids.add(f);
        }
        Collections.sort(fluids, new Comparator<Fluid>() {
            @Override
            public int compare(Fluid f1, Fluid f2) {
                return Integer.compare(f1.getID(), f2.getID());
            }
        });
        for (Fluid f : fluids) {
            list.add(new String[] { String.valueOf(f.getID()), f.getName(), String.valueOf(f.getTemperature()), String.valueOf(f.getLuminosity()), String.valueOf(f.getDensity()), String.valueOf(f.getViscosity()), f.getBlock() != null ? Block.blockRegistry.getNameForObject(f.getBlock()) : null, f.getBlock() != null ? f.getBlock().getClass().getName() : null });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
