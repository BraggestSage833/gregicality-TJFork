package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;


public class CrackerUnitInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.CRACKER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("XCMCX", "XCSCF", "XCXCX")
                .aisle("XCXCX", "H###X", "XCXCX")
                .aisle("XCXCX", "XCECF", "XCXCX")
                .where('S', GATileEntities.CRACKER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
                .where('F',PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[9], EnumFacing.SOUTH )
                .where('F',PlaceholderType.OUTPUT_HATCH,MetaTileEntities.FLUID_EXPORT_HATCH[9], EnumFacing.SOUTH )
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('C',PlaceholderType.COIL).build();
    }




    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.cracker.description")};
    }

}
