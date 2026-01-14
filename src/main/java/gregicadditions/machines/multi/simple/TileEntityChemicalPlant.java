package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.CountableIngredient;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockWireCoil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.block.Block;
import gregtech.common.blocks.BlockWireCoil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class TileEntityChemicalPlant extends MultiRecipeMapMultiblockController {

	private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS, MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};

	private int energyBonus;
	private final Set<BlockPos> activeStates = new HashSet<>();

	public TileEntityChemicalPlant(ResourceLocation metaTileEntityId) {
		super(metaTileEntityId,
				GARecipeMaps.LARGE_CHEMICAL_RECIPES,
				GAConfig.multis.chemicalPlant.euPercentage,
				GAConfig.multis.chemicalPlant.durationPercentage,
				GAConfig.multis.chemicalPlant.chancedBoostPercentage,
				GAConfig.multis.chemicalPlant.stack,
				new RecipeMap[]{GARecipeMaps.LARGE_CHEMICAL_RECIPES, GARecipeMaps.CHEMICAL_PLANT_RECIPES});
		this.recipeMapWorkable = new AdvancedChemicalReactorWorkableHandler(this);
	}

	@Override
	public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
		return new TileEntityChemicalPlant(metaTileEntityId);
	}

	@Override
	protected BlockPattern createStructurePattern() {
		return FactoryBlockPattern.start()
				.aisle("X###X", "XXXXX", "X###X", "XXXXX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("X###X", "XPPPX", "XMMMX", "XPPPX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("X###X", "SXXXX", "X###X", "XXXXX", "X###X")
				.setAmountAtLeast('L', 22)
				.where('S', selfPredicate())
				.where('L', statePredicate(getCasingState()))
				.where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
				.where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
				.where('P', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
				.where('#', (tile) -> true)
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
			int reactorCoilTemperature = coilType.getCoilTemperature();
			int currentTemperature = blockWorldState.getMatchContext().getOrPut("blastFurnaceTemperature", reactorCoilTemperature);
			if (currentTemperature == reactorCoilTemperature) {
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

	public IBlockState getCasingState() {
		return GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT);
	}

	@Override
	public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
		return ClientHandler.CHEMICALLY_INERT;
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
				energyBonus = 5;
				break;
			case 3600:
				energyBonus = 10;
				break;
			case 4500:
				energyBonus = 15;
				break;
			case 5400:
				energyBonus = 20;
				break;
			case 7200:
				energyBonus = 25;
				break;
			case 8600:
				energyBonus = 30;
				break;
			case 9600:
				energyBonus = 35;
				break;
			case 10700:
				energyBonus = 40;
				break;
			case 11200:
				energyBonus = 45;
				break;
			case 12600:
				energyBonus = 50;
				break;
			case 14200:
				energyBonus = 55;
				break;
			case 28400:
				energyBonus = 60;
				break;
			case 56800:
				energyBonus = 65;
				break;
			default:
				energyBonus = 0;
		}
	}

	@Override
	public void invalidateStructure() {
		super.invalidateStructure();
		this.energyBonus = 0;
	}

	public int getEnergyBonus() {
		return this.energyBonus;
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		data.setInteger("energyBonus", this.energyBonus);
		return data;
	}

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		this.energyBonus = data.getInteger("energyBonus");
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {
		super.writeInitialSyncData(buf);
		buf.writeInt(energyBonus);
	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {
		super.receiveInitialSyncData(buf);
		this.energyBonus = buf.readInt();
	}

	@Nonnull
	@Override
	protected OrientedOverlayRenderer getFrontOverlay() {
		return Textures.CHEMICAL_REACTOR_OVERLAY;
	}

	@Override
	public OrientedOverlayRenderer getRecipeMapOverlay(int recipeMapIndex) {
		return Textures.CHEMICAL_REACTOR_OVERLAY;
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		tooltip.add(I18n.format("gtadditions.multiblock.chemical_plant.tooltip.1"));
		tooltip.add(I18n.format("gtadditions.multiblock.large_chemical_reactor.tooltip.2"));
		tooltip.add(I18n.format("gtadditions.multiblock.large_chemical_reactor.tooltip.3"));
	}

	private void replaceCoilsAsActive(boolean isActive) {
		if (!GAConfig.Misc.activeCoils) return;
		for (BlockPos pos : this.activeStates) {
			IBlockState state = this.getWorld().getBlockState(pos);
			Block block = state.getBlock();
			if (block instanceof BlockWireCoil) {
				state = state.withProperty(BlockWireCoil.ACTIVE, isActive);
				this.getWorld().setBlockState(pos, state);
			} else if (block instanceof GAHeatingCoil) {
				state = state.withProperty(GAHeatingCoil.ACTIVE, isActive);
				this.getWorld().setBlockState(pos, state);
			}
		}
	}

	@Override
	public void onRemoval() {
		super.onRemoval();
		if (!this.getWorld().isRemote) {
			this.replaceCoilsAsActive(false);
		}
	}

	protected static class AdvancedChemicalReactorWorkableHandler extends MultiRecipeMapMultiblockRecipeLogic {

		public AdvancedChemicalReactorWorkableHandler(RecipeMapMultiblockController tileEntity) {
			super(tileEntity,
					GAConfig.multis.chemicalPlant.euPercentage,
					GAConfig.multis.chemicalPlant.durationPercentage,
					GAConfig.multis.chemicalPlant.chancedBoostPercentage,
					GAConfig.multis.chemicalPlant.stack,
					((TileEntityChemicalPlant) tileEntity).recipeMaps);
		}

		@Override
		protected int getMinRatioItem(Set<ItemStack> countIngredients, Recipe r, int maxItemsLimit) {
			if (((TileEntityChemicalPlant) metaTileEntity).getRecipeMapIndex() == 1)
				return 1;
			int minMultiplier = Integer.MAX_VALUE;
			for (CountableIngredient ci : r.getInputs()) {
				if (ci.getCount() == 0) {
					continue;
				}
				for (ItemStack wholeItemStack : countIngredients) {
					if (ci.getIngredient().apply(wholeItemStack)) {
						int ratio = Math.min(maxItemsLimit, wholeItemStack.getCount() / ci.getCount());
						if (ratio < minMultiplier) {
							minMultiplier = ratio;
						}
						break;
					}
				}
			}
			return minMultiplier;
		}

		@Override
		protected int getMinRatioFluid(Object2IntMap<String> countFluid, Recipe r, int maxItemsLimit) {
			if (((TileEntityChemicalPlant) metaTileEntity).getRecipeMapIndex() == 1)
				return 1;
			int minMultiplier = Integer.MAX_VALUE;
			for (FluidStack fs : r.getFluidInputs()) {
				if (fs.amount != 0) { // skip notConsumable fluids
					String name = fs.getFluid().getUnlocalizedName();
					int ratio = Math.min(maxItemsLimit, countFluid.getInt(name) / fs.amount);
					if (ratio < minMultiplier) {
						minMultiplier = ratio;
					}
				}
			}
			return minMultiplier;
		}
		@Override
		protected void setupRecipe(Recipe recipe) {
			TileEntityChemicalPlant metaTileEntity = (TileEntityChemicalPlant) getMetaTileEntity();
			int energyBonus = metaTileEntity.getEnergyBonus();

			int[] resultOverclock = calculateOverclock(recipe.getEUt(), recipe.getDuration());
			this.progressTime = 1;

			// apply energy bonus
			resultOverclock[0] -= (int) (resultOverclock[0] * energyBonus * 0.01f);

			setMaxProgress(resultOverclock[1]);

			this.recipeEUt = resultOverclock[0];
			this.fluidOutputs = GTUtility.copyFluidList(recipe.getFluidOutputs());
			int tier = getMachineTierForRecipe(recipe);
			this.itemOutputs = GTUtility.copyStackList(recipe.getResultItemOutputs(getOutputInventory().getSlots(), random, tier));
			if (this.wasActiveAndNeedsUpdate) {
				this.wasActiveAndNeedsUpdate = false;
			} else {
				this.setActive(true);
			}
		}

		@Override
		protected void setActive(boolean active) {
			TileEntityChemicalPlant tileEntity = (TileEntityChemicalPlant) this.metaTileEntity;
			tileEntity.replaceCoilsAsActive(active);
			super.setActive(active);
		}
	}
}
