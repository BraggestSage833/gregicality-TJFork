package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.metal.MetalCasing2;
import gregicadditions.machines.multi.CasingUtils;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import gregtech.api.capability.IEnergyContainer;

import javax.annotation.Nonnull;

import static gregicadditions.client.ClientHandler.STABALLOY_CASING;
import static gregicadditions.item.GAMetaBlocks.METAL_CASING_2;
import static gregtech.api.metatileentity.multiblock.MultiblockAbility.INPUT_ENERGY;

public class TileEntityLargeMixer extends LargeSimpleRecipeMapMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS, INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};


    public TileEntityLargeMixer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GARecipeMaps.LARGE_MIXER_RECIPES, GAConfig.multis.largeMixer.euPercentage, GAConfig.multis.largeMixer.durationPercentage, GAConfig.multis.largeMixer.chancedBoostPercentage, GAConfig.multis.largeMixer.stack);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new TileEntityLargeMixer(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("AXXXA", "AXXXA", "AXXXA")
                .aisle("XXXXX", "X#M#X", "AXXXA").setRepeatable(2, 4)
                .aisle("XXXXX", "X#Y#X", "AXXXA")
                .aisle("AXXXA", "AXSXA", "AXXXA")
                .setAmountAtLeast('L', 9)
                .where('S', selfPredicate())
                .where('L', statePredicate(getCasingState()))
                .where('Y', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.TUNGSTENSTEEL_GEARBOX_CASING)))
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)).or(multiiPartPredicate()))
                .where('A', (tile) -> true)
                .where('#', isAirPredicate())
                .where('M', motorPredicate())
                .build();
    }

    private static final IBlockState defaultCasingState = METAL_CASING_2.getState(MetalCasing2.CasingType.STABALLOY);
    public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeMixer.casingMaterial, defaultCasingState);


    public IBlockState getCasingState() {
        return casingState;
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeMixer.casingMaterial, STABALLOY_CASING);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
        int min = motor.getTier();

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
        } else this.maxVoltage = 8L << min * 2  ;

    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return Textures.MIXER_OVERLAY;
    }

}
