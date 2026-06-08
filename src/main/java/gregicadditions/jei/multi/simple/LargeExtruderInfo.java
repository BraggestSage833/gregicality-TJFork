package gregicadditions.jei.multi.simple;

import gregicadditions.GAConfig;
import gregicadditions.item.GAMetaBlocks;
import gregicadditions.item.components.PistonCasing;
import gregicadditions.jei.GAMultiblockShapeInfo;
import gregicadditions.machines.GATileEntities;
import gregicadditions.machines.multi.simple.TileEntityLargeExtruder;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;
import gregtech.common.metatileentities.MetaTileEntities;
import gregtech.integration.jei.multiblock.MultiblockInfoPage;
import gregtech.integration.jei.multiblock.MultiblockShapeInfo;
import gregtech.integration.jei.multiblock.channel.PlaceholderType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

import static gregtech.api.multiblock.BlockPattern.RelativeDirection.*;

public class LargeExtruderInfo extends MultiblockInfoPage {

	@Override
	public MultiblockControllerBase getController() {
		return GATileEntities.LARGE_EXTRUDER;
	}

	@Override
	public MultiblockShapeInfo getMatchingShapes() {
		return GAMultiblockShapeInfo.builder(FRONT, UP, LEFT)
				.aisle("XXXX", "XXXX", "XXX#")
				.aisle("IXXX", "XCPX", "OXX#")
				.aisle("IXXX", "XCPX", "OXX#")
				.aisle("IXXX", "XCPX", "OXX#")
				.aisle("IXXX", "XCPX", "OXX#")

				.aisle("EHXX", "XSXX", "XXX#")

				.where('E', GATileEntities.getEnergyHatch(0, false), EnumFacing.NORTH)
				.where('S', GATileEntities.LARGE_EXTRUDER, EnumFacing.WEST)
				.where('H', GATileEntities.MAINTENANCE_HATCH[0], EnumFacing.WEST)
				.where('X', TileEntityLargeExtruder.casingState)

				.where('I', PlaceholderType.INPUT_BUS,
						MetaTileEntities.ITEM_IMPORT_BUS[0], EnumFacing.NORTH)

				.where('O', PlaceholderType.OUTPUT_BUS,
						MetaTileEntities.ITEM_EXPORT_BUS[0], EnumFacing.NORTH)

				.where('P', GAMetaBlocks.PISTON_CASING.getState(PistonCasing.CasingType.values()[0]))
				.where('C', MetaBlocks.BOILER_CASING.getState(BlockBoilerCasing.BoilerCasingType.TUNGSTENSTEEL_PIPE))
				.build();
	}

	@Override
	public String[] getDescription() {
		return new String[]{I18n.format("gtadditions.multiblock.large_extruder.description")};
	}
}
