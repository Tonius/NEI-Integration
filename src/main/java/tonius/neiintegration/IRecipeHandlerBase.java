package tonius.neiintegration;

import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.IUsageHandler;

public interface IRecipeHandlerBase extends ICraftingHandler, IUsageHandler {
    
    public void prepare();
    
}
