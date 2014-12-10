package tonius.neiintegration.forge;

import java.util.LinkedList;

import codechicken.nei.config.DataDumper;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;

public class RecipeHandlerDumper extends DataDumper {
    
    public RecipeHandlerDumper() {
        super("tools.dump.neiintegration_recipehandler");
    }
    
    @Override
    public String[] header() {
        return new String[] { "Class Name", "Type" };
    }
    
    @Override
    public Iterable<String[]> dump(int mode) {
        LinkedList<String[]> list = new LinkedList<String[]>();
        
        for (ICraftingHandler crafting : GuiCraftingRecipe.craftinghandlers) {
            list.add(new String[] { crafting.getClass().getName(), "crafting" });
        }
        for (IUsageHandler usage : GuiUsageRecipe.usagehandlers) {
            list.add(new String[] { usage.getClass().getName(), "usage" });
        }
        
        return list;
    }
    
    @Override
    public int modeCount() {
        return 1;
    }
    
}
