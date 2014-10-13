package tonius.neiintegration;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientConfig;
import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;

public abstract class RecipeHandlerBase extends TemplateRecipeHandler {
    
    public void prepare() {
    }
    
    public abstract class CachedBaseRecipe extends CachedRecipe {
        
        public List<FluidTankElement> getFluidTanks() {
            List<FluidTankElement> tanks = new ArrayList<FluidTankElement>();
            FluidTankElement tank = this.getFluidTank();
            if (tank != null) {
                tanks.add(tank);
            }
            return tanks;
        }
        
        public FluidTankElement getFluidTank() {
            return null;
        }
        
    }
    
    public abstract String getRecipeID();
    
    public String getRecipeNameSub() {
        return null;
    }
    
    public void loadCraftingRecipes(FluidStack result) {
    }
    
    public void loadUsageRecipes(FluidStack ingredient) {
    }
    
    public void changeToGuiTexture() {
        GuiDraw.changeTexture(this.getGuiTexture());
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public void changeToOverlayTexture() {
        GuiDraw.changeTexture(NEIIntegration.RESOURCE_PREFIX + "textures/overlays.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);
        this.drawFluidTanks(recipe);
        if (recipe % this.recipiesPerPage() == 0 && this.getRecipeNameSub() != null) {
            GuiDraw.drawStringC(this.getRecipeNameSub(), 83, -2, 0x404040, false);
        }
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        try {
            if (outputId.equals("liquid") && results.length == 1 && results[0] instanceof FluidStack) {
                this.loadCraftingRecipes((FluidStack) results[0]);
            } else {
                super.loadCraftingRecipes(outputId, results);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        FluidStack fluid = getFluidStack(result);
        if (fluid != null) {
            this.loadCraftingRecipes(fluid);
        }
    }
    
    @Override
    public void loadUsageRecipes(String inputId, Object... ingredients) {
        try {
            if (inputId.equals("liquid") && ingredients.length == 1 && ingredients[0] instanceof FluidStack) {
                this.loadUsageRecipes((FluidStack) ingredients[0]);
            } else {
                super.loadUsageRecipes(inputId, ingredients);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        FluidStack fluid = getFluidStack(ingred);
        if (fluid != null) {
            this.loadUsageRecipes(fluid);
        }
    }
    
    @Override
    public List<String> handleTooltip(GuiRecipe guiRecipe, List<String> currenttip, int recipe) {
        super.handleTooltip(guiRecipe, currenttip, recipe);
        CachedBaseRecipe crecipe = (CachedBaseRecipe) this.arecipes.get(recipe);
        if (GuiContainerManager.shouldShowTooltip(guiRecipe)) {
            Point mouse = GuiDraw.getMousePosition();
            Point offset = guiRecipe.getRecipePosition(recipe);
            Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2 - offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);
            
            currenttip = this.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        }
        return currenttip;
    }
    
    @Override
    public List<String> handleItemTooltip(GuiRecipe guiRecipe, ItemStack itemStack, List<String> currenttip, int recipe) {
        super.handleItemTooltip(guiRecipe, itemStack, currenttip, recipe);
        CachedBaseRecipe crecipe = (CachedBaseRecipe) this.arecipes.get(recipe);
        Point mouse = GuiDraw.getMousePosition();
        Point offset = guiRecipe.getRecipePosition(recipe);
        Point relMouse = new Point(mouse.x - (guiRecipe.width - 176) / 2 - offset.x, mouse.y - (guiRecipe.height - 166) / 2 - offset.y);
        
        currenttip = this.provideItemTooltip(guiRecipe, itemStack, currenttip, crecipe, relMouse);
        return currenttip;
    }
    
    public List<String> provideTooltip(GuiRecipe guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        if (crecipe.getFluidTanks() != null) {
            for (FluidTankElement tank : crecipe.getFluidTanks()) {
                if (tank.position.contains(relMouse)) {
                    tank.handleTooltip(currenttip);
                }
            }
        }
        return currenttip;
    }
    
    public List<String> provideItemTooltip(GuiRecipe guiRecipe, ItemStack itemStack, List<String> currenttip, CachedBaseRecipe crecipe, Point relMouse) {
        for (PositionedStack stack : crecipe.getIngredients()) {
            if (stack instanceof PositionedStackAdv && ((PositionedStackAdv) stack).getRect().contains(relMouse)) {
                currenttip = ((PositionedStackAdv) stack).handleTooltip(guiRecipe, currenttip);
            }
        }
        for (PositionedStack stack : crecipe.getOtherStacks()) {
            if (stack instanceof PositionedStackAdv && ((PositionedStackAdv) stack).getRect().contains(relMouse)) {
                currenttip = ((PositionedStackAdv) stack).handleTooltip(guiRecipe, currenttip);
            }
        }
        PositionedStack stack = crecipe.getResult();
        if (stack instanceof PositionedStackAdv && ((PositionedStackAdv) stack).getRect().contains(relMouse)) {
            currenttip = ((PositionedStackAdv) stack).handleTooltip(guiRecipe, currenttip);
        }
        return currenttip;
    }
    
    @Override
    public boolean keyTyped(GuiRecipe gui, char keyChar, int keyCode, int recipe) {
        if (keyCode == NEIClientConfig.getKeyBinding("gui.recipe")) {
            if (this.transferFluidTank(gui, recipe, false)) {
                return true;
            }
        } else if (keyCode == NEIClientConfig.getKeyBinding("gui.usage")) {
            if (this.transferFluidTank(gui, recipe, true)) {
                return true;
            }
        }
        return super.keyTyped(gui, keyChar, keyCode, recipe);
    }
    
    @Override
    public boolean mouseClicked(GuiRecipe gui, int button, int recipe) {
        if (button == 0) {
            if (this.transferFluidTank(gui, recipe, false)) {
                return true;
            }
        } else if (button == 1) {
            if (this.transferFluidTank(gui, recipe, true)) {
                return true;
            }
        }
        return super.mouseClicked(gui, button, recipe);
    }
    
    protected boolean transferFluidTank(GuiRecipe guiRecipe, int recipe, boolean usage) {
        CachedBaseRecipe crecipe = (CachedBaseRecipe) this.arecipes.get(recipe);
        Point mousepos = GuiDraw.getMousePosition();
        Point offset = guiRecipe.getRecipePosition(recipe);
        Point relMouse = new Point(mousepos.x - (guiRecipe.width - 176) / 2 - offset.x, mousepos.y - (guiRecipe.height - 166) / 2 - offset.y);
        
        if (crecipe.getFluidTanks() != null) {
            for (FluidTankElement tank : crecipe.getFluidTanks()) {
                if (tank.position.contains(relMouse)) {
                    if (tank.fluid != null && tank.fluid.amount > 0) {
                        if (usage) {
                            if (!GuiUsageRecipe.openRecipeGui("liquid", new Object[] { tank.fluid })) {
                                return false;
                            }
                        } else {
                            if (!GuiCraftingRecipe.openRecipeGui("liquid", new Object[] { tank.fluid })) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public void drawFluidTanks(int recipe) {
        CachedBaseRecipe crecipe = (CachedBaseRecipe) this.arecipes.get(recipe);
        if (crecipe.getFluidTanks() != null) {
            for (FluidTankElement fluidTank : crecipe.getFluidTanks()) {
                fluidTank.draw();
            }
        }
    }
    
    public static List getSingleList(Object o) {
        List list = new ArrayList();
        list.add(o);
        return list;
    }
    
    public static FluidStack getFluidStack(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        Item item = stack.getItem();
        FluidStack fluidStack = null;
        if (item instanceof IFluidContainerItem) {
            fluidStack = ((IFluidContainerItem) item).getFluid(stack);
        }
        if (fluidStack == null) {
            fluidStack = FluidContainerRegistry.getFluidForFilledItem(stack);
        }
        if (fluidStack == null && Block.getBlockFromItem(stack.getItem()) instanceof IFluidBlock) {
            Fluid fluid = ((IFluidBlock) Block.getBlockFromItem(stack.getItem())).getFluid();
            if (fluid != null) {
                return new FluidStack(fluid, 1000);
            }
        }
        return fluidStack;
    }
    
    public static boolean areFluidsEqual(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1 == null || fluidStack2 == null) {
            return false;
        }
        return fluidStack1.isFluidEqual(fluidStack2);
    }
    
    public static class FluidTankElement {
        public Rectangle position;
        public FluidStack fluid;
        public int capacity;
        public boolean flowingTexture = false;
        public boolean showAmount = true;
        public boolean perTick = false;
        
        public FluidTankElement(Rectangle position, int capacity, FluidStack fluid) {
            this.position = position;
            this.capacity = capacity;
            this.fluid = fluid;
        }
        
        public List<String> handleTooltip(List<String> currenttip) {
            if (this.fluid == null || this.fluid.getFluid() == null || this.fluid.amount <= 0) {
                return currenttip;
            }
            currenttip.add(this.fluid.getLocalizedName());
            if (this.showAmount) {
                currenttip.add(EnumChatFormatting.GRAY.toString() + this.fluid.amount + (this.perTick ? " mB/t" : " mB"));
            }
            return currenttip;
        }
        
        public void draw() {
            if (this.fluid == null || this.fluid.getFluid() == null || this.fluid.amount <= 0) {
                return;
            }
            IIcon fluidIcon = null;
            if (this.flowingTexture && this.fluid.getFluid().getFlowingIcon() != null) {
                fluidIcon = this.fluid.getFluid().getFlowingIcon();
            } else if (this.fluid.getFluid().getStillIcon() != null) {
                fluidIcon = this.fluid.getFluid().getStillIcon();
            } else {
                return;
            }
            
            GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
            int color = this.fluid.getFluid().getColor(this.fluid);
            GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));
            GL11.glDisable(GL11.GL_BLEND);
            
            int amount = Math.max(Math.min(this.position.height, this.fluid.amount * this.position.height / this.capacity), 1);
            int posY = this.position.y + this.position.height - amount;
            
            for (int i = 0; i < this.position.width; i += 16) {
                for (int j = 0; j < amount; j += 16) {
                    int drawWidth = Math.min(this.position.width - i, 16);
                    int drawHeight = Math.min(amount - j, 16);
                    
                    int drawX = this.position.x + i;
                    int drawY = posY + j;
                    
                    double minU = fluidIcon.getMinU();
                    double maxU = fluidIcon.getMaxU();
                    double minV = fluidIcon.getMinV();
                    double maxV = fluidIcon.getMaxV();
                    
                    Tessellator tessellator = Tessellator.instance;
                    tessellator.startDrawingQuads();
                    tessellator.addVertexWithUV(drawX, drawY + drawHeight, 0, minU, minV + (maxV - minV) * drawHeight / 16F);
                    tessellator.addVertexWithUV(drawX + drawWidth, drawY + drawHeight, 0, minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F);
                    tessellator.addVertexWithUV(drawX + drawWidth, drawY, 0, minU + (maxU - minU) * drawWidth / 16F, minV);
                    tessellator.addVertexWithUV(drawX, drawY, 0, minU, minV);
                    tessellator.draw();
                }
            }
            
            GL11.glEnable(GL11.GL_BLEND);
        }
        
    }
    
}
