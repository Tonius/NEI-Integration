package tonius.neiintegration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

public class Utils {
    
    public static List<ItemStack> getItemVariations(ItemStack base) {
        List<ItemStack> variations = new ArrayList<ItemStack>();
        base.getItem().getSubItems(base.getItem(), null, variations);
        Iterator<ItemStack> itr = variations.iterator();
        ItemStack stack;
        while (itr.hasNext()) {
            stack = itr.next();
            if (!base.isItemEqual(stack) || !stack.hasTagCompound()) {
                itr.remove();
            }
        }
        if (variations.isEmpty()) {
            return Collections.singletonList(base);
        }
        return variations;
    }
    
    public static FluidStack getFluidStack(ItemStack stack) {
        if (stack != null) {
            FluidStack fluidStack = null;
            if (stack.getItem() instanceof IFluidContainerItem) {
                fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
            }
            if (fluidStack == null) {
                fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
            }
            if (fluidStack == null && Block.getBlockFromItem(stack.getItem()) instanceof IFluidBlock) {
                Fluid fluid = ((IFluidBlock) Block.getBlockFromItem(stack.getItem())).getFluid();
                if (fluid != null) {
                    return new FluidStack(fluid, 1000);
                }
            }
            return fluidStack;
        }
        return null;
    }
    
    public static boolean areFluidStacksEqual(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1 == null || fluidStack2 == null) {
            return false;
        }
        return fluidStack1.isFluidEqual(fluidStack2);
    }
    
    public static boolean areFluidsSameType(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1 == null || fluidStack2 == null) {
            return false;
        }
        return fluidStack1.getFluid() == fluidStack2.getFluid();
    }
    
    public static Class getClass(String name) {
        try {
            Class clazz = Class.forName(name);
            return clazz;
        } catch (Exception e) {
            NEIIntegration.log.error("Failed to find class " + name);
        }
        return null;
    }
    
}
