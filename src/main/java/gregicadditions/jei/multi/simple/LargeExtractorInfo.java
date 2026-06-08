package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeExtractor;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeExtractorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_EXTRACTOR;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("XXXXX", "X###X", "X###X", "XXXXX")
                .aisle("XXXXX", "#XXX#", "#XXX#", "XXXXX")
                .aisle("XXXXX", "#PpM#", "#XpX#", "XXXXX")
                .aisle("XXXXX", "#XSX#", "#XXX#", "XXXXX")
                .aisle("XXHoE", "I###O", "X###X", "XXXXX")

                .where('S', GATileEntities.LARGE_EXTRACTOR, EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', TileEntityLargeExtractor.casingState)

                .where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)

                .where('I', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)

                .where('O', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)

                .where('o', PlaceholderType.OUTPUT_HATCH,
                        MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)

                .where('P', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[0]))
                .where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[0]))
                .where('p', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE))
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_extractor.description")};
    }
}
