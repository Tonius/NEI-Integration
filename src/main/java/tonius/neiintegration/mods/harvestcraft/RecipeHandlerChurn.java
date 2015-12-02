package tonius.neiintegration.mods.harvestcraft;

import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.Utils;

import com.pam.harvestcraft.ChurnRecipes;
import com.pam.harvestcraft.GuiChurn;
import com.pam.harvestcraft.ItemRegistry;

public class RecipeHandlerChurn extends RecipeHandlerHCBase {
    
    @Override
    protected String getRecipeSubID() {
        return "churn";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.churn.name", false);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiChurn.class;
    }
    
    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        return ChurnRecipes.smelting().getSmeltingList();
    }
    
    @Override
    public ItemStack getFuelItem() {
        return new ItemStack(ItemRegistry.saltItem);
    }
    
}
