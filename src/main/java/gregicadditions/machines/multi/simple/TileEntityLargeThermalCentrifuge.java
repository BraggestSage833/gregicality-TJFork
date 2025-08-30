package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.multi.CasingUtils;
import gregicadditions.machines.multi.mega.MetaTileEntityMegaBlastFurnace;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static gregicadditions.client.ClientHandler.RED_STEEL_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;

public class TileEntityLargeThermalCentrifuge extends LargeSimpleRecipeMapMultiblockController {

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};

	private int speedBonus;
	private final Set<BlockPos> activeStates = new HashSet<>();

	public TileEntityLargeThermalCentrifuge(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId, RecipeMaps.THERMAL_CENTRIFUGE_RECIPES, GAConfig.multis.largeThermalCentrifuge.euPercentage, GAConfig.multis.largeThermalCentrifuge.durationPercentage, GAConfig.multis.largeThermalCentrifuge.chancedBoostPercentage, GAConfig.multis.largeThermalCentrifuge.stack, true, true, true);
		this.recipeMapWorkable = new LargeThermalCentrifugeWorkableHandler(this);
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new TileEntityLargeThermalCentrifuge(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("AXXXA", "AXHXA", "AXXXA", "AAAAA")
				.aisle("XXXXX", "XCCCX", "X###X", "AXXXA")
				.aisle("XXMXX", "HCPCH", "X#P#X", "AXHXA")
				.aisle("XXXXX", "XCCCX", "X###X", "AXXXA")
				.aisle("AXXXA", "AXSXA", "AXXXA", "AAAAA")
				.setAmountAtLeast('L', 12)
				.setAmountAtLeast('G', 3)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('G', statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING)))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
				.where('P', statePredicate(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TITANIUM_PIPE)))
				.where('H', abilityPartPredicate(GregicAdditionsCapabilities.MUFFLER_HATCH).or(statePredicate(MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))))
				.where('#', isAirPredicate())
				.where('A', (tile) -> true)
				.where('M', motorPredicate())
				.build();
	}

	public Predicate<BlockWorldState> heatingCoilPredicate() {
		return blockWorldState -> {
			IBlockState blockState = blockWorldState.getBlockState();
			if (!(blockState.getBlock() instanceof BlockWireCoil))
				return false;
			BlockWireCoil blockWireCoil = (BlockWireCoil) blockState.getBlock();
			BlockWireCoil.CoilType coilType = blockWireCoil.getState(blockState);
			if (Arrays.asList(GAConfig.multis.heatingCoils.gtceHeatingCoilsBlacklist).contains(coilType.getName()))
				return false;
			int blastFurnaceTemperature = coilType.getCoilTemperature();
			int currentTemperature = blockWorldState.getMatchContext().getOrPut("blastFurnaceTemperature", blastFurnaceTemperature);
			BlockWireCoil.CoilType currentCoilType = blockWorldState.getMatchContext().getOrPut("coilType", coilType);
			if (currentTemperature == blastFurnaceTemperature && coilType.equals(currentCoilType)) {
				if (blockWorldState.getWorld() != null)
					this.activeStates.add(blockWorldState.getPos());
				return true;
			}
			return false;
		};
	}
	public Predicate<BlockWorldState> heatingCoilPredicate2() {
		return blockWorldState -> {
			IBlockState blockState = blockWorldState.getBlockState();
			if (!(blockState.getBlock() instanceof GAHeatingCoil))
				return false;
			GAHeatingCoil blockWireCoil = (GAHeatingCoil) blockState.getBlock();
			GAHeatingCoil.CoilType coilType = blockWireCoil.getState(blockState);
			if (Arrays.asList(GAConfig.multis.heatingCoils.gregicalityheatingCoilsBlacklist).contains(coilType.getName()))
				return false;
			int blastFurnaceTemperature = coilType.getCoilTemperature();
			int currentTemperature = blockWorldState.getMatchContext().getOrPut("blastFurnaceTemperature", blastFurnaceTemperature);
			GAHeatingCoil.CoilType currentCoilType = blockWorldState.getMatchContext().getOrPut("gaCoilType", coilType);
			if (currentTemperature == blastFurnaceTemperature && coilType.equals(currentCoilType)) {
				if (blockWorldState.getWorld() != null)
					this.activeStates.add(blockWorldState.getPos());
				return true;
			}
			return false;
		};
	}

	private static final IBlockState defaultCasingState = METAL_CASING_2.getState(MetalCasing2.CasingType.RED_STEEL);
	public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeThermalCentrifuge.casingMaterial, defaultCasingState);


	public IBlockState getCasingState() {
		return casingState;
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeThermalCentrifuge.casingMaterial, RED_STEEL_CASING);
	}

	@Override
	protected void formStructure(PatternMatchContext context) {
		super.formStructure(context);
		MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
		int min = motor.getTier();
		maxVoltage = (long) (Math.pow(4, min) * 8);

		int temperature = context.getOrDefault("blastFurnaceTemperature", 0);

		switch (temperature){

			case 2700:
				speedBonus = 5;
				break;
			case 3600:
				speedBonus = 10;
				break;
			case 4500:
				speedBonus = 15;
				break;
			case 5400:
				speedBonus = 20;
				break;
			case 7200:
				speedBonus = 25;
				break;
			case 8600:
				speedBonus = 30;
				break;
			case 9600:
				speedBonus = 35;
				break;
			case 10700:
				speedBonus = 40;
				break;
			case 11200:
				speedBonus = 45;
				break;
			case 12600:
				speedBonus = 50;
				break;
			case 14200:
				speedBonus = 55;
				break;
			case 28400:
				speedBonus = 60;
				break;
			case 56800:
				speedBonus = 65;
				break;
			default:
				speedBonus = 0;
		}
	}

	@Nonnull
	@Override
	protected OrientedOverlayRenderer getFrontOverlay() {
		return Textures.THERMAL_CENTRIFUGE_OVERLAY;
	}

	protected int getSpeedBonus() {
		return this.speedBonus;
	}

	@Override
	protected void addDisplayText(List<ITextComponent> textList) {
		super.addDisplayText(textList);
		if (isStructureFormed() && !hasProblems()) {
			textList.add(new TextComponentTranslation("gregtech.multiblock.universal.speed_increase", this.speedBonus).setStyle(new Style().setColor(TextFormatting.AQUA)));
		}
	}
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add(I18n.format("gtadditions.multiblock.large_ThermalCentrifuge.tooltip.1"));
		tooltip.add(I18n.format("gtadditions.multiblock.large_ThermalCentrifuge.tooltip.2"));
	}

	private void replaceCoilsAsActive(boolean isActive) {
		this.activeStates.forEach(pos -> {
			IBlockState state = this.getWorld().getBlockState(pos);
			Block block = state.getBlock();
			if (block instanceof BlockWireCoil) {
				state = state.withProperty(BlockWireCoil.ACTIVE, isActive);
				this.getWorld().setBlockState(pos, state);
			} else if (block instanceof GAHeatingCoil) {
				state = state.withProperty(GAHeatingCoil.ACTIVE, isActive);
				this.getWorld().setBlockState(pos, state);
			}
		});
	}

	@Override
	public void onRemoval() {
		super.onRemoval();
		if (!this.getWorld().isRemote) {
			this.replaceCoilsAsActive(false);
		}
	}


	private static class LargeThermalCentrifugeWorkableHandler extends LargeSimpleMultiblockRecipeLogic {

		public LargeThermalCentrifugeWorkableHandler(RecipeMapMultiblockController tileEntity) {
			super(tileEntity, GAConfig.multis.largeThermalCentrifuge.euPercentage, GAConfig.multis.largeThermalCentrifuge.durationPercentage, GAConfig.multis.largeThermalCentrifuge.chancedBoostPercentage, GAConfig.multis.largeThermalCentrifuge.stack);
		}

		@Override
		protected void setupRecipe(Recipe recipe) {
			long maxVoltage = getMaxVoltage();
			if (metaTileEntity instanceof TileEntityLargeThermalCentrifuge)
				maxVoltage = ((TileEntityLargeThermalCentrifuge) metaTileEntity).maxVoltage;
			int[] resultOverclock = calculateOverclock(recipe.getEUt(), maxVoltage, recipe.getDuration());
			this.progressTime = 1;

			TileEntityLargeThermalCentrifuge metaTileEntity = (TileEntityLargeThermalCentrifuge) getMetaTileEntity();
			int speedBonus = metaTileEntity.getSpeedBonus();

			// apply speed bonus
			resultOverclock[1] -= (int) (resultOverclock[0] * speedBonus * 0.01f);

			setMaxProgress(resultOverclock[1]);
			this.recipeEUt = resultOverclock[0];
			this.fluidOutputs = GTUtility.copyFluidList(recipe.getFluidOutputs());
			int tier = getMachineTierForRecipe(recipe);
			this.itemOutputs = GTUtility.copyStackList(recipe.getResultItemOutputs(Integer.MAX_VALUE, random, tier));
			if (this.wasActiveAndNeedsUpdate) {
				this.wasActiveAndNeedsUpdate = false;
			} else {
				this.setActive(true);
			}
		}

		@Override
		protected void setActive(boolean active) {
			TileEntityLargeThermalCentrifuge tileEntity = (TileEntityLargeThermalCentrifuge) this.metaTileEntity;
			tileEntity.replaceCoilsAsActive(active);
			super.setActive(active);
		}
	}

}
