package gregicadditions.jei.multi;

import gregicadditions.GAConfig;
import gregicadditions.item.CellCasing;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GATransparentCasing;
import gregicadditions.item.metal.MetalCasing1;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
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
    public MultiblockShapeInfo getMatchingShapes() {
        return MultiblockShapeInfo.builder()
                .aisle("eCCCC", "GGGGG", "GGGGG", "GGGGG", "GGGGG", "CCCCC")
                .aisle("ECCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("SCCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("MCCCC", "GRRRG", "GRRRG", "GRRRG", "GRRRG", "CCCCC")
                .aisle("CCCCC", "GGGGG", "GGGGG", "GGGGG", "GGGGG", "CCCCC")
                .where('S', GATileEntities.BATTERY_TOWER, EnumFacing.WEST)
                .where('M', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                .where('C', GAMetaBlocks.METAL_CASING_1.getState(MetalCasing1.CasingType.TALONITE))
                .where('G', GAMetaBlocks.TRANSPARENT_CASING.getState(GATransparentCasing.CasingType.BOROSILICATE_GLASS))
                .where('E', PlaceholderType.ENERGY_OUTPUT_HATCH ,GATileEntities.getEnergyHatch(0, true), EnumFacing.WEST)
                .where('e', PlaceholderType.ENERGY_INPUT_HATCH, GATileEntities.getEnergyHatch(0, false), EnumFacing.WEST)
                .where('R', GAMetaBlocks.CELL_CASING.getState(CellCasing.CellType.values()[0]))
                .build();
    }

    public String[] getDescription() {
        return new String[]{I18n.format("gtadditions.multiblock.battery_tower.description")};
    }

    @Override
    public float getDefaultZoom() {
        return 0.6f;
    }
}
