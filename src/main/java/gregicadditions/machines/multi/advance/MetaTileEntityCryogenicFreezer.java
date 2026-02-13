package gregicadditions.machines.multi.advance;

import gregicadditions.GAMaterials;
import gregicadditions.GAUtility;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.override.MetaTileEntityVacuumFreezer;
import gregicadditions.machines.multi.simple.LargeSimpleRecipeMapMultiblockController;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static gregicadditions.client.ClientHandler.INCOLOY_MA956_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;

public class MetaTileEntityCryogenicFreezer extends MetaTileEntityVacuumFreezer {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS,
            MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS,
            GregicAdditionsCapabilities.MAINTENANCE_HATCH, MultiblockAbility.INPUT_ENERGY};

    private static final int DURATION_DECREASE_FACTOR = 35;

    private static final int ENERGY_DECREASE_FACTOR = 20;

    private FluidStack cryotheum;

    public MetaTileEntityCryogenicFreezer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId);
        this.recipeMapWorkable = new CryogenicFreezerRecipeLogic(this, ENERGY_DECREASE_FACTOR, DURATION_DECREASE_FACTOR, 100, 4);
        reinitializeStructurePattern();
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityCryogenicFreezer(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .setAmountAtLeast('L', 14)
                .where('S', selfPredicate())
                .where('L', statePredicate(getCasingState()))
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('#', isAirPredicate())
                .build();
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        this.cryotheum = GAMaterials.Cryotheum.getFluid((int) Math.pow(2, GAUtility.getTierByVoltage(this.energyContainer.getInputVoltage())));
    }

    @Override
    public IBlockState getCasingState() {
        return METAL_CASING_1.getState(MetalCasing1.CasingType.INCOLOY_MA956);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return INCOLOY_MA956_CASING;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        tooltip.add(I18n.format("gregtech.multiblock.cryogenic_freezer.description"));
        tooltip.add(I18n.format("gtadditions.multiblock.universal.tooltip.1", this.recipeMap.getLocalizedName()));
        tooltip.add(I18n.format("gtadditions.multiblock.universal.tooltip.2", 20 / 100.0));
        tooltip.add(I18n.format("gtadditions.multiblock.universal.tooltip.3", 60 / 100.0));
        tooltip.add(I18n.format("gregtech.multiblock.vol_cryo.description"));

    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return ClientHandler.FREEZER_OVERLAY;
    }


    public class CryogenicFreezerRecipeLogic extends LargeSimpleRecipeMapMultiblockController.LargeSimpleMultiblockRecipeLogic {

        public CryogenicFreezerRecipeLogic(RecipeMapMultiblockController tileEntity, int EUtPercentage, int durationPercentage, int chancePercentage, int stack) {
            super(tileEntity, EUtPercentage, durationPercentage, chancePercentage, stack);
        }

        @Override
        protected long getMaxVoltage() {
            return Math.min(GTValues.V[7], super.getMaxVoltage());
        }

        @Override
        protected boolean drawEnergy(int recipeEUt) {
            if (!cryotheum.isFluidStackIdentical(this.getInputTank().drain(cryotheum, false)))
                return false;
            this.getInputTank().drain(cryotheum, true);
            return super.drawEnergy(recipeEUt);
        }
    }
}
