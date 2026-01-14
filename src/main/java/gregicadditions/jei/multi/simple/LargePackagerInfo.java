package gregicadditions.jei.multi.simple;

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
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        for (int tier = 0; tier < 15; tier++) {
            GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                    .aisle("XXX", "XXX", "XXX");
            for (int j = -1; j < Math.min(5, tier); j++) {
                builder.aisle("IXO", "XCX", "XRX");
            }
            shapeInfos.add(builder.aisle("XHX", "XSX", "XEX")
                    .where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('S', GATileEntities.LARGE_PACKAGER, EnumFacing.WEST)
                    .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                    .where('X', TileEntityLargePackager.casingState)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.SOUTH)
                    .where('R', GAMetaBlocks.ROBOT_ARM_CASING.getState(RobotArmCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('C', GAMetaBlocks.CONVEYOR_CASING.getState(ConveyorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.large_packager.description")};
    }
}