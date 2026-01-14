package gregicadditions.jei;

import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.centralmonitor.MetaTileEntityCentralMonitor;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

public class CentralMonitorInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.CENTRAL_MONITOR;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        for (int tier = 0; tier < 15; tier++) {
            int height = 3;
            GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder();
            String[] start = new String[height];
            String[] slice = new String[height];
            String[] end = new String[height];
            for (int j = 0; j < height; j++) {
                start[j] = "A";
                slice[j] = "B";
                end[j] = "A";
            }
            start[0] = "E";
            start[1] = "S";
            builder.aisle(start);
            for (int num = -3; num < Math.min(11, tier); num++) {
                builder.aisle(slice);
            }
            shapeInfos.add(builder.aisle(end)
                    .where('S', GATileEntities.CENTRAL_MONITOR, EnumFacing.WEST)
                    .where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.WEST)
                    .where('A', MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID))
                    .where('B', GATileEntities.MONITOR_SCREEN, EnumFacing.WEST)
                    .build());
        }
        return shapeInfos;
    }

    @Override
    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.central_monitor.tooltip.2", MetaTileEntityCentralMonitor.MAX_WIDTH, MetaTileEntityCentralMonitor.MAX_HEIGHT), I18n.format("gtadditions.multiblock.central_monitor.tooltip.3")};
    }
}
