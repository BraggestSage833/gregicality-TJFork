package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.GAMultiblockCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
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

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeChemicalReactorInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_CHEMICAL_REACTOR;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("XXX", "XEX", "XXX")
				.aisle("IXX", "iPX", "XCX")
				.aisle("OHX", "oSX", "XXX")
				.where('S', GATileEntities.LARGE_CHEMICAL_REACTOR, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.CHEMICALLY_INERT))
				.where('P', GAMetaBlocks.MUTLIBLOCK_CASING.getState(GAMultiblockCasing.CasingType.PTFE_PIPE))

				.where('E', PlaceholderType.ENERGY_INPUT_HATCH,
						GATileEntities.getEnergyHatch(0, false), EnumFacing.EAST)

				.where('I', PlaceholderType.INPUT_BUS,
						MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)

				.where('O', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)

				.where('i', PlaceholderType.INPUT_HATCH,
						MetaTileEntities.FLUID_IMPORT_HATCH[0], EnumFacing.NORTH)

				.where('o', PlaceholderType.OUTPUT_HATCH,
						MetaTileEntities.FLUID_EXPORT_HATCH[0], EnumFacing.NORTH)

				.where('C', PlaceholderType.COIL)
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_chemical_reactor.description")};
	}
}
