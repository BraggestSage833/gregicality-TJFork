package gregicadditions.machines.multi.simple;

import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.recipes.GARecipeMaps;
import gregtech.api.capability.IEnergyContainer;
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
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;

import static gregtech.api.metatileentity.multiblock.MultiblockAbility.INPUT_ENERGY;

public class MetaTileEntityPolymerSynthesizer extends MultiRecipeMapMultiblockController {
    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.IMPORT_FLUIDS, MultiblockAbility.EXPORT_FLUIDS, MultiblockAbility.INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};



    public MetaTileEntityPolymerSynthesizer(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId,
                GARecipeMaps.POLYMERS_SYN,
                80,
                60,
                60,
                16,
                new RecipeMap[]{GARecipeMaps.POLYMERS_SYN, GARecipeMaps.PCB_FACTORY});
    }
    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new MetaTileEntityPolymerSynthesizer(metaTileEntityId);
    }
    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")
                .aisle("U  F  U", " CCCCC ", "       ", "       ", "       ", " CCCCC ", "U  F  U")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("UFFTFFU", " C H C ", "  PHP  ", "   H   ", "  PHP  ", " C H C ", "UFFTFFU")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("U  F  U", " CCSCC ", "       ", "       ", "       ", " CCCCC ", "U  F  U")
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")

                .where('S', selfPredicate())

				.where('C', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)).or(abilityPartPredicate(ALLOWED_ABILITIES)).or(multiiPartPredicate()))

				.where('T', motorPredicate())

				.where('U', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)))

				.where('F', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))

				.where('H' , pumpPredicate())

				.where('P', statePredicate(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))

                .where(' ', tile -> true)

                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return ClientHandler.CHEMICALLY_INERT;
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
        PumpCasing.CasingType pump = context.getOrDefault("Pump", PumpCasing.CasingType.PUMP_LV);
        int min = Collections.min(Arrays.asList(motor.getTier(), pump.getTier()));

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
        return Textures.CHEMICAL_REACTOR_OVERLAY;
    }

    @Override
    public OrientedOverlayRenderer getRecipeMapOverlay(int recipeMapIndex) {
        return Textures.CHEMICAL_REACTOR_OVERLAY;
    }


}
