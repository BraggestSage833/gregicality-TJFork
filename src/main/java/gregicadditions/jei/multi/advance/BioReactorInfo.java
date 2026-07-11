package gregicadditions.jei.multi.advance;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing2;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.components.EmitterCasing;
import gregicadditions.item.components.FieldGenCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.item.components.SensorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;


public class BioReactorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.BIO_REACTOR;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {

        return GAMultiblockShapeInfo.builder()
                .aisle("oXXXX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                .aisle("IXXXX", "G###G", "G#E#G", "G###G", "XXXXX")
                .aisle("SXXXe", "G#P#G", "GsFsG", "G#P#G", "MXXXX")
                .aisle("OXXXX", "G###G", "G#E#G", "G###G", "XXXXX")
                .aisle("iXXXX", "XGGGX", "XGGGX", "XGGGX", "XXXXX")
                .where('S', GATileEntities.BIO_REACTOR, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', GAMetaBlocks.MUTLIBLOCK_CASING2.getState(GAMultiblockCasing2.CasingType.BIO_REACTOR))
                .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.OSMIRIDIUM_GLASS))
                .where('e', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('O', PlaceholderType.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.WEST)
                .where('o', PlaceholderType.INPUT_HATCH, MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)
                .where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.WEST)
                .where('i', PlaceholderType.OUTPUT_BUS, MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.WEST)
                .where('E', PlaceholderType.EMITTER, GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[0]))
                .where('P', PlaceholderType.PUMP, GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[0]))
                .where('F', PlaceholderType.FIELD_GEN, GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[0]))
                .where('s', PlaceholderType.SENSOR, GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[0]))
                .build();

    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.bio_reactor.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.7f;
    }
}
