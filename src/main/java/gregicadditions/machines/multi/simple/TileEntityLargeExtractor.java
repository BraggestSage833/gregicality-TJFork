package gregicadditions.machines.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.GAValues;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.client.ClientHandler;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.multi.CasingUtils;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.multiblock.PatternMatchContext;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.OrientedOverlayRenderer;
import gregtech.api.render.Textures;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import gregtech.api.capability.IEnergyContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static gregtech.api.metatileentity.multiblock.MultiblockAbility.INPUT_ENERGY;

public class TileEntityLargeExtractor extends MultiRecipeMapMultiblockController {

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {MultiblockAbility.IMPORT_ITEMS, MultiblockAbility.EXPORT_ITEMS, MultiblockAbility.EXPORT_FLUIDS, INPUT_ENERGY, GregicAdditionsCapabilities.MAINTENANCE_HATCH};

    public TileEntityLargeExtractor(ResourceLocation metaTileEntityId, RecipeMap<?> recipeMap) {
        super(metaTileEntityId, recipeMap, GAConfig.multis.largeExtractor.euPercentage, GAConfig.multis.largeExtractor.durationPercentage, GAConfig.multis.largeExtractor.chancedBoostPercentage, GAConfig.multis.largeExtractor.stack,
                new RecipeMap[]{RecipeMaps.FLUID_EXTRACTION_RECIPES, RecipeMaps.EXTRACTOR_RECIPES});
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder holder) {
        return new TileEntityLargeExtractor(metaTileEntityId, RecipeMaps.FLUID_EXTRACTION_RECIPES);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXXXX", "X###X", "X###X", "XXXXX")
                .aisle("XXXXX", "#XXX#", "#XXX#", "XXXXX")
                .aisle("XXXXX", "#PpM#", "#XpX#", "XXXXX")
                .aisle("XXXXX", "#XSX#", "#XXX#", "XXXXX")
                .aisle("XXXXX", "X###X", "X###X", "XXXXX")
                .setAmountAtLeast('L', 12)
                .where('S', selfPredicate())
                .where('L', statePredicate(getCasingState()))
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('p', statePredicate(MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE)))
                .where('#', (tile) -> true)
                .where('P', pumpPredicate())
                .where('M', motorPredicate())
                .build();
    }

    private static final IBlockState defaultCasingState = GAMetaBlocks.METAL_CASING_1.getState(MetalCasing1.CasingType.TALONITE);
    public static final IBlockState casingState = CasingUtils.getConfigCasingBlockState(GAConfig.multis.largeExtractor.casingMaterial, defaultCasingState);


    public IBlockState getCasingState() {
        return casingState;
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return CasingUtils.getConfigCasingTexture(GAConfig.multis.largeExtractor.casingMaterial, ClientHandler.TALONITE_CASING);
    }

    @Override
    protected void formStructure(PatternMatchContext context) {
        super.formStructure(context);
        PumpCasing.CasingType pump = context.getOrDefault("Pump", PumpCasing.CasingType.PUMP_LV);
        MotorCasing.CasingType motor = context.getOrDefault("Motor", MotorCasing.CasingType.MOTOR_LV);
        int min = Math.min(pump.getTier(), motor.getTier());

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
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.multiblock.recipe", this.recipeMap.getLocalizedName()));
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Nonnull
    @Override
    protected OrientedOverlayRenderer getFrontOverlay() {
        return (getRecipeMapIndex() == 0) ? Textures.FLUID_EXTRACTOR_OVERLAY : Textures.EXTRACTOR_OVERLAY;
    }

    @Override
    public OrientedOverlayRenderer getRecipeMapOverlay(int recipeMapIndex) {
        return (getRecipeMapIndex() == 0) ? Textures.FLUID_EXTRACTOR_OVERLAY : Textures.EXTRACTOR_OVERLAY;
    }
}
