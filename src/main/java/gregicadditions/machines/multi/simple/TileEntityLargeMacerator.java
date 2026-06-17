package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.multi.CasingUtils;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import gregtech.api.capability.IEnergyContainer;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

import static gregicadditions.client.ClientHandler.STELLITE_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;
import static gregtech.api.metatileentity.multiblock.MultiblockAbility.INPUT_ENERGY;

public class TileEntityLargeMacerator extends LargeSimpleRecipeMapMultiblockController {

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS, INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};


	public TileEntityLargeMacerator(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, RecipeMaps.MACERATOR_RECIPES, GAConfig.multis.largeMacerator.euPercentage, GAConfig.multis.largeMacerator.durationPercentage, GAConfig.multis.largeMacerator.chancedBoostPercentage, GAConfig.multis.largeMacerator.stack);
		this.recipeMapWorkable = new LargeSimpleMultiblockRecipeLogic(this, EUtPercentage, durationPercentage, chancePercentage, stack, 64);
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new TileEntityLargeMacerator(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("XXX", "XXX","XXX","XXX", "XXX", "XXX")
				.aisle("XXX", "XMX","X#X","XPX", "X#X", "XXX")
				.aisle("XSX", "XXX","XXX","XXX", "XXX", "XXX")
				.setAmountAtLeast('L', 10)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('#', isAirPredicate())
				.where('M', motorPredicate())
				.where('P', pistonPredicate())
				.build();
	}

	private static final IBlockState defaultCasingState = METAL_CASING_2.getState(MetalCasing2.CasingType.STELLITE);
	public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeMacerator.casingMaterial, defaultCasingState);


	public IBlockState getCasingState() {
		return casingState;
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeMacerator.casingMaterial, STELLITE_CASING);
	}

	@Override
	protected void formStructure(PatternMatchContext context) {
		super.formStructure(context);
		MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
		PistonCasing.CasingType piston = context.getOrDefault("Piston", PistonCasing.CasingType.PISTON_LV);
		int min = Collections.min(Arrays.asList(motor.getTier(), piston.getTier()));

		if (min >= GAValues.MAX) {
			this.maxVoltage = this.getAbilities(INPUT_ENERGY).stream()
					.mapToLong(IEnergyContainer::getInputVoltage)
					.max()
					.orElse(0);
			long amps = this.getAbilities(INPUT_ENERGY).stream()
					.filter(energy -> energy.getInputVoltage() == this.maxVoltage)
					.mapToLong(IEnergyContainer::getInputAmperage)
					.sum();
			amps = Math.min(1024, amps);
			while (amps >= 4) {
				amps /= 4;
				this.maxVoltage *= 4;
			}
			if (this.maxVoltage >= Integer.MAX_VALUE)
				this.maxVoltage += this.maxVoltage / Integer.MAX_VALUE;
		} else this.maxVoltage = 8L << min * 2;

	}

	@Nonnull
	@Override
	protected OrientedOverlayRenderer getFrontOverlay() {
		return ClientHandler.PULVERIZER_OVERLAY;
	}
}
