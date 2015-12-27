package tonius.neiintegration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.lwjgl.input.Keyboard;

import codechicken.nei.NEIServerUtils;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

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
            Block block = Block.getBlockFromItem(stack.getItem());
            
            if (stack.getItem() instanceof IFluidContainerItem) {
                fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
            }
            if (fluidStack == null) {
                fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
            }
            if (fluidStack == null && block instanceof IFluidBlock) {
                Fluid fluid = ((IFluidBlock) block).getFluid();
                if (fluid != null) {
                    fluidStack = new FluidStack(fluid, 1000);
                }
            }
            if (fluidStack == null && (block == Blocks.water || block == Blocks.flowing_water)) {
                fluidStack = new FluidStack(FluidRegistry.WATER, 1000);
            }
            if (fluidStack == null && (block == Blocks.lava || block == Blocks.flowing_lava)) {
                fluidStack = new FluidStack(FluidRegistry.LAVA, 1000);
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
    
    public static boolean isFluidBlock(ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return false;
        }
        
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block == null) {
            return false;
        }
        
        return block instanceof IFluidBlock || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.lava || block == Blocks.flowing_lava;
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
    
    public static boolean isModLoaded(String modid) {
        return Loader.isModLoaded(modid);
    }
    
    public static boolean isModLoaded(String modid, String versionRangeString) {
        if (!isModLoaded(modid)) {
            return false;
        }
        
        ModContainer mod = Loader.instance().getIndexedModList().get(modid);
        VersionRange versionRange = VersionParser.parseRange(versionRangeString);
        DefaultArtifactVersion required = new DefaultArtifactVersion(modid, versionRange);
        
        return required.containsVersion(mod.getProcessedVersion());
    }
    
}
