package gregicadditions.recipes.impl.nuclear;

import gregicadditions.Gregicality;
import gregtech.api.gui.*;
import gregtech.api.gui.widgets.ProgressWidget;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

@MethodsReturnNonnullByDefault
public class HotCoolantRecipeMapCategory implements IRecipeCategory<GTHotCoolantRecipeWrapper> {

    private final HotCoolantRecipeMap recipeMap;
    private final IDrawable background;
    private final ModularUI modularUI;

    public HotCoolantRecipeMapCategory(HotCoolantRecipeMap recipeMap, IGuiHelper helper) {
        this.recipeMap = recipeMap;
        this.background = helper.createBlankDrawable(176, 110);
        this.modularUI = ModularUI.builder(GuiTextures.BLANK_BACKGROUND, this.background.getWidth(), this.background.getHeight())
                .widget(new ProgressWidget(() -> 0, 77, 22, 20, 20, GuiTextures.PROGRESS_BAR_ARROW, ProgressWidget.MoveType.HORIZONTAL)
                        .setUpdateOnClient(true))
                .build(new BlankUIHolder(), Minecraft.getMinecraft().player);
        this.modularUI.initWidgets();
    }

    @Override
    public String getUid() {
        return Gregicality.MODID + ":" + recipeMap.getUnlocalizedName();
    }

    @Override
    public String getTitle() {
        return recipeMap.getLocalizedName();
    }

    @Override
    public String getModName() {
        return Gregicality.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GTHotCoolantRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getFluidStacks().init(0, true, 52, 24, 16, 16,
                recipeWrapper.recipe.getRecipeFluid().amount, false, null);
        recipeLayout.getFluidStacks().init(1, false, 104, 24, 16, 16,
                recipeWrapper.recipe.getRecipeFluid().amount, false, null);
        recipeLayout.getFluidStacks().set(ingredients);
        this.modularUI.guiWidgets.values().stream()
                .filter(widget -> widget instanceof ProgressWidget)
                .forEach(widget -> ((ProgressWidget) widget).setMaxProgress(recipeWrapper.getRecipe().getDuration()));
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        for (Widget widget : this.modularUI.guiWidgets.values()) {
            widget.updateScreen();
            widget.drawInForeground(0, 0);
            widget.drawInBackground(0, 0, new IRenderContext() {
            });
        }
        GuiTextures.FLUID_SLOT.draw(51, 23, 18, 18);
        GuiTextures.FLUID_SLOT.draw(103, 23, 18, 18);
    }
}
