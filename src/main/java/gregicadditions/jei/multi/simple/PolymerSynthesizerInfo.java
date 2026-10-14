package gregicadditions.jei.multi.simple;

import gregicadditions.channels.GAChannelRegistry;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.channels.ChannelRegistry;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class PolymerSynthesizerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.POLY_SYN;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return MultiblockShapeInfo.builder()
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")
                .aisle("U  F  U", " CoSIi ", "       ", "       ", "       ", " CCmCC ", "U  F  U")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("UFFTFFU", " C H C ", "  PHP  ", "   H   ", "  PHP  ", " C H C ", "UFFTFFU")
                .aisle("U  F  U", " C   C ", "  FPF  ", "       ", "  FPF  ", " C   C ", "U  F  U")
                .aisle("U  F  U", " CCqCC ", "       ", "       ", "       ", " CCCCC ", "U  F  U")
                .aisle("UUUUUUU", "U     U", "U     U", "U     U", "U     U", "U     U", "UUUUUUU")
                .where('S', getController(), EnumFacing.NORTH)
				.where('C' , (GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)))
				.where('U' ,(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT)))
				.where('F' ,(GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
				.where('P' , (GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE)))
                .where('m' , GATileEntities.MAINTENANCE_HATCH[2], EnumFacing.NORTH)
                .where('q',  ChannelRegistry.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.SOUTH)
                .where('o',  ChannelRegistry.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .where('i',  ChannelRegistry.INPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .where('I',  ChannelRegistry.INPUT_BUS, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .where('H',  GAChannelRegistry.PUMP, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .where('T',  GAChannelRegistry.MOTOR, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .where(' ', Blocks.AIR.getDefaultState())
                .build();

    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.polymer.synth.description")};
    }



    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}

