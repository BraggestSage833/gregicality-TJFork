package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.CasingUtils;
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
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import gregtech.api.capability.IEnergyContainer;

import javax.annotation.Nonnull;

import static gregicadditions.client.ClientHandler.INCONEL_625_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;
import static gregtech.api.metatileentity.multiblock.MultiblockAbility.INPUT_ENERGY;

public class TileEntityLargeExtruder extends LargeSimpleRecipeMapMultiblockController {

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
			MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS,
			INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};


	public TileEntityLargeExtruder(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, RecipeMaps.EXTRUDER_RECIPES, GAConfig.multis.largeExtruder.euPercentage, GAConfig.multis.largeExtruder.durationPercentage, GAConfig.multis.largeExtruder.chancedBoostPercentage, GAConfig.multis.largeExtruder.stack);
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new TileEntityLargeExtruder(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("XXXX", "XXXX", "XXX#")
				.aisle("XXXX", "XCPX", "XXX#").setRepeatable(2, 6)
				.aisle("XXXX", "XSXX", "XXX#")
				.setAmountAtLeast('L', 9)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('C', statePredicate(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE)))
				.where('#', (tile) -> true)
				.where('P', pistonPredicate())
				.build();
	}

	private static final IBlockState defaultCasingState = METAL_CASING_1.getState(MetalCasing1.CasingType.INCONEL_625);
	public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeExtruder.casingMaterial, defaultCasingState);


	public IBlockState getCasingState() {
		return casingState;
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeExtruder.casingMaterial, INCONEL_625_CASING);
	}

	@Override
	protected void formStructure(PatternMatchContext context) {
		super.formStructure(context);
		PistonCasing.CasingType piston = context.getOrDefault("Piston", PistonCasing.CasingType.PISTON_LV);
		int min = piston.getTier();

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
		return Textures.EXTRUDER_OVERLAY;
	}
}
