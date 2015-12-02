package tonius.neiintegration.mods.harvestcraft;

import tonius.neiintegration.IntegrationBase;
import codechicken.nei.api.API;

import com.pam.harvestcraft.GuiChurn;
import com.pam.harvestcraft.GuiOven;
import com.pam.harvestcraft.GuiPamPresser;
import com.pam.harvestcraft.GuiQuern;

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
        this.registerHandler(new RecipeHandlerChurn());
        this.registerHandler(new RecipeHandlerOven());
        this.registerHandler(new RecipeHandlerPresser());
        this.registerHandler(new RecipeHandlerQuern());
        
        API.setGuiOffset(GuiChurn.class, 11, 13);
        API.setGuiOffset(GuiOven.class, 11, 13);
        API.setGuiOffset(GuiPamPresser.class, 7, -18);
        API.setGuiOffset(GuiQuern.class, 11, 13);
    }
}
