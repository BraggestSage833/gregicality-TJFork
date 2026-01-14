package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.ConveyorCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeCutting;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeCuttingInfo extends MultiblockInfoPage {

    @Override
    public MultiblockControllerBase getController() {
        return GATileEntities.LARGE_CUTTING;
    }

    @Override
    public List<MultiblockShapeInfo> getMatchingShapes() {
        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
        for (int tier = 0; tier < 15; tier++) {
            GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
                    .aisle("EXXXX", "XXX#X", "##X#X");
            for(int j = -1; j < Math.min(7, tier); j++) {
					builder.aisle("IXXCX", "OXXMX", "##X#X");
            }
            shapeInfos.add(builder.aisle("iHXXX", "XSX#X", "##X#X")
                    .where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.NORTH)
                    .where('S', GATileEntities.LARGE_CUTTING, EnumFacing.WEST)
                    .where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
                    .where('X', TileEntityLargeCutting.casingState)
                    .where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
                    .where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
                    .where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .where('C', GAMetaBlocks.CONVEYOR_CASING.getState(ConveyorCasing.CasingType.values()[Math.max(0, tier - 1)]))
                    .build());
        }
        return shapeInfos;
    }

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_cutting.description")};
	}
}
