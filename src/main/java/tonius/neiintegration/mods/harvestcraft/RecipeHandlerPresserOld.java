package tonius.neiintegration.mods.harvestcraft;

import java.awt.Point;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import tonius.neiintegration.Utils;
import codechicken.nei.api.API;

import com.pam.harvestcraft.GuiPamPresser;
import com.pam.harvestcraft.PresserRecipes;

public class RecipeHandlerPresserOld extends RecipeHandlerHCBase {
    
    private static PresserRecipes presserRecipesInstance;
    private static Method getSmeltingList;
    
    @Override
    public void prepare() {
        API.setGuiOffset(GuiPamPresser.class, 7, -18);
        
        try {
            Method smelting = PresserRecipes.class.getMethod("smelting");
            getSmeltingList = PresserRecipes.class.getMethod("getSmeltingList");
            presserRecipesInstance = (PresserRecipes) smelting.invoke(null);
        } catch (Exception e) {
        }
    }
    
    @Override
    protected String getRecipeSubID() {
        return "presserOld";
    }
    
    @Override
    public String getRecipeName() {
        return Utils.translate("tile.presser.name", false);
    }
    
    @Override
    public Point getInputStackPos() {
        return new Point(45, 22);
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiPamPresser.class;
    }
    
    @Override
    public Map<ItemStack, ItemStack> getRecipes() {
        if (getSmeltingList == null) {
            return new HashMap<ItemStack, ItemStack>();
        }
        
        try {
            return (Map<ItemStack, ItemStack>) getSmeltingList.invoke(presserRecipesInstance);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<ItemStack, ItemStack>();
        }
    }
    
}
