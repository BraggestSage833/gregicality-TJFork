package gregicadditions.machines.multi.advance;

import gregicadditions.GAMaterials;
import gregicadditions.GAUtility;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.override.MetaTileEntityElectricBlastFurnace;
import gregicadditions.machines.multi.simple.LargeSimpleRecipeMapMultiblockController;
import gregicadditions.utils.GALog;
import gregtech.api.GTValues;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.recipes.recipeproperties.BlastTemperatureProperty;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static gregicadditions.client.ClientHandler.HASTELLOY_N_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;

public class MetaTileEntityVolcanus extends MetaTileEntityElectricBlastFurnace {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS,
            MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS,
            GregicAdditionsCapabilities.MAINTENANCE_HATCH, MultiblockAbility.INPUT_ENERGY};


    private static final int DURATION_DECREASE_FACTOR = 20;

    private static final int ENERGY_DECREASE_FACTOR = 20;
    private FluidStack pyrotheum;

    public MetaTileEntityVolcanus(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        this.recipeMapWorkable = new VolcanusRecipeLogic(this, ENERGY_DECREASE_FACTOR, DURATION_DECREASE_FACTOR, 100, 4);
        reinitializeStructurePattern();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityVolcanus(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "CCC", "CCC", "XXX")
                .aisle("XXX", "C#C", "C#C", "XMX")
                .aisle("XSX", "CCC", "CCC", "XXX")
                .setAmountAtLeast('L', 8)
                .where('L', statePredicate(getCasingState()))
                .where('S', selfPredicate())
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('M', abilityPartPredicate(GregicAdditionsCapabilities.MUFFLER_HATCH))
                .where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
                .where('#', isAirPredicate())
                .build();
    }

    public IBlockState getCasingState() {
        return METAL_CASING_1.getState(MetalCasing1.CasingType.HASTELLOY_N);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return HASTELLOY_N_CASING;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.pyrotheum = GAMaterials.Pyrotheum.getFluid((int) Math.pow(2, GAUtility.getTierByVoltage(this.energyContainer.getInputVoltage())));
    }

    @Override
    public boolean checkRecipe(Recipe recipe, boolean consumeIfSuccess) {
        int recipeRequiredTemp = recipe.getRecipePropertyStorage().getRecipePropertyValue(BlastTemperatureProperty.getInstance(), 0);
        return this.blastFurnaceTemperature >= recipeRequiredTemp && super.checkRecipe(recipe, consumeIfSuccess);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.multiblock.volcanus.description"));
        tooltip.add(I18n.format("gregtech.multiblock.vol_cryo.description"));

    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return Textures.PRIMITIVE_BLAST_FURNACE_OVERLAY;
    }

    public class VolcanusRecipeLogic extends LargeSimpleRecipeMapMultiblockController.LargeSimpleMultiblockRecipeLogic {

        public VolcanusRecipeLogic(RecipeMapMultiblockController tileEntity, int EUtPercentage, int durationPercentage, int chancePercentage, int stack) {
            super(tileEntity, EUtPercentage, durationPercentage, chancePercentage, stack);
        }

        @Override
        protected long getMaxVoltage() {
            return Math.min(GTValues.V[7], super.getMaxVoltage());
        }

        @Override
        protected Recipe findRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, boolean useOptimizedRecipeLookUp) {
            Recipe recipe = super.findRecipe(maxVoltage, inputs, fluidInputs, useOptimizedRecipeLookUp);
            int currentTemp = ((MetaTileEntityVolcanus) metaTileEntity).getBlastFurnaceTemperature();
            if (recipe != null && recipe.getRecipePropertyStorage().getRecipePropertyValue(BlastTemperatureProperty.getInstance(), 0) <= currentTemp)
                return recipe;
            return null;
        }

        @Override
        protected boolean drawEnergy(int recipeEUt) {
            if (!pyrotheum.isFluidStackIdentical(this.getInputTank().drain(pyrotheum, false)))
                return false;
            this.getInputTank().drain(pyrotheum, true);
            return super.drawEnergy(recipeEUt);
        }

        @Override
        protected Recipe createRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, Recipe matchingRecipe) {
            long EUt;
            int duration;
            int minMultiplier = Integer.MAX_VALUE;
            int recipeTemp = matchingRecipe.getRecipePropertyStorage().getRecipePropertyValue(BlastTemperatureProperty.getInstance(), 0);
            int tier = getOverclockingTier(maxVoltage);
            int tierNeeded;
            int maxItemsLimit = this.getStack();
            Set<ItemStack> countIngredients = new HashSet<>();

            tierNeeded = Math.max(1, GAUtility.getTierByVoltage(matchingRecipe.getEUt()));
            maxItemsLimit *= tier - tierNeeded;
            maxItemsLimit = Math.max(1, maxItemsLimit);

            if (!matchingRecipe.getInputs().isEmpty()) {
                this.findIngredients(countIngredients, inputs);
                minMultiplier = Math.min(maxItemsLimit, this.getMinRatioItem(countIngredients, matchingRecipe, maxItemsLimit));
            }

            Object2IntMap<String> countFluid = new Object2IntOpenHashMap<>();
            if (!matchingRecipe.getFluidInputs().isEmpty()) {


                this.findFluid(countFluid, fluidInputs);
                minMultiplier = Math.min(minMultiplier, this.getMinRatioFluid(countFluid, matchingRecipe, maxItemsLimit));
            }

            if (minMultiplier == Integer.MAX_VALUE) {
                GALog.logger.error("Cannot calculate ratio of items for the mega blast furnace");
                return null;
            }

            EUt = matchingRecipe.getEUt();
            duration = matchingRecipe.getDuration();
            int currentTemp = ((MetaTileEntityVolcanus) this.metaTileEntity).getBlastFurnaceTemperature();

            // Get amount of 900Ks over the recipe temperature
            int bonusAmount = Math.max(0, (currentTemp - recipeTemp) / 900);

            // Apply EUt discount for every 900K above the base recipe temperature
            EUt *= Math.pow(0.95, bonusAmount);

            // Apply Super Overclocks for every 1800k above the base recipe temperature

            for (int i = bonusAmount; EUt <= GAValues.V[tier - 1] && duration >= 3 && i > 0; i--) {
                if (i % 2 == 0) {
                    EUt *= 4;
                    duration *= 0.25;
                }
            }

            // Apply Regular Overclocking
            while (duration >= 3 && EUt <= GAValues.V[tier - 1]) {
                EUt *= 4;
                duration /= 2.8;
            }
            if (duration <= 0) {
                duration = 1;
            }

            List<CountableIngredient> newRecipeInputs = new ArrayList<>();
            List<FluidStack> newFluidInputs = new ArrayList<>();
            List<ItemStack> outputI = new ArrayList<>();
            List<FluidStack> outputF = new ArrayList<>();
            this.multiplyInputsAndOutputs(newRecipeInputs, newFluidInputs, outputI, outputF, matchingRecipe, minMultiplier);

            BlastRecipeBuilder newRecipe = (BlastRecipeBuilder) this.recipeMap.recipeBuilder();
            copyChancedItemOutputs(newRecipe, matchingRecipe, minMultiplier);

            // determine if there is enough room in the output to fit all of this
            // if there isn't, we can't process this recipe.
            List<ItemStack> totalOutputs = newRecipe.getChancedOutputs().stream().map(Recipe.ChanceEntry::getItemStack).collect(Collectors.toList());
            totalOutputs.addAll(outputI);

            newRecipe.inputsIngredients(newRecipeInputs)
                    .fluidInputs(newFluidInputs)
                    .outputs(outputI)
                    .fluidOutputs(outputF)
                    .EUt((int) Math.max(1, EUt))
                    .duration(duration)
                    .blastFurnaceTemp(recipeTemp);

            return newRecipe.build().getResult();
        }

        @Override
        protected void setActive(boolean active) {
            MetaTileEntityElectricBlastFurnace tileEntity = (MetaTileEntityElectricBlastFurnace) this.metaTileEntity;
            tileEntity.replaceCoilsAsActive(active);
            super.setActive(active);
        }
    }


}
