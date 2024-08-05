package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.capabilities.impl.GAMultiblockRecipeLogic;
import gregicadditions.capabilities.impl.GARecipeMapMultiblockController;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.GAHeatingCoil;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
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
import gregtech.api.recipes.Recipe;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockWireCoil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TileEntityLargeChemicalReactor extends GARecipeMapMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS,
            MultiblockAbility.EXPORT_FLUIDS, MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};

    private int energyBonus;

    public TileEntityLargeChemicalReactor(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.LARGE_CHEMICAL_RECIPES, false, true, true);
        this.recipeMapWorkable = new LargeChemicalReactorWorkableHandler(this);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new TileEntityLargeChemicalReactor(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XCX", "XXX")
                .aisle("XCX", "CPC", "XCX")
                .aisle("XXX", "XSX", "XXX")
                .setAmountAtLeast('L', 8)
                .setAmountLimit('K', 1, 1)
                .where('S', selfPredicate())
                .where('L', statePredicate(getCasingState()))
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('C', heatingCoilPredicate().or(heatingCoilPredicate2()).or(statePredicate(getCasingState())).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('K', heatingCoilPredicate().or(heatingCoilPredicate2()))
                .where('P', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return ClientHandler.CHEMICALLY_INERT;
    }

    public IBlockState getCasingState() {
        return GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT);
    }

    public static Predicate<BlockWorldState> heatingCoilPredicate() {
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
            return currentTemperature == reactorCoilTemperature;
        };
    }

    public static Predicate<BlockWorldState> heatingCoilPredicate2() {
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

            return currentTemperature == blastFurnaceTemperature && coilType.equals(currentCoilType);
        };
    }
    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        super.addDisplayText(textList);
        if (isStructureFormed() && !hasProblems())
            textList.add(new TextComponentTranslation("gregtech.multiblock.universal.energy_usage", 100-this.energyBonus).setStyle(new Style().setColor(TextFormatting.AQUA)));
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
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


    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtadditions.multiblock.large_chemical_reactor.tooltip.1"));
        tooltip.add(I18n.format("gtadditions.multiblock.large_chemical_reactor.tooltip.2"));
        tooltip.add(I18n.format("gtadditions.multiblock.large_chemical_reactor.tooltip.3"));

    }

    public int getEnergyBonus() {
        return this.energyBonus;
    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        this.energyBonus = 0;
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

    private static class LargeChemicalReactorWorkableHandler extends GAMultiblockRecipeLogic {

        public LargeChemicalReactorWorkableHandler(RecipeMapMultiblockController tileEntity) {
            super(tileEntity);
        }

        @Override
        protected void setupRecipe(Recipe recipe) {
            TileEntityLargeChemicalReactor metaTileEntity = (TileEntityLargeChemicalReactor) getMetaTileEntity();
            int energyBonus = metaTileEntity.getEnergyBonus();

            int[] resultOverclock = calculateOverclock(recipe.getEUt(), recipe.getDuration());
            this.progressTime = 1;

//            // perfect overclocking
//            if (resultOverclock[1] < recipe.getDuration())
//                resultOverclock[1] *= 0.5;

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
        protected int[] calculateOverclock(int EUt, long voltage, int duration) {
            int numMaintenanceProblems = (this.metaTileEntity instanceof GARecipeMapMultiblockController) ?
                    ((GARecipeMapMultiblockController) metaTileEntity).getNumProblems() : 0;

            double maintenanceDurationMultiplier = 1.0 + (0.2 * numMaintenanceProblems);
            int durationModified = (int) (duration * maintenanceDurationMultiplier);

            boolean negativeEU = EUt < 0;
            int tier = getOverclockingTier(voltage);
            if (GAValues.V[tier] <= EUt || tier == 0)
                return new int[]{EUt, durationModified};
            if (negativeEU)
                EUt = -EUt;
            int resultEUt = EUt;
            double resultDuration = durationModified;
            //do not overclock further if duration is already too small
            while (resultDuration >= 1 && resultEUt <= GAValues.V[tier - 1]) {
                resultEUt *= 4;
                resultDuration /= 4;
            }
            previousRecipeDuration = (int) resultDuration;
            return new int[]{negativeEU ? -resultEUt : resultEUt, (int) Math.ceil(resultDuration)};
        }
    }
}
