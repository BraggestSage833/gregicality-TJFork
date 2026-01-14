package gregicadditions.jei.multi;

import gregicadditions.item.CellCasing;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;


public class BatteryTowerInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.BATTERY_TOWER;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        MultiblockShapeInfo.Builder builder = MultiblockShapeInfo.builder()
                .aisle("eCCCC", "GGGGG", "GGGGG", "GGGGG", "GGGGG", "CCCCC")
                .aisle("ECCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("SCCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("MCCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("CCCCC", "GGGGG", "GGGGG", "GGGGG", "GGGGG", "CCCCC")
                .where('S', GATileEntities.BATTERY_TOWER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('C', GAMetaBlocks.METAL_CASING_1.getState(MetalCasing1.CasingType.TALONITE))
                .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.BOROSILICATE_GLASS));
        for (int tier = 0; tier < 15; tier++) {
            shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(Math.min(3, tier), true), EnumFacing.WEST)
                    .where('e', GATileEntities.getEnergyHatch(Math.min(3, tier), false), EnumFacing.WEST)
                    .where('R', GAMetaBlocks.CELL_CASING.getState(CellCasing.CellType.values()[Math.max(0, tier - 3)]))
                    .build());
        }
        return shapeInfos;
    }

    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.battery_tower.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}
