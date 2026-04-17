package gregicadditions.machines.multi.advance.hyper;

import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.GAExplosive;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMetaItems;
import gregicadditions.item.GAMultiblockCasing2;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.item.fusion.GAFusionCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.multi.GAFuelRecipeLogic;
import gregicadditions.machines.multi.GAFueledMultiblockController;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.FuelRecipeLogic;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.BlockWorldState;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.machines.FuelRecipeMap;
import gregtech.api.recipes.recipes.FuelRecipe;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Materials;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static gregicadditions.GAMaterials.FreeAlphaGas;
import static gregicadditions.GAMaterials.Infinity;
import static gregicadditions.client.ClientHandler.ENRICHED_NAQUADAH_ALLOY_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;

public class MetaTileEntityBlackHoleGenerator extends GAFueledMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.OUTPUT_ENERGY, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS, GregicAdditionsCapabilities.MAINTENANCE_HATCH, MultiblockAbility.IMPORT_ITEMS
    };
    protected Item lep = GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.LEPTONIC_CHARGE).getItem();
    protected Item quant = GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.QCD_CHARGE).getItem();
    protected Item inf = GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.INFINITY_CHARGE).getItem();
    private IItemHandlerModifiable outputInventory;
    private IMultipleTankHandler exportFluidHandler;
    private IItemHandlerModifiable inputInventory;

    public MetaTileEntityBlackHoleGenerator(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.BLACK_HOLE_GENERATOR, GAValues.V[GAValues.MAX]);
    }

    public static Predicate<BlockWorldState> emitterPredicate() {
        return (blockWorldState) -> {
            IBlockState blockState = blockWorldState.getBlockState();
            if (!(blockState.getBlock() instanceof EmitterCasing)) {
                return false;
            } else {
                EmitterCasing motorCasing = (EmitterCasing) blockState.getBlock();
                EmitterCasing.CasingType tieredCasingType = motorCasing.getState(blockState);
                EmitterCasing.CasingType currentCasing = blockWorldState.getMatchContext().getOrPut("Emitter", tieredCasingType);
                return currentCasing.getName().equals(tieredCasingType.getName());
            }
        };
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityBlackHoleGenerator(metaTileEntityId);
    }

    @Override
    protected FuelRecipeLogic createWorkable(long maxVoltage) {
        return new MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler(this, recipeMap, () -> energyContainer, () -> importFluidHandler, () -> inputInventory, maxVoltage);
    }

    @Override
    protected void addDisplayText(List<ITextComponent> textList) {
        if (isStructureFormed()) {
            FluidStack fuelStack = ((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getFuelStack();
            int fuelAmount = fuelStack == null ? 0 : fuelStack.amount;
            int itemCount = ((BlackHoleGeneratorWorkableHandler) workableHandler).getLepCharges();

            ITextComponent fuelName = new TextComponentTranslation(fuelAmount == 0 ? "gregtech.fluid.empty" : fuelStack.getUnlocalizedName());

            if (fuelStack == null)
                textList.add(new TextComponentTranslation("gregtech.multiblock.large_rocket_engine.no_fuel").setStyle(new Style().setColor(TextFormatting.RED)));
            else {
                textList.add(new TextComponentTranslation("gregtech.multiblock.diesel_engine.fuel_amount", fuelAmount, fuelName).setStyle(new Style().setColor(TextFormatting.AQUA)));
            }
            textList.add(new TextComponentTranslation("gregtech.multiblock.black.hole.charges", ((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getLepCharges(),GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.LEPTONIC_CHARGE).getDisplayName()).setStyle(new Style().setColor(TextFormatting.AQUA)));
            textList.add(new TextComponentTranslation("gregtech.multiblock.black.hole.charges", ((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getInfinityCharges(),GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.INFINITY_CHARGE).getDisplayName()).setStyle(new Style().setColor(TextFormatting.AQUA)));

            textList.add(new TextComponentTranslation("gregtech.multiblock.black.hole.cycle",((BlackHoleGeneratorWorkableHandler) workableHandler).getCurrentCycle()));

        }
        super.addDisplayText(textList);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gtadditions.multiblock.black_hole_generator.tooltip.1"));

    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("###############", "######CCC######", "######C#C######", "######C#C######", "######C#C######", "######C#C######", "######C#C######", "######CCC######", "###############")
                .aisle("######C#C######", "#####FFFFF#####", "###############", "###############", "###############", "###############", "###############", "#####FFFFF#####", "######C#C######")
                .aisle("######C#C######", "###FF#####FF###", "###############", "###############", "###############", "###############", "###############", "###FF#####FF###", "######C#C######")
                .aisle("######C#C######", "##F#########F##", "#####FFFFF#####", "###############", "###############", "###############", "#####FFFFF#####", "##F#########F##", "######C#C######")
                .aisle("######C#C######", "##F#########F##", "####F#XXX#F####", "######XXX######", "######XXX######", "######XXX######", "####F#XXX#F####", "##F#########F##", "######C#C######")
                .aisle("######C#C######", "#F###########F#", "###F#X###X#F###", "#####X###X#####", "#####X###X#####", "#####X###X#####", "###F#X###X#F###", "#F###########F#", "######C#C######")
                .aisle("#CCCCCCMCCCCCC#", "CF####XXX####FC", "C##FX#####XF##C", "C###X#####X###C", "C###X#####X###C", "C###X#####X###C", "C##FX#####XF##C", "CF####XXX####FC", "#CCCCCCMCCCCCC#")
                .aisle("######MMM######", "CF####XXX####FC", "###FX#####XF###", "####X#####X####", "####X#####X####", "####X#####X####", "###FX#####XF###", "CF####XXX####FC", "######MMM######")
                .aisle("#CCCCCCMCCCCCC#", "CF####XXX####FC", "C##FX#####XF##C", "C###X#####X###C", "C###X#####X###C", "C###X#####X###C", "C##FX#####XF##C", "CF####XXX####FC", "#CCCCCCMCCCCCC#")
                .aisle("######C#C######", "#F###########F#", "###F#X###X#F###", "#####X###X#####", "#####X###X#####", "#####X###X#####", "###F#X###X#F###", "#F###########F#", "######C#C######")
                .aisle("######C#C######", "##F#########F##", "####F#XXX#F####", "######XXX######", "######XXX######", "######XXX######", "####F#XXX#F####", "##F#########F##", "######C#C######")
                .aisle("######C#C######", "##F#########F##", "#####FFFFF#####", "###############", "###############", "###############", "#####FFFFF#####", "##F#########F##", "######C#C######")
                .aisle("######C#C######", "###FF#####FF###", "###############", "###############", "###############", "###############", "###############", "###FF#####FF###", "######C#C######")
                .aisle("######C#C######", "#####FFFFF#####", "###############", "###############", "###############", "###############", "###############", "#####FFFFF#####", "######C#C######")
                .aisle("###############", "######CSC######", "######C#C######", "######C#C######", "######C#C######", "######C#C######", "######C#C######", "######CCC######", "###############")
                .setAmountAtLeast('p', 1)
                .where('p', abilityPartPredicate(MultiblockAbility.EXPORT_ITEMS))
                .setAmountAtLeast('L', 1)
                .where('L', abilityPartPredicate(MultiblockAbility.IMPORT_ITEMS))
                .where('M', emitterPredicate())
                .where('C', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('X', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING2.getState(GAMultiblockCasing2.CasingType.STELLAR_CONTAINMENT)))
                .where('F', statePredicate(GAMetaBlocks.FUSION_CASING.getState(GAFusionCasing.CasingType.FUSION_COIL_2)))
                .where('S', selfPredicate())
                .where('#', (tile) -> true)
                .build();
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        initializeAbilities();
    }

    @Override
    protected void updateFormedValid() {
        super.updateFormedValid();
        if (this.isActive()) {
            if (!(this.energyContainer.getEnergyCapacity() == this.energyContainer.getEnergyStored()) && this.energyContainer.getOutputAmperage() !=0) {
                if (getWorld().rand.nextInt(100) % 5 == 0 && this.getOffsetTimer() % 20 == 0 && outputInventory != null && ((BlackHoleGeneratorWorkableHandler) workableHandler).getCurrentCycle() > 0) {
                    ItemHandlerHelper.insertItemStacked(this.outputInventory, OreDictUnifier.get(OrePrefix.dust, Infinity, getWorld().rand.nextInt(((BlackHoleGeneratorWorkableHandler) workableHandler).getCurrentCycle() * 128)), false);
                }
                if (this.getOffsetTimer() % 20 == 0 && (((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getFuelStack() != null)) {
                    this.exportFluidHandler.fill(new FluidStack(((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getFuelStack().getFluid(), (((MetaTileEntityBlackHoleGenerator.BlackHoleGeneratorWorkableHandler) workableHandler).getPreviousRecipeFuelUsage() / 20) - 40), true);
                }
            }
        }


    }

    @Override
    public void invalidateStructure() {
        super.invalidateStructure();
        resetTileAbilities();
    }

    private void initializeAbilities() {
        this.outputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.EXPORT_ITEMS));
        this.exportFluidHandler = new FluidTankList(true, getAbilities(MultiblockAbility.EXPORT_FLUIDS));
        this.inputInventory = new ItemHandlerList(getAbilities(MultiblockAbility.IMPORT_ITEMS));
    }

    private void resetTileAbilities() {
        this.outputInventory = new ItemStackHandler(0);
        this.exportFluidHandler = new FluidTankList(true);
        this.inputInventory = new ItemStackHandler(0);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return ENRICHED_NAQUADAH_ALLOY_CASING;
    }

    protected IBlockState getCasingState() {
        return METAL_CASING_2.getState(MetalCasing2.CasingType.ENRICHED_NAQUADAH_ALLOY);
    }


    public static class BlackHoleGeneratorWorkableHandler extends GAFuelRecipeLogic {

        private final int maxCycleLength = 200;
        private int currentCycle = 0;


        private Supplier<IItemHandlerModifiable> importHandler;


        public BlackHoleGeneratorWorkableHandler(MetaTileEntity metaTileEntity, FuelRecipeMap recipeMap,
                                                 Supplier<IEnergyContainer> energyContainer, Supplier<IMultipleTankHandler> fluidTank, Supplier<IItemHandlerModifiable> importHandler, long maxVoltage) {
            super(metaTileEntity, recipeMap, energyContainer, fluidTank, maxVoltage);
            this.importHandler = importHandler;
        }

        public FluidStack getFuelStack() {
            if (previousRecipe == null)
                return null;
            FluidStack fuelStack = previousRecipe.getRecipeFluid();
            return fluidTank.get().drain(new FluidStack(fuelStack.getFluid(), Integer.MAX_VALUE), false);
        }

        public int getMaxCycleLength() {
            return maxCycleLength;
        }

        public int getPreviousRecipeFuelUsage() {
            if (previousRecipe == null) {
                return 0;
            }
            return calculateFuelAmount(previousRecipe);
        }

        @Override
        protected long startRecipe(FuelRecipe currentRecipe, int fuelAmountUsed, int recipeDuration) {
            if (importHandler.get() != null && this.metaTileEntity != null && currentCycle <= 200) {
                int sizeInventory = importHandler.get().getSlots();

                for (int i = 0; i < sizeInventory; i++) {
                    ItemStack slot = importHandler.get().getStackInSlot(i);
                    if (slot.isItemEqual(GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.LEPTONIC_CHARGE)) && currentCycle < 100 ) {
                        importHandler.get().extractItem(i, 1, false);
                        this.currentCycle += 1;
                        break;
                    }
                    else if (slot.isItemEqual(GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.INFINITY_CHARGE)) && currentCycle >= 100 ) {
                        importHandler.get().extractItem(i, 1, false);
                        this.currentCycle += 1;
                        break;
                    }
                    else if (currentCycle < 200){
                        this.currentCycle -= 1;
                    }
                }
            }
            currentCycle = 200;
            return (currentRecipe.getMinVoltage()) * currentCycle * 2;
        }

        @Override
        public void update() {
            super.update();
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = super.serializeNBT();
            compound.setInteger("Cycle", currentCycle);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound compound) {
            super.deserializeNBT(compound);
            this.currentCycle = compound.getInteger("Cycle");
        }

        public int getCurrentCycle() {
            return currentCycle;
        }

        public int getLepCharges() {
            int sizeInventory = importHandler.get().getSlots();
            int itemCount = 0;

            for (int i = 0; i < sizeInventory; i++) {
                ItemStack slot = importHandler.get().getStackInSlot(i);
                if (slot.isItemEqual(GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.LEPTONIC_CHARGE))) {
                    itemCount += slot.getCount();
                }
            }
            return itemCount;
        }

        public int getInfinityCharges() {
            int sizeInventory = importHandler.get().getSlots();
            int itemCount = 0;

            for (int i = 0; i < sizeInventory; i++) {
                ItemStack slot = importHandler.get().getStackInSlot(i);
                if (slot.isItemEqual(GAMetaBlocks.EXPLOSIVE.getItemVariant(GAExplosive.ExplosiveType.INFINITY_CHARGE))) {
                    itemCount += slot.getCount();
                }
            }
            return itemCount;
        }
    }
}