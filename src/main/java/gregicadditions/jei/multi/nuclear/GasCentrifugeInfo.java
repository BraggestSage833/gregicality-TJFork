package gregicadditions.jei.multi.nuclear;

import gregicadditions.GAConfig;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.BlockMetalCasing;
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


public class GasCentrifugeInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.GAS_CENTRIFUGE;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return GAMultiblockShapeInfo.builder(FRONT, UP, RIGHT)
                .aisle("#FEM#", "#YSY#", "#####", "#####", "#####", "#####", "#####")
                .aisle("OYYYY", "YYYYY", "#ZCZ#", "#Z#Z#", "#Z#Z#", "#Z#Z#", "#Z#Z#")
                .aisle("OYYYY", "YYYYY", "#CCC#", "#####", "#####", "#####", "#####")
                .aisle("OYYYY", "YYYYY", "#ZCZ#", "#Z#Z#", "#Z#Z#", "#Z#Z#", "#Z#Z#")
                .aisle("#IYY#", "#YYY#", "#####", "#####", "#####", "#####", "#####")

                .where('S', GATileEntities.GAS_CENTRIFUGE, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)

                .where('Y', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STAINLESS_CLEAN))
                .where('Z', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                .where('C', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.NORTH)

                .where('O', PlaceholderType.OUTPUT_HATCH, MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)

                .where('F', PlaceholderType.INPUT_HATCH, MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.WEST)

                .where('I', PlaceholderType.INPUT_BUS, MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.EAST)

                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[] {I18n.format("gtadditions.multiblock.gas_centrifuge.description")};
    }


    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}
