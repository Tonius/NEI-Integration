package tonius.neiintegration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.lwjgl.input.Keyboard;

import codechicken.nei.NEIServerUtils;

public class Utils {
    
    public static String translate(String unlocalized, boolean prefix) {
        return StatCollector.translateToLocal((prefix ? "neiintegration." : "") + unlocalized);
    }
    
    public static String translate(String unlocalized) {
        return translate(unlocalized, true);
    }
    
    public static boolean areStacksSameTypeCraftingSafe(ItemStack stack1, ItemStack stack2) {
        if (stack1 != null && stack2 != null) {
            return NEIServerUtils.areStacksSameTypeCrafting(stack1, stack2);
        }
        return false;
    }
    
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
                    fluidStack = new FluidStack(fluid, 1000);
                }
            }
            return fluidStack;
        }
        return null;
    }
    
    public static boolean areFluidsSameType(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1 == null || fluidStack2 == null) {
            return false;
        }
        return fluidStack1.getFluid() == fluidStack2.getFluid();
    }
    
    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
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
