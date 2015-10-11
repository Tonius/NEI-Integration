package tonius.neiintegration.mods.mcforge.dumpers;

import codechicken.nei.api.API;

public abstract class MCForgeDumpers {
    
    public static void loadConfig() {
        API.addOption(new LoadedModDumper());
        API.addOption(new ChestLootDumper());
        API.addOption(new DimensionDumper());
        API.addOption(new EntityDumper());
        API.addOption(new TileEntityDumper());
        
        API.addOption(new OreDictionaryDumper());
        API.addOption(new FluidDumper());
        API.addOption(new FluidContainerDumper());
        API.addOption(new RecipeHandlerDumper());
        
        API.addOption(new InventoryDumper());
    }
    
}
