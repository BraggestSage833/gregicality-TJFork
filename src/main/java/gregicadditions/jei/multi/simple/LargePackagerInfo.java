package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.ConveyorCasing;
import gregicadditions.item.components.RobotArmCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargePackager;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargePackagerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_PACKAGER;
    }

    @Override
    public MultiblockShapeInfo getMatchingShapes(int extent) {
        GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                .aisle("XXX", "XXX", "XXX");
        for (int j = -1; j < Math.min(5, extent); j++) {
            builder.aisle("IXO", "XCX", "XRX");
        }

        return builder.aisle("XHX", "XSX", "XEX")
            .where('E', PlaceholderType.ENERGY_INPUT_HATCH,GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
            .where('S', GATileEntities.LARGE_PACKAGER, EnumFacing.WEST)
            .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
            .where('X', TileEntityLargePackager.casingState)
            .where('I', PlaceholderType.INPUT_BUS,MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)
            .where('O', PlaceholderType.OUTPUT_BUS,MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.SOUTH)
            .where('R', PlaceholderType.ROBOT_ARM,GAMetaBlocks.ROBOT_ARM_CASING.getState(RobotArmCasing.CasingType.values()[0]))
            .where('C',PlaceholderType.CONVEYOR, GAMetaBlocks.CONVEYOR_CASING.getState(ConveyorCasing.CasingType.values()[0]))
            .build();
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_packager.description")};
    }
}