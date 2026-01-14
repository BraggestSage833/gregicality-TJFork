package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.item.components.PumpCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeElectrolyzer;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeElectrolyzerInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_ELECTROLYZER;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		for (int tier = 0; tier < 15; tier++) {
			GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
					.aisle("XXCXX", "XXCXX", "XXCXX", "XX#XX");
			for (int j = -1; j < Math.min(5, tier); j++) {
				builder.aisle("iXCXi", "IP#MO", "XXCXX", "X###X");
			}
			shapeInfos.add(builder.aisle("EXHXX", "XXSXX", "XXCXX", "XX#XX")
					.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.NORTH)
					.where('S', GATileEntities.LARGE_ELECTROLYZER, EnumFacing.WEST)
					.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
					.where('X', TileEntityLargeElectrolyzer.casingState)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.NORTH)
					.where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.NORTH)
					.where('o', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.SOUTH)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.SOUTH)
					.where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.where('P', GAMetaBlocks.PUMP_CASING.getState(PumpCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.where('C', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.STEEL_PIPE))
					.build());
		}
		return shapeInfos;
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_electrolyzer.description")};
	}
}
