package gregicadditions.machines.multi.advance;

import gregicadditions.GAMaterials;
import gregicadditions.GAUtility;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.GAFuelRecipeLogic;
import gregicadditions.machines.multi.GAFueledMultiblockController;
import gregicadditions.recipes.GARecipeMaps;
import gregicadditions.recipes.helper.HelperMethods;
import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FuelRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.machines.FuelRecipeMap;
import gregtech.api.recipes.recipes.FuelRecipe;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.unification.material.Materials;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static gregicadditions.client.ClientHandler.NITINOL_60_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;
import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class MetaTileEntityLargeRocketEngine extends GAFueledMultiblockController {


    public MetaTileEntityLargeRocketEngine(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.ROCKET_FUEL_RECIPES, GAValues.V[GAValues.LuV]);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityLargeRocketEngine(metaTileEntityId);
    }

    @Override
    protected FuelRecipeLogic createWorkable(long maxVoltage) {
        return new RocketEngineWorkableHandler(this, recipeMap, () -> energyContainer, () -> importFluidHandler, maxVoltage);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed() && !hasProblems()) {
            FluidStack oxygen = importFluidHandler.drain(GAMaterials.LiquidOxygen.getFluid(Integer.MAX_VALUE), false);
            FluidStack air = importFluidHandler.drain(Materials.Air.getFluid(Integer.MAX_VALUE), false);
            FluidStack fuelStack = ((RocketEngineWorkableHandler) workableHandler).getFuelStack();
            int oxygenNeededToBoost = ((RocketEngineWorkableHandler) workableHandler).getOxygenNeededToBoost();
            boolean isBoosted = ((RocketEngineWorkableHandler) workableHandler).isUsingOxygen();
            int oxygenAmount = oxygen == null ? 0 : oxygen.amount;
            int airAmount = air == null ? 0 : air.amount;
            int fuelAmount = fuelStack == null ? 0 : fuelStack.amount;

            if (fuelStack == null)
                textList.add(new TextComponentTranslation("gregtech.multiblock.large_rocket_engine.no_fuel").setStyle(new Style().setColor(TextFormatting.RED)));
            else
                textList.add(new TextComponentString(String.format("%s: %dmb", fuelStack.getLocalizedName(), fuelAmount)).setStyle(new Style().setColor(TextFormatting.GREEN)));

            textList.add(new TextComponentTranslation("gregtech.multiblock.universal.air_amount", airAmount).setStyle(new Style().setColor(TextFormatting.AQUA)));

            textList.add(new TextComponentTranslation("gregtech.multiblock.universal.liquid_oxygen_amount", oxygenAmount).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
            textList.add(new TextComponentTranslation("gregtech.multiblock.large_rocket_engine.oxygen_need", oxygenNeededToBoost).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
            if (isBoosted)
                textList.add(new TextComponentTranslation("gregtech.multiblock.large_rocket_engine.boost").setStyle(new Style().setColor(TextFormatting.GREEN)));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtadditions.multiblock.large_rocket_engine.tooltip.1"));
        tooltip.add(I18n.format("gtadditions.multiblock.large_rocket_engine.tooltip.2"));
        tooltip.add(I18n.format("gtadditions.multiblock.large_rocket_engine.tooltip.3"));
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start(LEFT, DOWN, BACK)
                .aisle("CCC", "CEC", "CCC")
                .aisle("CAC", "F#F", "CCC").setRepeatable(8)
                .aisle("KKK", "KSK", "KKK")
                .where('S', selfPredicate())
                .where('C', statePredicate(getCasingState()))
                .where('K', statePredicate(getCasingState()).or(abilityPartPredicate(GregicAdditionsCapabilities.MAINTENANCE_HATCH)))
                .where('E', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.OUTPUT_ENERGY)))
                .where('F', statePredicate(getCasingState()).or(abilityPartPredicate(MultiblockAbility.IMPORT_FLUIDS)))
                .where('A', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.ENGINE_INTAKE_CASING)))
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return NITINOL_60_CASING;
    }

    protected IBlockState getCasingState() {
        return METAL_CASING_1.getState(MetalCasing1.CasingType.NITINOL_60);
    }

    public static class RocketEngineWorkableHandler extends GAFuelRecipeLogic {

        private boolean isUsingoxygen = false;
        private int oxygenNeededToBoost;
        private static final int AIR_INTAKE_PER_SEC = 37500;
        private FluidStack fuelStack;

        public RocketEngineWorkableHandler(MetaTileEntity metaTileEntity, FuelRecipeMap recipeMap,
                                           Supplier<IEnergyContainer> energyContainer, Supplier<IMultipleTankHandler> fluidTank, long maxVoltage) {
            super(metaTileEntity, recipeMap, energyContainer, fluidTank, maxVoltage);
        }

        public FluidStack getFuelStack() {
            if (previousRecipe == null)
                return null;
            FluidStack fuelStack = previousRecipe.getRecipeFluid();
            return fluidTank.get().drain(new FluidStack(fuelStack.getFluid(), Integer.MAX_VALUE), false);
        }

        // Override to remove the "Boosted!" from the GUI when not running
        // Also resets oxygenNeededToBoost, just for the GUI really.
        @Override
        protected void setActive(boolean active) {
            if (!active && this.isActive()) {
                isUsingoxygen = false;
                oxygenNeededToBoost = 0;
            }
            super.setActive(active);
        }

        @Override
        protected boolean checkRecipe(FuelRecipe recipe) {
            return checkAir();
        }

        private boolean checkAir() {
            FluidStack airResult = fluidTank.get().drain(Materials.Air.getFluid(AIR_INTAKE_PER_SEC), false);
            return airResult != null && airResult.amount == AIR_INTAKE_PER_SEC;
        }

        private boolean checkBoost() {
            FluidStack oxygenStack = fluidTank.get().drain(GAMaterials.LiquidOxygen.getFluid(oxygenNeededToBoost), false);
            this.isUsingoxygen = oxygenStack != null && oxygenStack.amount >= oxygenNeededToBoost;
            return isUsingoxygen;
        }


        @Override
        protected int calculateFuelAmount(FuelRecipe recipe) {
            FluidStack rocketFuel = recipe.getRecipeFluid().copy();
            this.fuelStack = fluidTank.get().drain(rocketFuel, false);
            return  recipe.getRecipeFluid().amount * getVoltageMultiplier(maxVoltage * (isUsingOxygen() ? 10 : 4), recipe.getMinVoltage()) * (isUsingOxygen() ? 2 : 1);
        }



        @Override
        protected long startRecipe(FuelRecipe recipe, int fuelUsed, int recipeDuration) {
            long MaxVoltage = this.getMaxVoltage();


    // Drain tanks
    if (checkAir()) {
        fluidTank.get().drain(Materials.Air.getFluid(AIR_INTAKE_PER_SEC), true);
    } else return 0;

    // Check boosted status and drain if needed
            oxygenNeededToBoost = 4 * (int) Math.ceil(fuelUsed / 10.0);
    if (checkBoost()) {
        fluidTank.get().drain(GAMaterials.LiquidOxygen.getFluid(oxygenNeededToBoost), true);
        MaxVoltage *= 64;
    } else {
        MaxVoltage *= 32;
    }


    // Refresh our internal FluidStack
    fuelStack.amount -= fuelUsed;

    return MaxVoltage;
}


        public int getOxygenNeededToBoost() {
            return oxygenNeededToBoost;
        }

        public boolean isUsingOxygen() {
            return isUsingoxygen;
        }
    }
}

