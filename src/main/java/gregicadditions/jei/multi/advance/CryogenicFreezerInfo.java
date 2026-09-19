package gregicadditions.jei.multi.advance;

import gregicadditions.GAConfig;
import gregicadditions.item.metal.MetalCasing1;
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

import static gregicadditions.item.GAMetaBlocks.METAL_CASING_1;

public class CryogenicFreezerInfo  extends MultiblockInfoPage {
    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.CRYOGENIC_FREEZER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        return MultiblockShapeInfo.builder()
                .aisle("XXX", "BXH", "XXX")
                .aisle("MXX", "C#E", "XXX")
                .aisle("XXX", "IXF", "XXX")
                .where('C', GATileEntities.CRYOGENIC_FREEZER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', METAL_CASING_1.getState(MetalCasing1.CasingType.INCOLOY_MA956))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('I', PlaceholderType.INPUT_BUS,MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.SOUTH)
                .where('F', PlaceholderType.INPUT_HATCH,MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.SOUTH)
                .where('B', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)
                .where('H', PlaceholderType.OUTPUT_HATCH,MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.cryogenic_freezer.description")};
    }
}