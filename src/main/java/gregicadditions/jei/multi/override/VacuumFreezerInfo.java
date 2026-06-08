package gregicadditions.jei.multi.override;

import gregicadditions.GAConfig;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
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


public class VacuumFreezerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.VACUUM_FREEZER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes() {
        return MultiblockShapeInfo.builder()
                .aisle("XXX", "BXH", "XXX")
                .aisle("MXX", "C#E", "XXX")
                .aisle("XXX", "IXF", "XXX")
                .where('C', GATileEntities.VACUUM_FREEZER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('X', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.ALUMINIUM_FROSTPROOF))
                .where('E', PlaceholderType.ENERGY_INPUT_HATCH,
                        GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)
                .where('I', PlaceholderType.INPUT_BUS,
                        MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.SOUTH)
                .where('F', PlaceholderType.INPUT_HATCH,
                        MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.SOUTH)
                .where('B', PlaceholderType.OUTPUT_BUS,
                        MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)
                .where('H', PlaceholderType.OUTPUT_HATCH,
                        MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)
                .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gregtech.multiblock.vacuum_freezer.description")};
    }

}
