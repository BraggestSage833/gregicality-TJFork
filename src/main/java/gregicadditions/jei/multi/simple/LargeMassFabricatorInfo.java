package gregicadditions.jei.multi.simple;

import gregicadditions.GAValues;
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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
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
                .where('p', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('e', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('F', GAMetaBlocks.FIELD_GEN_CASING.getState(FieldGenCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('U', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('s', GAMetaBlocks.SENSOR_CASING.getState(SensorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('E', GAMetaBlocks.EMITTER_CASING.getState(EmitterCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
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
