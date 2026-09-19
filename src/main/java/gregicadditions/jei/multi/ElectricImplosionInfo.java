package gregicadditions.jei.multi;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMultiblockCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class ElectricImplosionInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.ELECTRIC_IMPLOSION;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder()
                .aisle("IXX", "GXG", "GXG", "GXG", "GXG", "GXG", "XXX")
                .aisle("SXE", "X#X", "X#X", "X#X", "X#X", "X#X", "XmX")
                .aisle("OMX", "GXG", "GXG", "GXG", "GXG", "GXG", "XXX")
                .where('S', getController(), EnumFacing.WEST)
                .where('G', MetaBlocks.MUTLIBLOCK_CASING.getState(BlockMultiblockCasing.MultiblockCasingType.GRATE_CASING))
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.SOUTH)
                .where('m', GATileEntities.MUFFLER_HATCH[0], EnumFacing.UP)
                .where('X', GAMetaBlocks.METAL_CASING_1.getState(MetalCasing1.CasingType.INCOLOY_MA956))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('O', PlaceholderType.OUTPUT_BUS, MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, 0)], EnumFacing.WEST)
                .where('I', PlaceholderType.INPUT_BUS,MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, 0)], EnumFacing.WEST)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.electric_implosion.description")};
    }


    @Override
    public float getDefaultZoom() {
        return 0.7f;
    }
}
