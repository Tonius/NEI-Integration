package tonius.neiintegration.railcraft;

import java.awt.Rectangle;

import net.minecraft.client.gui.inventory.GuiContainer;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;
import codechicken.lib.gui.GuiDraw;

public abstract class RecipeHandlerRollingMachine extends RecipeHandlerBase {
    
    private static Class<? extends GuiContainer> guiClass;
    
    @Override
    public void prepare() {
        guiClass = Utils.getClass("mods.railcraft.client.gui.GuiRollingMachine");
    }
    
    @Override
    public String getRecipeID() {
        return "railcraft.rollingmachine";
    }
    
    @Override
    public String getRecipeName() {
        return "Rolling Machine";
    }
    
    @Override
    public String getGuiTexture() {
        return "railcraft:textures/gui/gui_rolling.png";
    }
    
    @Override
    public void loadTransferRects() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 39, 24, 10), this.getRecipeID()));
    }
    
    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }
    
    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 2, 5, 11, 150, 65);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.changeToGuiTexture();
        this.drawProgressBar(84, 36, 176, 0, 25, 12, 60, 0);
    }
    
}
