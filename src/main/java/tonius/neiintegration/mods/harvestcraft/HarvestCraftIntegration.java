package tonius.neiintegration.mods.harvestcraft;

import tonius.neiintegration.IntegrationBase;
import codechicken.nei.api.API;

import com.pam.harvestcraft.GuiPamPresser;

import cpw.mods.fml.common.Loader;

public class HarvestCraftIntegration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Pam's HarvestCraft";
    }
    
    @Override
    public boolean isValid() {
        return Loader.isModLoaded("harvestcraft");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerApiary());
        this.registerHandler(new RecipeHandlerPresser());
        API.setGuiOffset(GuiPamPresser.class, 7, -18);
    }
}
