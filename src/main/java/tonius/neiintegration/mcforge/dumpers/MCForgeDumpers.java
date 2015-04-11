package tonius.neiintegration.mcforge.dumpers;

import codechicken.nei.api.API;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class MCForgeDumpers {
    
    public static void loadConfig() {
        API.addOption(new ChestLootDumper());
        API.addOption(new DimensionDumper());
        API.addOption(new FluidDumper());
        API.addOption(new FluidContainerDumper());
        API.addOption(new LoadedModDumper());
        API.addOption(new EntityDumper());
        API.addOption(new OreDictionaryDumper());
        API.addOption(new RecipeHandlerDumper());
        API.addOption(new TileEntityDumper());
        
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            API.addOption(new InventoryDumper());
        }
    }
    
}
