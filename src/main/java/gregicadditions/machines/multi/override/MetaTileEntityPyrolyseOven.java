package gregicadditions.machines.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.GAUtility;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.capabilities.impl.GARecipeMapMultiblockController;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.machines.multi.simple.LargeSimpleRecipeMapMultiblockController;
import gregicadditions.utils.GALog;
import gregtech.api.capability.IMultipleTankHandler;
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
import gregtech.api.recipes.RecipeBuilder;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockMachineCasing;
import gregtech.common.blocks.BlockWireCoil;
import gregtech.common.blocks.MetaBlocks;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static gregtech.api.recipes.RecipeMaps.PYROLYSE_RECIPES;

public class MetaTileEntityPyrolyseOven extends GARecipeMapMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS,
            MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS,
            MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH
    };

    protected int heatingCoilLevel = 1;
    protected int heatingCoilDiscount = 1;
    private final Set<BlockPos> activeStates = new HashSet<>();

    public MetaTileEntityPyrolyseOven(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, PYROLYSE_RECIPES);
        this.recipeMapWorkable = new PyrolyzeOvenWorkable(this);
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
            int heatingCoilDiscount = coilType.getEnergyDiscount();
            int currentCoilDiscount = blockWorldState.getMatchContext().getOrPut("heatingCoilDiscount", heatingCoilDiscount);
            int heatingCoilLevel = coilType.ordinal() + 1;
            int currentCoilLevel = blockWorldState.getMatchContext().getOrPut("heatingCoilLevel", heatingCoilLevel);
            if (currentCoilDiscount == heatingCoilDiscount && heatingCoilLevel == currentCoilLevel) {
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
            int heatingCoilDiscount = coilType.getEnergyDiscount();
            int currentCoilDiscount = blockWorldState.getMatchContext().getOrPut("heatingCoilDiscount", heatingCoilDiscount);
            int heatingCoilLevel = coilType.ordinal() + 8;
            int currentCoilLevel = blockWorldState.getMatchContext().getOrPut("heatingCoilLevel", heatingCoilLevel);
            if (currentCoilDiscount == heatingCoilDiscount && heatingCoilLevel == currentCoilLevel) {
                if (blockWorldState.getWorld() != null)
                    this.activeStates.add(blockWorldState.getPos());
                return true;
            }
            return false;
        };
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityPyrolyseOven(metaTileEntityId);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.heatingCoilLevel = context.getOrDefault("heatingCoilLevel", 0);
        this.heatingCoilDiscount = 100 / context.getOrDefault("heatingCoilDiscount", 0);
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.heatingCoilLevel = 1;
        this.heatingCoilDiscount = 1;
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed()) {
            textList.add(new TextComponentTranslation("gregtech.multiblock.multi_furnace.heating_coil_level", heatingCoilLevel));
            textList.add(new TextComponentTranslation("gregtech.multiblock.universal.energy_usage", heatingCoilDiscount)
                    .setStyle(new Style().setColor(TextFormatting.AQUA)));
        }
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("CCC", "C#C", "CCC")
                .aisle("CCC", "C#C", "CCC")
                .aisle("XXX", "XSX", "XXX")
                .where('S', selfPredicate())
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('C', heatingCoilPredicate().or(heatingCoilPredicate2()))
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.VOLTAGE_CASINGS[0];
    }

    protected IBlockState getCasingState() {
        return MetaBlocks.MACHINE_CASING.getState(BlockMachineCasing.MachineCasingType.ULV);
    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return Textures.PYROLYSE_OVEN_OVERLAY;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtadditions.multiblock.pyrolyse_oven.tooltip.1"));
        tooltip.add(I18n.format("gtadditions.multiblock.pyrolyse_oven.tooltip.2"));
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

    protected class PyrolyzeOvenWorkable extends LargeSimpleRecipeMapMultiblockController.LargeSimpleMultiblockRecipeLogic {

        public PyrolyzeOvenWorkable(RecipeMapMultiblockController tileEntity) {
            super(tileEntity, 100 / heatingCoilDiscount, 100, 100, heatingCoilLevel);
        }

        @Override
        protected Recipe createRecipe(long maxVoltage, IItemHandlerModifiable inputs, IMultipleTankHandler fluidInputs, Recipe matchingRecipe) {
            int maxItemsLimit = heatingCoilLevel;
            int EUt;
            int duration;
            int currentTier = getOverclockingTier(maxVoltage);
            int tierNeeded = Math.max(1, GAUtility.getTierByVoltage(matchingRecipe.getEUt()));;
            int minMultiplier = Integer.MAX_VALUE;

            maxItemsLimit = Math.max(1, maxItemsLimit);
            if (maxItemsLimit == 1) {
                return matchingRecipe;
            }

            Set<ItemStack> countIngredients = new HashSet<>();
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
                GALog.logger.error("Cannot calculate ratio of items for large multiblocks");
                return null;
            }

            EUt = matchingRecipe.getEUt();
            duration = matchingRecipe.getDuration();

            int tierDiff = currentTier - tierNeeded;
            for (int i = 0; i < tierDiff; i++) {
                int attemptItemsLimit = heatingCoilLevel;
                attemptItemsLimit += tierDiff - i;
                attemptItemsLimit = Math.max(1, attemptItemsLimit);
                attemptItemsLimit = Math.min(minMultiplier, attemptItemsLimit);
                List<CountableIngredient> newRecipeInputs = new ArrayList<>();
                List<FluidStack> newFluidInputs = new ArrayList<>();
                List<ItemStack> outputI = new ArrayList<>();
                List<FluidStack> outputF = new ArrayList<>();
                this.multiplyInputsAndOutputs(newRecipeInputs, newFluidInputs, outputI, outputF, matchingRecipe, attemptItemsLimit);


                RecipeBuilder<?> newRecipe = recipeMap.recipeBuilder();
                copyChancedItemOutputs(newRecipe, matchingRecipe, attemptItemsLimit);

                // determine if there is enough room in the output to fit all of this
                // if there isn't, we can't process this recipe.
                List<ItemStack> totalOutputs = newRecipe.getChancedOutputs().stream().map(Recipe.ChanceEntry::getItemStack).collect(Collectors.toList());
                totalOutputs.addAll(outputI);

                newRecipe.inputsIngredients(newRecipeInputs)
                        .fluidInputs(newFluidInputs)
                        .outputs(outputI)
                        .fluidOutputs(outputF)
                        .EUt((int) Math.min(Integer.MAX_VALUE, Math.max(1, (long) EUt * heatingCoilDiscount / 100)))
                        .duration((int) Math.max(3, duration * (this.getDurationPercentage() / 100.0)));

                return newRecipe.build().getResult();
            }
            return matchingRecipe;
        }

        @Override
        protected void setActive(boolean active) {
            MetaTileEntityPyrolyseOven tileEntity = (MetaTileEntityPyrolyseOven) this.metaTileEntity;
            tileEntity.replaceCoilsAsActive(active);
            super.setActive(active);
        }
    }
}
