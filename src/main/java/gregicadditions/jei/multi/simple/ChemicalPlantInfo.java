package gregicadditions.jei.multi.simple;

import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.item.components.MotorCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class ChemicalPlantInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.CHEMICAL_PLANT;
	}

	@Override
	public List<MultiblockShapeInfo> getMatchingShapes() {
		List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
		GAMultiblockShapeInfo.Builder builder = GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("X###X", "EXXXX", "X###X", "XXXXX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("X###X", "XPPPX", "XMMMX", "XPPPX", "X###X")
				.aisle("XXXXX", "XCCCX", "XPPPX", "XCCCX", "XXXXX")
				.aisle("H###X", "SioIO", "X###X", "XXXXX", "X###X")
				.where('S', GATileEntities.CHEMICAL_PLANT, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT))
				.where('P', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE));
		for (int tier = 0; tier < 15; tier++) {
			shapeInfos.add(builder.where('E', GATileEntities.getEnergyHatch(tier, false), EnumFacing.EAST)
					.where('I', MetaTileEntities.ITEM_IMPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('O', MetaTileEntities.ITEM_EXPORT_BUS[Math.min(9, tier)], EnumFacing.WEST)
					.where('i', MetaTileEntities.FLUID_IMPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
					.where('o', MetaTileEntities.FLUID_EXPORT_HATCH[Math.min(9, tier)], EnumFacing.WEST)
					.where('M', GAMetaBlocks.MOTOR_CASING.getState(MotorCasing.CasingType.values()[Math.max(0, tier - 1)]))
					.where('C', GAMetaBlocks.getCoils(tier))
					.build());
		}
		return shapeInfos;
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.chemical_plant.description")};
	}

	@Override
	public float getDefaultZoom() {
		return 0.8f;
	}
}
