package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.components.ConveyorCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.multi.CasingUtils;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import gregtech.api.capability.IEnergyContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gregicadditions.client.ClientHandler.STELLITE_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;
import static gregtech.api.recipes.RecipeMaps.CUTTER_RECIPES;
import static gregtech.api.recipes.RecipeMaps.LATHE_RECIPES;
import static gregtech.api.metatileentity.multiblock.MultiblockAbility.*;

public class TileEntityLargeCutting extends MultiRecipeMapMultiblockController {

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS, MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};


	public TileEntityLargeCutting(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, CUTTER_RECIPES, GAConfig.multis.largeCutting.euPercentage, GAConfig.multis.largeCutting.durationPercentage, GAConfig.multis.largeCutting.chancedBoostPercentage, GAConfig.multis.largeCutting.stack,
				new RecipeMap<?>[]{CUTTER_RECIPES, LATHE_RECIPES});
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new TileEntityLargeCutting(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("XXXXX", "XXXAX", "##XAX")
				.aisle("XXXCX", "XXXMX", "##XAX").setRepeatable(1, 8)
				.aisle("XXXXX", "XSXAX", "##XAX")
				.setAmountAtLeast('L', 12)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('#', (tile) -> true)
				.where('A', isAirPredicate())
				.where('M', motorPredicate())
				.where('C', conveyorPredicate())
				.build();
	}

	private static final IBlockState defaultCasingState = METAL_CASING_2.getState(MetalCasing2.CasingType.STELLITE);
	public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeCutting.casingMaterial, defaultCasingState);


	public IBlockState getCasingState() {
		return casingState;
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeCutting.casingMaterial, STELLITE_CASING);
	}

	@Override
	protected void formStructure(PatternMatchContext context) {
		super.formStructure(context);
		MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
		ConveyorCasing.CasingType conveyor = context.getOrDefault("Conveyor", ConveyorCasing.CasingType.CONVEYOR_LV);
		int min = Collections.min(Arrays.asList(motor.getTier(), conveyor.getTier()));

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

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
		tooltip.add(I18n.format("gregtech.multiblock.recipe", this.recipeMap.getLocalizedName()));
		super.addInformation(stack, player, tooltip, advanced);
	}

	@Override
	public OrientedOverlayRenderer getRecipeMapOverlay(int recipeMapIndex) {
		return (getRecipeMapIndex() == 0) ? Textures.CUTTER_OVERLAY : Textures.LATHE_OVERLAY;
	}

	@Nonnull
	@Override
	protected OrientedOverlayRenderer getFrontOverlay() {
		return (getRecipeMapIndex() == 0) ? Textures.CUTTER_OVERLAY : Textures.LATHE_OVERLAY;
	}
}
