package tonius.neiintegration.mods.harvestcraft;

import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.Utils;
import codechicken.nei.api.API;

import com.pam.harvestcraft.GuiQuern;
import com.pam.harvestcraft.QuernRecipes;

public class RecipeHandlerQuern extends RecipeHandlerHCBase {
    
    @Override
    public void prepare() {
        API.setGuiOffset(GuiQuern.class, 11, 13);
    }
    
    @Override
    protected String getRecipeSubID() {
        return "quern";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.quern.name", false);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiQuern.class;
    }
    
    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        return QuernRecipes.smelting().getSmeltingList();
    }
    
    @Override
    public ItemStack getFuelItem() {
        return new ItemStack(Blocks.stone_pressure_plate);
    }
    
}
