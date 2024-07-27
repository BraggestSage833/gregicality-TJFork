package gregicadditions.machines.multi.advance;

import gregicadditions.GAMaterials;
import gregicadditions.GAUtility;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.capabilities.impl.GAMultiblockRecipeLogic;
import gregicadditions.capabilities.impl.GARecipeMapMultiblockController;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.mega.MetaTileEntityMegaBlastFurnace;
import gregicadditions.machines.multi.multiblockpart.MetaTileEntityMultiFluidHatch;
import gregicadditions.machines.multi.override.MetaTileEntityElectricBlastFurnace;
import gregicadditions.machines.multi.simple.LargeSimpleRecipeMapMultiblockController;
import gregicadditions.utils.GALog;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.recipes.builders.BlastRecipeBuilder;
import gregtech.api.recipes.recipeproperties.BlastTemperatureProperty;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.api.util.GTUtility;
import gregtech.api.util.InventoryUtils;
import gregtech.common.metatileentities.MetaTileEntities;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static gregicadditions.client.ClientHandler.*;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;

public class MetaTileEntityVolcanus extends MetaTileEntityElectricBlastFurnace {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS,
            MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS,
            GregicAdditionsCapabilities.MAINTENANCE_HATCH};


    private static final int DURATION_DECREASE_FACTOR = 20;

    private static final int ENERGY_DECREASE_FACTOR = 20;

    public MetaTileEntityVolcanus(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        this.recipeMapWorkable = new VolcanusRecipeLogic(this, ENERGY_DECREASE_FACTOR, DURATION_DECREASE_FACTOR, 100, 3);
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
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)).or(energyHatchPredicate()))
                .where('M', abilityPartPredicate(GregicAdditionsCapabilities.MUFFLER_HATCH))
                .where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
                .where('#', isAirPredicate())
                .build();
    }

    @Nonnull
    private static Predicate<BlockWorldState> energyHatchPredicate() {
        return tilePredicate((state, tile) -> tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[0].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[1].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[2].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[3].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[4].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[5].metaTileEntityId) || tile.metaTileEntityId.equals(MetaTileEntities.ENERGY_INPUT_HATCH[6].metaTileEntityId));
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



        @Override
        protected Recipe findRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs) {
            Recipe recipe = super.findRecipe(maxVoltage, inputs, fluidInputs);
            int currentTemp = ((MetaTileEntityVolcanus) metaTileEntity).getBlastFurnaceTemperature();
            if (recipe != null && recipe.getRecipePropertyStorage().getRecipePropertyValue(BlastTemperatureProperty.getInstance(), 0) <= currentTemp)
                return createRecipe(maxVoltage, inputs, fluidInputs, recipe);
            return null;
        }


        public VolcanusRecipeLogic(RecipeMapMultiblockController tileEntity, int EUtPercentage, int durationPercentage, int chancePercentage, int stack) {
            super(tileEntity, EUtPercentage, durationPercentage, chancePercentage, stack);
        }

        @Override
        protected boolean drawEnergy(int recipeEUt) {
            int drain = (int) Math.pow(2, getOverclockingTier(getMaxVoltage()));
            long resultEnergy = this.getEnergyStored() - (long) recipeEUt;
            Optional<IFluidTank> fluidTank =
                    getInputFluidInventory().getFluidTanks().stream()
                            .filter(iFluidTank -> iFluidTank.getFluid() != null)
                            .filter(iFluidTank -> iFluidTank.getFluid().isFluidEqual(GAMaterials.Pyrotheum.getFluid(drain)))
                            .findFirst();
            if (fluidTank.isPresent()) {
                IFluidTank tank = fluidTank.get();
                if (resultEnergy >= 0L && resultEnergy <= this.getEnergyCapacity() && tank.getCapacity() > 1) {
                    tank.drain(drain, true);
                    this.getEnergyContainer().changeEnergy(-recipeEUt);
                    return true;
                } else {
                    return false;
                }
            }
            return false;
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

            if (matchingRecipe.getInputs().size() != 0) {
                this.findIngredients(countIngredients, inputs);
                minMultiplier = Math.min(maxItemsLimit, this.getMinRatioItem(countIngredients, matchingRecipe, maxItemsLimit));
            }

            Map<String, Integer> countFluid = new HashMap<>();
            if (matchingRecipe.getFluidInputs().size() != 0) {


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
                if (duration <= 0){
                    duration = 1;
                }
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
            boolean canFitOutputs = InventoryUtils.simulateItemStackMerge(totalOutputs, this.getOutputInventory());
            if (!canFitOutputs)
                return matchingRecipe;

            newRecipe.inputsIngredients(newRecipeInputs)
                    .fluidInputs(newFluidInputs)
                    .outputs(outputI)
                    .fluidOutputs(outputF)
                    .EUt((int) Math.max(1, EUt))
                    .duration(duration)
                    .blastFurnaceTemp(recipeTemp);

            return newRecipe.build().getResult();
        }
    }


}
