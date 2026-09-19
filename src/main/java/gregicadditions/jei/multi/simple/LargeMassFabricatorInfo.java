package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.item.components.FieldGenCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.item.components.SensorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.uumatter.TileEntityLargeMassFabricator;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.BlockWireCoil;
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

public class LargeMassFabricatorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_MASS_FABRICATOR;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("XXXXX", "#P#P#", "#P#P#", "#P#P#", "XXXXX")
                .aisle("IXXXX", "PXGXP", "PXGXP", "PXGXP", "iXXXX")
                .aisle("IXXXX", "#pUp#", "#psp#", "#pUp#", "iGGGX")
                .aisle("IXXXX", "#pcp#", "#FEF#", "#pcp#", "iGGGX")
                .aisle("IXXXX", "#pcp#", "#FEF#", "#pcp#", "iGGGX")
                .aisle("IXXXX", "#pUp#", "#psp#", "#pUp#", "iGGGX")
                .aisle("IXXXX", "PXGXP", "PXGXP", "PXGXP", "iXXXX")
                .aisle("XHSeX", "#P#P#", "#P#P#", "#P#P#", "XoXoX")

                .where('S', GATileEntities.LARGE_MASS_FABRICATOR, EnumFacing.WEST)
                .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', TileEntityLargeMassFabricator.casingState)

                .where('c', MetaBlocks.WIRE_COIL.getState(BlockWireCoil.CoilType.SUPERCONDUCTOR))
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
                .where('P', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE))
                .where('p', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))

                .where('e', PlaceholderType.ENERGY_INPUT_HATCH,
                        GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)

                .where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)

                .where('i', PlaceholderType.INPUT_HATCH, MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.NORTH)

                .where('o', PlaceholderType.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)

                .where('F', PlaceholderType.FIELD_GEN, GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[0]))
                .where('U', PlaceholderType.PUMP, GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[0]))
                .where('s', PlaceholderType.SENSOR, GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[0]))
                .where('E', PlaceholderType.EMITTER, GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[0]))
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_mass_fabricator.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.5F;
    }
}
