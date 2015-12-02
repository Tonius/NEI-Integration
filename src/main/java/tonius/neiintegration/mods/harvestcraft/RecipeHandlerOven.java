package tonius.neiintegration.mods.harvestcraft;

import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.Utils;

import com.pam.harvestcraft.GuiOven;
import com.pam.harvestcraft.ItemRegistry;
import com.pam.harvestcraft.OvenRecipes;

public class RecipeHandlerOven extends RecipeHandlerHCBase {
    
    @Override
    protected String getRecipeSubID() {
        return "oven";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.oven.name", false);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiOven.class;
    }
    
    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        return OvenRecipes.smelting().getSmeltingList();
    }
    
    @Override
    public ItemStack getFuelItem() {
        return new ItemStack(ItemRegistry.oliveoilItem);
    }
    
}
