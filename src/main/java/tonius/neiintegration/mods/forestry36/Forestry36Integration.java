package tonius.neiintegration.mods.forestry36;

import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.Utils;

public class Forestry36Integration extends IntegrationBase {
    
    @Override
    public String getName() {
        return "Forestry 3.6";
    }
    
    @Override
    public boolean isValid() {
        return Utils.isModLoaded("Forestry", "[3.6.0,4.0.0)");
    }
    
    @Override
    public void loadConfig() {
        this.registerHandler(new RecipeHandlerShapedCustom());
        this.registerHandler(new RecipeHandlerBottler());
        this.registerHandler(new RecipeHandlerCarpenter());
        this.registerHandler(new RecipeHandlerCentrifuge());
        this.registerHandler(new RecipeHandlerFabricator());
        this.registerHandler(new RecipeHandlerFermenter());
        this.registerHandler(new RecipeHandlerMoistener());
        this.registerHandler(new RecipeHandlerSqueezer());
        this.registerHandler(new RecipeHandlerStill());
    }
    
}
